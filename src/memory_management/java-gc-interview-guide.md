# Java Garbage Collection — Deep Dive for Interviews

## 1. Why GC Exists (the pitch you should be able to give in 30 seconds)

In C/C++, you `malloc`/`free` manually — miss a `free` and you leak memory; free too early and you get dangling pointers/use-after-free bugs. Java's GC automates this: it tracks which objects are still reachable from your running program and reclaims memory for the ones that aren't. Trade-off: you give up manual control and predictability (pause times) for safety and productivity.

**Core idea:** An object is garbage if it's **unreachable** from a set of "GC Roots." It doesn't matter if the object still has valid data — if nothing points to it (directly or transitively), it's collected.

---

## 2. GC Roots — what "reachable" actually means

GC Roots are the starting points of reachability analysis. An object is alive if there's a path from any GC Root to it.

GC Roots include:
- Local variables and parameters on the stack of any live thread
- Active Java threads themselves
- Static fields of loaded classes
- JNI references (native code holding references)
- Objects used for synchronization (monitors currently locked)

**Interview trap:** People say "objects with refcount 0 are garbage." Java does NOT use reference counting (that fails on cyclic references — A points to B, B points to A, both unreachable from roots, but ref count is 1 each). Java uses **reachability from roots**, which correctly handles cycles.

---

## 3. JVM Memory Layout (need this before generations make sense)

```
JVM Memory
├── Heap                     ← GC manages this
│   ├── Young Generation
│   │   ├── Eden
│   │   ├── Survivor 0 (S0)
│   │   └── Survivor 1 (S1)
│   └── Old Generation (Tenured)
├── Metaspace                ← class metadata (native memory, replaced PermGen since Java 8)
├── Stack (per thread)       ← local vars, method frames — NOT GC'd, popped on return
├── PC Registers (per thread)
└── Native Method Stack
```

- **Metaspace vs PermGen:** Pre-Java 8, class metadata lived in PermGen (fixed size, caused `OutOfMemoryError: PermGen space` a lot). Java 8+ moved this to Metaspace, which lives in native memory and grows dynamically (still can OOM if you leak classloaders, e.g. bad hot-reload setups).

---

## 4. The Generational Hypothesis (the single most important idea)

**Empirical observation:** Most objects die young. Request-scoped objects, temporary strings, loop iterators — created and discarded fast. Few objects survive to become long-lived (caches, singletons, connection pools).

This is *why* the heap is split into Young and Old generations — it lets GC apply different strategies:
- **Young Gen:** collected frequently, fast, because most objects here die anyway (cheap to just copy the few survivors out)
- **Old Gen:** collected rarely, more expensive, because most objects here really are long-lived

### Young Generation flow (this is the part people fumble in interviews — know it cold)

1. New objects are allocated in **Eden**.
2. When Eden fills up, a **Minor GC** is triggered.
3. Minor GC uses a **copying collector**: live objects in Eden + the active survivor space are copied to the *other* survivor space. Dead objects are simply not copied (Eden is then wiped entirely — no scanning of garbage needed, that's the efficiency win).
4. Each time an object survives a Minor GC, its **age** counter increments.
5. Once an object's age crosses a threshold (`-XX:MaxTenuringThreshold`, default is often 15), it gets **promoted** to the Old Generation.
6. Objects can also be promoted early if a survivor space fills up (survivor space overflow) — this is called **premature promotion** and it's a real tuning problem (floods Old Gen, triggers more Major GCs).

**Why two survivor spaces (S0/S1) instead of one?** Avoids fragmentation. At any time one is "from" (active, has live objects) and one is "to" (empty). Copying GC always copies into the empty one, then swaps roles. This means survivor space is always compacted as a side-effect — no separate compaction pass needed for young gen.

### Old Generation

- Collected via **Major GC** (or "Full GC" when it also touches young gen + metaspace).
- Typically uses mark-sweep-compact rather than pure copying (too expensive to copy a mostly-live large space).
- Major GCs are **much more expensive** — this is where "stop-the-world" pause complaints usually come from in production.

---

## 5. The Three Core GC Algorithms (mechanisms, not collectors — collectors combine these)

### Mark-Sweep
1. **Mark:** traverse from GC roots, mark every reachable object.
2. **Sweep:** scan the whole heap, reclaim memory for unmarked objects.
- Problem: leaves **fragmentation** — free memory in scattered chunks, so a large object allocation can fail even if total free space is enough.

### Mark-Sweep-Compact
Same as above, plus a **compact** phase: live objects are shifted together to one end of memory, eliminating fragmentation. Cost: compaction is slow because you're moving objects and fixing up every reference.

### Copying (used in Young Gen)
Split space into two halves. Only live objects get copied from the active half to the other half; the active half is then cleared entirely. Fast when most objects die (which is exactly the young-gen case) but wastes 50% of space by design (half is always empty).

---

## 6. GC Collectors (the actual implementations — pick-your-algorithm)

### Serial GC (`-XX:+UseSerialGC`)
- Single-threaded, stop-the-world for both young and old gen.
- Good for: small heaps, single-core machines, client apps. Bad for: any server workload.

### Parallel GC (`-XX:+UseParallelGC`) — "throughput collector"
- Multiple threads do the GC work (still stop-the-world), but does the work faster in wall-clock time.
- Optimizes for **throughput** (total app work done / total time), not pause time.
- Was the default collector pre-Java 9.

### CMS — Concurrent Mark Sweep (deprecated in Java 9, removed in Java 14)
- First attempt at low-pause GC: does marking **concurrently** with the application (mostly), only stops the world briefly for initial mark and remark.
- Problem: doesn't compact → fragmentation → eventually forces a full stop-the-world compacting GC anyway. Also suffers "concurrent mode failure" if Old Gen fills up before CMS finishes. This is why it got replaced.

### G1 (Garbage First) — **default since Java 9**, most likely to come up in interviews
- Splits heap into many equal-sized **regions** (not fixed Eden/Survivor/Old blocks) — a region can be Eden, Survivor, Old, or Humongous (for very large objects) at any time.
- Tracks **liveness per region**, and prioritizes collecting regions with the *most garbage first* (hence the name) — best "bang per pause."
- Goal-oriented: you set a target pause time (`-XX:MaxGCPauseMillis=200`), and G1 tries to hit it by choosing how many regions to collect per cycle.
- Does incremental compaction (region by region), so it largely avoids CMS's fragmentation problem.
- Still has stop-the-world pauses, just much shorter and more predictable than Parallel/CMS.

### ZGC and Shenandoah (low-latency collectors, Java 11+/12+)
- Target **sub-millisecond pause times** even on multi-terabyte heaps, by doing almost everything concurrently (marking, compaction, reference updates) using colored pointers / load barriers.
- Pause time is essentially independent of heap size — huge deal for very large heaps.
- Trade a bit of throughput/CPU overhead for extremely predictable low pauses. Good for latency-sensitive services (matches the kind of latency-obsessed work you did with your auth caching — same instinct, JVM-level).

**Quick interview-ready comparison table:**

| Collector | Parallelism | Pause behavior | Best for |
|---|---|---|---|
| Serial | Single-threaded | STW, long | Tiny heaps/single core |
| Parallel | Multi-threaded | STW, long, but fast total time | Batch/throughput jobs |
| CMS | Mostly concurrent | Short, but fragmentation issues | Legacy, deprecated |
| G1 | Concurrent + regional | Short, tunable target | Default general-purpose (most services) |
| ZGC/Shenandoah | Almost fully concurrent | Sub-ms, heap-size independent | Huge heaps, strict latency SLAs |

---

## 7. Reference Types (comes up constantly, ties GC to real code)

Java has four reference strength levels, used to influence what GC treats as reachable:

1. **Strong reference** — normal `Object o = new Object()`. Never collected while reachable.
2. **Soft reference** (`SoftReference<T>`) — collected only when JVM is low on memory. Good for memory-sensitive caches.
3. **Weak reference** (`WeakReference<T>`) — collected at the *next* GC cycle regardless of memory pressure, as soon as no strong refs remain. Used in `WeakHashMap`, and classically for avoiding memory leaks in listener/callback registries.
4. **Phantom reference** (`PhantomReference<T>`) — `get()` always returns `null`; used purely to get a notification *after* an object has been finalized/collected, via a `ReferenceQueue`. Used for cleanup actions (successor to `finalize()`, see `Cleaner` API).

**Interview gotcha:** static collections holding strong references to objects (e.g., a `static Map` used as a cache without eviction) is the #1 real-world Java memory leak pattern — GC can't help you because the objects genuinely *are* reachable.

---

## 8. Escape Analysis & Stack Allocation (JIT-adjacent, often asked as a "trick" question)

The JIT compiler can prove some objects **never escape** the method they're created in (no reference leaves via return, field assignment, or being passed elsewhere). For such objects, the JVM may:
- Perform **scalar replacement** — break the object into its primitive fields and keep them in registers/stack, skipping heap allocation entirely.
- Enable **lock elision** — if a lock is only ever seen by one thread, the JIT can eliminate the synchronization overhead.

This is why "everything in Java is heap-allocated" is technically an oversimplification — the JVM optimizes escape-analyzed objects away from the heap when it safely can.

---

## 9. Key JVM Flags (practical, shows you've actually tuned a JVM)

```
-Xms512m               initial heap size
-Xmx2g                 max heap size
-Xmn256m               young gen size
-XX:MaxTenuringThreshold=15    age before promotion to Old Gen
-XX:+UseG1GC           use G1 collector
-XX:MaxGCPauseMillis=200       target pause time for G1
-XX:+PrintGCDetails    (or -Xlog:gc* on newer JVMs) verbose GC logging
-XX:+HeapDumpOnOutOfMemoryError   dump heap on OOM for later analysis (e.g. with Eclipse MAT)
```

---

## 10. Common Interview Questions (with the answer shape you should give)

**Q: What's the difference between Minor GC, Major GC, and Full GC?**
Minor = young gen only, frequent, cheap. Major = old gen collection. Full GC = collects young + old + metaspace together, typically the most expensive, and what you see when the app "freezes" noticeably in production.

**Q: How would you debug a production app suffering from long GC pauses?**
Enable GC logging (`-Xlog:gc*`), check pause frequency/duration, check Old Gen occupancy trend for slow growth (memory leak signal), take a heap dump on OOM and analyze with Eclipse MAT/VisualVM to find dominator objects, check if premature promotion is happening (survivor space too small).

**Q: Can you force garbage collection?**
`System.gc()` only *suggests* it to the JVM — it's not guaranteed to run (the JVM can ignore it), and it's generally an anti-pattern to call in production code because it can trigger an unwanted Full GC.

**Q: Why doesn't Java use reference counting?**
Doesn't handle cycles (two objects referencing each other stay "alive" forever even if unreachable from roots), plus per-assignment counting overhead. Reachability tracing from GC roots avoids both problems.

**Q: What causes a memory leak in a garbage-collected language?**
Any code path that keeps a *strong reference* to an object longer than needed — unbounded caches, listeners never unregistered, ThreadLocal not cleaned up in pooled threads, static collections. GC can only reclaim what's *unreachable*; it can't tell "logically dead but still referenced" objects from real long-lived ones.

**Q: Explain G1's "Garbage First" approach in one line.**
It divides the heap into regions and always collects the regions with the highest garbage-to-live ratio first, to get the most memory back per unit of pause time.

**Q: Given your production experience — how does understanding GC change how you write code?**
Good place to bring in your own experience: things like avoiding unnecessary object churn in hot paths (e.g. string concatenation in loops, boxing in tight loops), sizing caches deliberately (which connects directly to your Caffeine Cache work), and being deliberate about what lives in static/long-lived scope vs request scope.

---

## 11. One-Page Mental Model to Recall Under Pressure

- Objects die → reclaimed if unreachable from GC Roots (not refcounting)
- Heap = Young (Eden + 2 Survivors) + Old, because most objects die young
- Young Gen: copying collector, cheap, frequent (Minor GC)
- Old Gen: mark-sweep-compact, expensive, rare (Major/Full GC)
- Modern default = G1: regions, prioritizes highest-garbage regions, tunable pause target
- Extreme low-latency needs = ZGC/Shenandoah: pause time independent of heap size
- Reference types (Strong/Soft/Weak/Phantom) let you influence collection behavior deliberately
- Escape analysis can skip heap allocation entirely for provably non-escaping objects
