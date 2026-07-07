package threads;

/**
 * ============================================================
 * JAVA THREAD NOTES
 * ============================================================
 *
 * A Thread is the smallest unit of execution inside a process.
 *
 * Every Java application starts with one thread:
 *
 *      main
 *
 * Threads allow concurrent execution of tasks.
 *
 * Commonly used methods:
 * 1. start()
 * 2. run()
 * 3. join()
 * 4. sleep()
 * 5. interrupt()
 * 6. isAlive()
 * 7. currentThread()
 * 8. getName()
 * 9. setName()
 * 10. yield()
 * 11. setDaemon()
 */
public class ThreadNotes {

    /**
     * ------------------------------------------------------------
     * start()
     * ------------------------------------------------------------
     *
     * Definition:
     * Starts a new thread and invokes run() on that thread.
     *
     * Important:
     * - Creates a NEW thread.
     * - Can be called only once.
     *
     * Example:
     *
     * Thread t = new Thread(() -> {
     *     System.out.println("Worker Thread");
     * });
     *
     * t.start();
     *
     * Output:
     * Worker Thread
     *
     * State Transition:
     *
     * NEW -> RUNNABLE -> RUNNING -> TERMINATED
     */
    public void startExample() {

        Thread t = new Thread(() -> {
            System.out.println("Executing in worker thread");
        });

        t.start();
    }

    /**
     * ------------------------------------------------------------
     * run()
     * ------------------------------------------------------------
     *
     * Definition:
     * Contains the task to be executed by a thread.
     *
     * Important:
     * Calling run() directly DOES NOT create a new thread.
     *
     * Example:
     *
     * Thread t = new Thread(...);
     * t.run();
     *
     * Executes on:
     * main thread
     */
    public void runExample() {

        Thread t = new Thread(() -> {
            System.out.println(
                    Thread.currentThread().getName());
        });

        t.run();
    }

    /**
     * ------------------------------------------------------------
     * join()
     * ------------------------------------------------------------
     *
     * Definition:
     * Waits for another thread to finish execution.
     *
     * Common Interview Usage:
     *
     * t1.start();
     * t2.start();
     *
     * t1.join();
     * t2.join();
     *
     * Ensures:
     * Main thread waits until both threads complete.
     */
    public void joinExample() throws InterruptedException {

        Thread t = new Thread(() -> {
            System.out.println("Worker Thread");
        });

        t.start();

        t.join();

        System.out.println("Main Continues");
    }

    /**
     * ------------------------------------------------------------
     * sleep()
     * ------------------------------------------------------------
     *
     * Definition:
     * Suspends the CURRENT thread for a specified duration.
     *
     * Syntax:
     *
     * Thread.sleep(milliseconds);
     *
     * Throws:
     * InterruptedException
     */
    public void sleepExample() throws InterruptedException {

        System.out.println("Start");

        Thread.sleep(2000);

        System.out.println("End");
    }

    /**
     * ------------------------------------------------------------
     * interrupt()
     * ------------------------------------------------------------
     *
     * Definition:
     * Send an interruption request to a thread.
     *
     * Important:
     * Does NOT forcibly stop the thread.
     *
     * The thread must cooperate by handling
     * InterruptedException or checking interruption status.
     */
    public void interruptExample() {

        Thread t = new Thread(() -> {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        });

        t.start();

        t.interrupt();
    }

    /**
     * ------------------------------------------------------------
     * currentThread()
     * ------------------------------------------------------------
     *
     * Definition:
     * Returns currently executing thread.
     *
     * Common Usage:
     *
     * Thread.currentThread().getName()
     */
    public void currentThreadExample() {

        System.out.println(
                Thread.currentThread().getName());
    }

    /**
     * ------------------------------------------------------------
     * isAlive()
     * ------------------------------------------------------------
     *
     * Definition:
     * Checks whether a thread is still running.
     *
     * Returns:
     * true  -> thread running
     * false -> thread terminated
     */
    public void isAliveExample() {

        Thread t = new Thread(() -> {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        });

        t.start();

        System.out.println(t.isAlive());
    }

    /**
     * ------------------------------------------------------------
     * getName() / setName()
     * ------------------------------------------------------------
     *
     * Used for thread identification and debugging.
     */
    public void threadNameExample() {

        Thread t = new Thread();

        t.setName("Worker-1");

        System.out.println(t.getName());
    }

    /**
     * ------------------------------------------------------------
     * yield()
     * ------------------------------------------------------------
     *
     * Definition:
     * Suggests scheduler that current thread is willing
     * to give up CPU temporarily.
     *
     * Important:
     * Not guaranteed.
     */
    public void yieldExample() {

        Thread.yield();
    }

    /**
     * ------------------------------------------------------------
     * setDaemon()
     * ------------------------------------------------------------
     *
     * Definition:
     * Marks thread as a daemon/background thread.
     *
     * Examples:
     * - Garbage Collector
     * - Background Cleanup Tasks
     *
     * Rule:
     * When all user threads terminate,
     * daemon threads are automatically stopped.
     */
    public void daemonExample() {

        Thread t = new Thread(() -> {

            while (true) {
                System.out.println("Daemon Running");
            }
        });

        t.setDaemon(true);

        t.start();
    }

    /**
     * ------------------------------------------------------------
     * RACE CONDITION
     * ------------------------------------------------------------
     *
     * Problem:
     *
     * count++;
     *
     * Internally:
     *
     * 1. Read count
     * 2. Add 1
     * 3. Write count
     *
     * Multiple threads can interleave these steps.
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
     * Solution:
     * - synchronized
     * - AtomicInteger
     * - ReentrantLock
     */
    public void raceConditionNotes() {

    }
}