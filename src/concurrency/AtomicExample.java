package concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ============================================================================
 * AtomicInteger Notes
 * ============================================================================
 *
 * What is AtomicInteger?
 * ----------------------
 *
 * AtomicInteger provides thread-safe operations on integers
 * without using synchronized or explicit locks.
 *
 * Package:
 *
 *      java.util.concurrent.atomic
 *
 * ============================================================================
 *
 * Why Do We Need AtomicInteger?
 * -----------------------------
 *
 * Consider:
 *
 *      int count = 0;
 *
 *      count++;
 *
 * count++ is NOT atomic.
 *
 * Internally:
 *
 *      temp = count;
 *      temp = temp + 1;
 *      count = temp;
 *
 * Multiple threads can interleave these operations
 * causing race conditions.
 *
 * ============================================================================
 *
 * Solution:
 *
 *      AtomicInteger
 *
 * provides atomic operations implemented using
 * CPU-level Compare-And-Swap (CAS).
 *
 * No explicit locking required.
 *
 * ============================================================================
 *
 * Advantages:
 *
 *      ✔ Thread-safe
 *      ✔ Lock-free
 *      ✔ Better performance than synchronized
 *        for simple counters
 *
 * ============================================================================
 */
public class AtomicExample {

    /**
     * Not thread-safe.
     */
    private static int unsafeInt = 0;

    /**
     * Thread-safe.
     */
    private static final AtomicInteger safeInt =
            new AtomicInteger(0);

    /**
     * =========================================================================
     * incrementAndGet()
     * =========================================================================
     *
     * Atomically:
     *
     *      increment value
     *      return updated value
     *
     * Equivalent Conceptually:
     *
     *      count++
     *
     * but thread-safe.
     *
     * =========================================================================
     */
    public static void increment() {

        unsafeInt++;

        safeInt.incrementAndGet();

    }

    public static void main(String[] args)
            throws InterruptedException {

        example();

    }

    /**
     * =========================================================================
     * Race Condition Example
     * =========================================================================
     *
     * Two threads increment both counters 1000 times.
     *
     * Expected:
     *
     *      2000
     *
     * unsafeInt:
     *
     *      Race conditions occur.
     *      Usually less than 2000.
     *
     * safeInt:
     *
     *      Always exactly 2000.
     *
     * =========================================================================
     */
    static void example()
            throws InterruptedException {

        Thread t1 = new Thread(() -> {

            for (int i = 0; i < 1000; i++) {

                increment();

            }

        });

        Thread t2 = new Thread(() -> {

            for (int i = 0; i < 1000; i++) {

                increment();

            }

        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(
                "Unsafe Value = " + unsafeInt
        );

        System.out.println(
                "Atomic Value = " + safeInt.get()
        );

    }

    /**
     * =========================================================================
     * Common AtomicInteger Methods
     * =========================================================================
     */

    public void incrementAndGetExample() {

        safeInt.incrementAndGet();

    }

    public void getAndIncrementExample() {

        safeInt.getAndIncrement();

    }

    public void decrementAndGetExample() {

        safeInt.decrementAndGet();

    }

    public void addAndGetExample() {

        safeInt.addAndGet(10);

    }

    public void getExample() {

        int value = safeInt.get();

    }

    public void setExample() {

        safeInt.set(100);

    }

    /**
     * =========================================================================
     * compareAndSet()
     * =========================================================================
     *
     * Most important method.
     *
     * Compare current value with expected value.
     *
     * If equal:
     *      update value
     *
     * Else:
     *      do nothing
     *
     * Returns:
     *
     *      true  -> update successful
     *      false -> update failed
     *
     * =========================================================================
     */
    public void compareAndSetExample() {

        boolean updated =
                safeInt.compareAndSet(
                        100,
                        200
                );

        System.out.println(updated);

    }

    /**
     * =========================================================================
     * CAS (Compare And Swap)
     * =========================================================================
     *
     * AtomicInteger internally uses:
     *
     *      Compare-And-Swap (CAS)
     *
     * Steps:
     *
     *      1. Read current value
     *      2. Compare with expected value
     *      3. Update only if unchanged
     *
     * Implemented using CPU instructions.
     *
     * No locking required.
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * AtomicInteger vs volatile
     * =========================================================================
     *
     * volatile:
     *
     *      ✔ Visibility
     *      ✘ Atomicity
     *
     * AtomicInteger:
     *
     *      ✔ Visibility
     *      ✔ Atomicity
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * AtomicInteger vs synchronized
     * =========================================================================
     *
     * AtomicInteger:
     *
     *      Lock-free
     *      Better for simple counters
     *
     * synchronized:
     *
     *      Lock-based
     *      More flexible
     *      Supports complex operations
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * When To Use AtomicInteger?
     * =========================================================================
     *
     * Good For:
     *
     *      Counters
     *      Request Count
     *      Sequence Numbers
     *      Metrics
     *      Statistics
     *
     * Examples:
     *
     *      API Request Count
     *      Active User Count
     *      Cache Hit Count
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * Interview Summary
     * =========================================================================
     *
     * AtomicInteger:
     *
     *      Thread-safe integer operations
     *
     * Key Methods:
     *
     *      get()
     *      set()
     *      incrementAndGet()
     *      getAndIncrement()
     *      addAndGet()
     *      compareAndSet()
     *
     * Internally Uses:
     *
     *      CAS (Compare-And-Swap)
     *
     * Better than synchronized
     * for simple counters.
     *
     * =========================================================================
     */
}