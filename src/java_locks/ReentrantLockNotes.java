package notes.java_concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ============================================================================
 * ReentrantLock Notes
 * ============================================================================
 *
 * What is ReentrantLock?
 * ----------------------
 * - Explicit locking mechanism provided by java.util.concurrent.locks package.
 * - Alternative to synchronized keyword.
 * - Gives more control over lock acquisition and release.
 *
 * Why use it?
 * -----------
 * 1. tryLock()
 * 2. tryLock(timeout)
 * 3. lockInterruptibly()
 * 4. Fair locking support
 * 5. Condition variables
 * 6. Lock state inspection APIs
 *
 * ----------------------------------------------------------------------------
 * Important Rule
 * ----------------------------------------------------------------------------
 *
 * One shared resource -> One shared lock
 *
 * Correct:
 *
 *      private final ReentrantLock lock = new ReentrantLock();
 *
 * Wrong:
 *
 *      public void update() {
 *          ReentrantLock lock = new ReentrantLock();
 *          ...
 *      }
 *
 * Creating a new lock per method call defeats synchronization.
 *
 * ============================================================================
 */
public class ReentrantLockNotes {

    private int count = 0;

    /**
     * Shared lock protecting count.
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * =========================================================================
     * lock()
     * =========================================================================
     *
     * Acquires lock.
     *
     * If another thread already owns it:
     *      -> current thread blocks
     *      -> waits indefinitely
     */
    public void lockExample() {

        lock.lock();

        try {
            count++;
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * =========================================================================
     * tryLock()
     * =========================================================================
     *
     * Attempts lock acquisition immediately.
     *
     * Returns:
     *      true  -> lock acquired
     *      false -> lock busy
     *
     * Does NOT wait.
     */
    public void tryLockExample() {

        if(lock.tryLock()) {

            try {
                count++;
            }
            finally {
                lock.unlock();
            }

        } else {

            System.out.println("Lock currently busy");

        }
    }

    /**
     * =========================================================================
     * tryLock(timeout)
     * =========================================================================
     *
     * Waits for limited time.
     *
     * Example:
     *      Wait up to 5 seconds.
     *
     * If lock becomes available:
     *      -> acquire lock
     *
     * Else:
     *      -> return false
     */
    public void timedTryLockExample() throws InterruptedException {

        if(lock.tryLock(5, TimeUnit.SECONDS)) {

            try {
                count++;
            }
            finally {
                lock.unlock();
            }
        }
    }

    /**
     * =========================================================================
     * lockInterruptibly()
     * =========================================================================
     *
     * Similar to lock()
     *
     * Difference:
     *      Waiting thread can be interrupted.
     *
     * Useful when thread should not wait forever.
     */
    public void interruptibleLockExample() throws InterruptedException {

        lock.lockInterruptibly();

        try {
            count++;
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * =========================================================================
     * isLocked()
     * =========================================================================
     *
     * Checks whether any thread currently owns lock.
     */
    public void isLockedExample() {

        boolean locked = lock.isLocked();

        System.out.println("Locked = " + locked);
    }

    /**
     * =========================================================================
     * isHeldByCurrentThread()
     * =========================================================================
     *
     * Checks whether current thread owns lock.
     */
    public void isHeldByCurrentThreadExample() {

        boolean ownedByCurrentThread =
                lock.isHeldByCurrentThread();

        System.out.println(ownedByCurrentThread);
    }

    /**
     * =========================================================================
     * getHoldCount()
     * =========================================================================
     *
     * Reentrant lock allows same thread to acquire lock multiple times.
     *
     * Example:
     *
     *      lock.lock();
     *      lock.lock();
     *
     * Hold count = 2
     */
    public void holdCountExample() {

        lock.lock();

        try {

            lock.lock();

            try {

                System.out.println(
                        "Hold Count = " +
                                lock.getHoldCount()
                );

            }
            finally {
                lock.unlock();
            }

        }
        finally {
            lock.unlock();
        }
    }

    /**
     * =========================================================================
     * getQueueLength()
     * =========================================================================
     *
     * Approximate number of threads waiting for lock.
     *
     * Mainly useful for:
     *      - Monitoring
     *      - Debugging
     */
    public void queueLengthExample() {

        int waitingThreads =
                lock.getQueueLength();

        System.out.println(waitingThreads);
    }

    /**
     * =========================================================================
     * hasQueuedThreads()
     * =========================================================================
     *
     * Returns true if threads are waiting.
     */
    public void queuedThreadsExample() {

        boolean waiting =
                lock.hasQueuedThreads();

        System.out.println(waiting);
    }

    /**
     * =========================================================================
     * Fair Locking
     * =========================================================================
     *
     * Default:
     *
     *      new ReentrantLock()
     *
     * Unfair lock
     *
     * Better throughput but may cause starvation.
     *
     * Fair lock:
     *
     *      new ReentrantLock(true)
     *
     * Threads acquire lock roughly in FIFO order.
     */
    public void fairLockExample() {

        ReentrantLock fairLock =
                new ReentrantLock(true);

        System.out.println(
                fairLock.isFair()
        );
    }

    /**
     * =========================================================================
     * Reentrancy Example
     * =========================================================================
     *
     * Same thread can acquire lock multiple times.
     *
     * This prevents self-deadlock.
     */
    public void methodA() {

        lock.lock();

        try {
            methodB();
        }
        finally {
            lock.unlock();
        }
    }

    private void methodB() {

        lock.lock();

        try {

            System.out.println(
                    "Re-entered successfully"
            );

        }
        finally {
            lock.unlock();
        }
    }
}

/**
 * ============================================================================
 * Interview Summary
 * ============================================================================
 *
 * lock()
 *      -> Wait forever
 *
 * tryLock()
 *      -> Immediate attempt
 *
 * tryLock(timeout)
 *      -> Wait limited duration
 *
 * lockInterruptibly()
 *      -> Waiting thread can be interrupted
 *
 * newCondition()
 *      -> wait()/notify() equivalent
 *
 * isLocked()
 *      -> Is lock held?
 *
 * getHoldCount()
 *      -> Reentrant acquisition count
 *
 * getQueueLength()
 *      -> Approx waiting threads
 *
 * ReentrantLock Advantages over synchronized:
 *
 *      1. tryLock()
 *      2. Timed lock acquisition
 *      3. Interruptible locking
 *      4. Fair locking
 *      5. Multiple conditions
 *
 * ============================================================================
 */