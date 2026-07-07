package java_locks;

/**
 * ============================================================
 * SYNCHRONIZED KEYWORD / INTRINSIC LOCK NOTES
 * ============================================================
 *
 * Problem:
 * Multiple threads updating shared state can cause race conditions.
 *
 * Example:
 *
 * count++;
 *
 * Internally:
 *
 * 1. Read count
 * 2. Add 1
 * 3. Write count
 *
 * Since these 3 operations are not atomic,
 * multiple threads can interfere with each other.
 *
 * Example:
 *
 * count = 5
 *
 * T1 reads 5
 * T2 reads 5
 *
 * T1 writes 6
 * T2 writes 6
 *
 * Expected:
 * 7
 *
 * Actual:
 * 6
 *
 * This is called a Race Condition.
 *
 * Solution:
 * synchronized keyword
 * ReentrantLock
 * AtomicInteger
 *
 */


/**
 * ============================================================
 * WITHOUT LOCK
 * ============================================================
 *
 * No synchronization is present.
 *
 * Multiple threads can enter updateValue()
 * simultaneously.
 *
 * Race conditions are possible.
 *
 * Expected Value:
 * 20000
 *
 * Actual Value:
 * Usually less than 20000
 * (depends on scheduling)
 *
 */
class WithoutLockExample {

    private int count = 0;

    public void updateValue() {
        count++;
    }

    public void print() {
        System.out.println(count);
    }
}


/**
 * ============================================================
 * WITH SYNCHRONIZED
 * ============================================================
 *
 * synchronized provides mutual exclusion.
 *
 * Only one thread can execute updateValue()
 * on the same object at a time.
 *
 * Lock acquired:
 *
 * this object monitor
 *
 * Equivalent:
 *
 * synchronized(this){
 *      count++;
 * }
 *
 * Other threads attempting to enter
 * updateValue() must wait.
 *
 * Race condition removed.
 *
 * Expected Value:
 * 20000
 *
 * Actual Value:
 * 20000
 *
 */
class WithLockExample {

    private int count = 0;

    /**
     * synchronized method
     *
     * Lock Used:
     * this
     *
     * Thread Flow:
     *
     * T1 acquires lock
     * T2 waits
     *
     * T1 completes
     * T1 releases lock
     *
     * T2 acquires lock
     */
    public synchronized void updateValue() {
        count++;
    }

    public void print() {
        System.out.println(count);
    }
}


/**
 * ============================================================
 * THREAD METHODS USED
 * ============================================================
 *
 * start()
 * --------
 * Creates a new thread and executes run()
 * on that thread.
 *
 * join()
 * -------
 * Current thread waits until target
 * thread finishes execution.
 *
 * Without join():
 * print() may execute before
 * worker threads complete.
 *
 * Result can be incorrect.
 *
 */
public class Synchronized_lock {

    public static void main(String[] args)
            throws InterruptedException {

        runWithoutLock();

        System.out.println();

        runWithLock();
    }

    /**
     * ============================================================
     * DEMO 1 : WITHOUT SYNCHRONIZATION
     * ============================================================
     *
     * Two threads increment same variable.
     *
     * Shared Resource:
     * count
     *
     * Expected:
     * 20000
     *
     * Actual:
     * Usually < 20000
     */
    private static void runWithoutLock()
            throws InterruptedException {

        WithoutLockExample example =
                new WithoutLockExample();

        Thread t1 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                example.updateValue();
            }

        });

        Thread t2 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                example.updateValue();
            }

        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Without Lock Value");

        example.print();
    }

    /**
     * ============================================================
     * DEMO 2 : WITH SYNCHRONIZATION
     * ============================================================
     *
     * synchronized guarantees that only one
     * thread updates count at a time.
     *
     * Expected:
     * 20000
     *
     * Actual:
     * 20000
     */
    private static void runWithLock()
            throws InterruptedException {

        WithLockExample example =
                new WithLockExample();

        Thread t1 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                example.updateValue();
            }

        });

        Thread t2 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                example.updateValue();
            }

        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("With Lock Value");

        example.print();
    }
}