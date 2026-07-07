package concurrency;

/**
 * ============================================================================
 * Volatile Notes
 * ============================================================================
 *
 * What is volatile?
 * -----------------
 *
 * volatile is a Java keyword used to guarantee visibility
 * of changes across threads.
 *
 * When a variable is marked volatile:
 *
 *      - Reads always happen from main memory.
 *      - Writes are immediately flushed to main memory.
 *
 * Threads do not rely on stale cached copies.
 *
 * ============================================================================
 *
 * Problem Without volatile
 * ------------------------
 *
 * Consider:
 *
 *      private static boolean keepRunning = true;
 *
 * Main Thread:
 *
 *      keepRunning = false;
 *
 * Worker Thread:
 *
 *      while(keepRunning) {
 *          ...
 *      }
 *
 * The worker thread may cache keepRunning.
 *
 * Even after Main Thread changes it to false,
 * worker thread may continue seeing true forever.
 *
 * Result:
 *
 *      Infinite loop.
 *
 * ============================================================================
 *
 * Solution
 * --------
 *
 * Mark variable as volatile.
 *
 *      private static volatile boolean keepRunning = true;
 *
 * Now every thread sees the latest value.
 *
 * ============================================================================
 *
 * Important Interview Point
 * -------------------------
 *
 * volatile guarantees:
 *
 *      ✔ Visibility
 *
 * volatile DOES NOT guarantee:
 *
 *      ✘ Atomicity
 *
 * ============================================================================
 */
public class VolatileExample {

    /**
     * =========================================================================
     * Visibility Example
     * =========================================================================
     *
     * Main thread updates keepRunning.
     *
     * Worker thread immediately sees latest value.
     *
     * Because variable is volatile.
     *
     * =========================================================================
     */
    private static volatile boolean keepRunning = true;

    public static void main(String[] args)
            throws InterruptedException {

        Thread worker = new Thread(() -> {

            while (keepRunning) {

                System.out.println("Working...");

            }

            System.out.println("Worker stopped.");
        });

        worker.start();

        Thread.sleep(2000);

        System.out.println(
                "Main thread signaling worker to stop..."
        );

        keepRunning = false;
    }

    /**
     * =========================================================================
     * Volatile Does NOT Provide Atomicity
     * =========================================================================
     *
     * Many developers mistakenly think volatile
     * makes operations thread-safe.
     *
     * Example:
     *
     *      volatile int count = 0;
     *
     *      count++;
     *
     * Still NOT thread-safe.
     *
     * =========================================================================
     *
     * Why?
     *
     * count++ is actually:
     *
     *      temp = count;
     *      temp = temp + 1;
     *      count = temp;
     *
     * Multiple threads can interleave these steps.
     *
     * Race conditions still occur.
     *
     * =========================================================================
     */
    private volatile int count = 0;

    public void increment() {

        count++;

    }

    /**
     * =========================================================================
     * When To Use volatile?
     * =========================================================================
     *
     * Suitable For:
     *
     *      Stop Flags
     *      Configuration Flags
     *      Status Indicators
     *      Shutdown Signals
     *
     * Examples:
     *
     *      volatile boolean running;
     *      volatile boolean shutdownRequested;
     *      volatile int currentState;
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * When NOT To Use volatile?
     * =========================================================================
     *
     * Avoid volatile when:
     *
     *      Multiple operations must be atomic.
     *
     * Example:
     *
     *      count++
     *      money += amount
     *      inventory--
     *
     * Use:
     *
     *      AtomicInteger
     *      synchronized
     *      ReentrantLock
     *
     * instead.
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * volatile vs synchronized
     * =========================================================================
     *
     * volatile:
     *
     *      Visibility only
     *
     * synchronized:
     *
     *      Visibility
     *      Atomicity
     *      Mutual Exclusion
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * Interview Summary
     * =========================================================================
     *
     * volatile:
     *
     *      ✔ Visibility
     *      ✘ Atomicity
     *
     * Use for:
     *
     *      Flags
     *      State Variables
     *      Shutdown Signals
     *
     * Do NOT use for:
     *
     *      Counters
     *      Money Updates
     *      Inventory Updates
     *
     * Classic Interview Question:
     *
     *      Is volatile int count thread-safe?
     *
     * Answer:
     *
     *      No.
     *
     *      count++ is not atomic.
     *
     * =========================================================================
     */
}