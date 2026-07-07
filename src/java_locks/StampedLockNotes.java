package java_locks;

import java.util.concurrent.locks.StampedLock;

public class StampedLockNotes {

    private double x;
    private double y;

    private final StampedLock lock = new StampedLock();

    /**
     * =========================================================================
     * What Is A Stamp?
     * =========================================================================
     *
     * Every lock acquisition returns a long value called a stamp.
     *
     * Think of stamp as:
     *
     *      "Proof that I own this lock"
     *
     * This stamp must be supplied during unlock.
     *
     * Example:
     *
     *      long stamp = lock.readLock();
     *
     *      ...
     *
     *      lock.unlockRead(stamp);
     *
     * =========================================================================
     */
    public void stampExample() {

        long stamp = lock.readLock();

        try {

            System.out.println("Reading data");

        } finally {

            lock.unlockRead(stamp);

        }
    }

    /**
     * =========================================================================
     * Optimistic Read
     * =========================================================================
     *
     * Reads data without acquiring a real lock.
     *
     * Assumption:
     *
     *      "Probably nobody is writing."
     *
     * After reading data we validate whether
     * a writer modified data during the read.
     *
     * Faster than acquiring a real read lock.
     *
     * Best suited for:
     *
     *      Reads >>> Writes
     *
     * =========================================================================
     */
    public double optimisticReadExample() {

        long stamp = lock.tryOptimisticRead();

        double currentX = x;
        double currentY = y;

        if (!lock.validate(stamp)) {

            stamp = lock.readLock();

            try {

                currentX = x;
                currentY = y;

            } finally {

                lock.unlockRead(stamp);

            }
        }

        return currentX * currentX
                + currentY * currentY;
    }

    /**
     * =========================================================================
     * validate()
     * =========================================================================
     *
     * Checks whether a writer modified data
     * after optimistic read started.
     *
     * Returns:
     *
     *      true  -> data still valid
     *      false -> data may have changed
     *
     * =========================================================================
     */
    public boolean validateExample() {

        long stamp = lock.tryOptimisticRead();

        return lock.validate(stamp);
    }

    /**
     * =========================================================================
     * Why Fallback To Read Lock?
     * =========================================================================
     *
     * Optimistic reads do not guarantee consistency.
     *
     * If validation fails:
     *
     *      Acquire actual read lock
     *      Re-read values
     *
     * This guarantees consistent data.
     *
     * =========================================================================
     */
    public double fallbackReadExample() {

        long stamp = lock.tryOptimisticRead();

        double currentX = x;
        double currentY = y;

        if (!lock.validate(stamp)) {

            stamp = lock.readLock();

            try {

                currentX = x;
                currentY = y;

            } finally {

                lock.unlockRead(stamp);

            }
        }

        return currentX + currentY;
    }

    /**
     * =========================================================================
     * Write Lock
     * =========================================================================
     *
     * Exclusive lock.
     *
     * While write lock is active:
     *
     *      No readers allowed.
     *      No writers allowed.
     *
     * =========================================================================
     */
    public void move(double deltaX,
                     double deltaY) {

        long stamp = lock.writeLock();

        try {

            x += deltaX;
            y += deltaY;

        } finally {

            lock.unlockWrite(stamp);

        }
    }

    /**
     * =========================================================================
     * Lock Conversion
     * =========================================================================
     *
     * Unique StampedLock feature.
     *
     * Convert:
     *
     *      Read Lock -> Write Lock
     *
     * without releasing lock first.
     *
     * =========================================================================
     */
    public void lockConversionExample() {

        long stamp = lock.readLock();

        try {

            long writeStamp =
                    lock.tryConvertToWriteLock(stamp);

            if (writeStamp != 0L) {

                stamp = writeStamp;

                x++;

            }

        } finally {

            lock.unlock(stamp);

        }
    }

    /**
     * =========================================================================
     * StampedLock Is NOT Reentrant
     * =========================================================================
     *
     * ReentrantLock:
     *
     *      Same thread can acquire lock multiple times.
     *
     * StampedLock:
     *
     *      Same thread cannot safely acquire
     *      same lock again.
     *
     * Doing so may cause self-deadlock.
     *
     * =========================================================================
     */
    public void methodA() {

        long stamp = lock.writeLock();

        try {

            methodB();

        } finally {

            lock.unlockWrite(stamp);

        }
    }

    /**
     * =========================================================================
     * Self Deadlock Example
     * =========================================================================
     *
     * methodA() already owns write lock.
     *
     * methodB() tries to acquire write lock again.
     *
     * Since StampedLock is NOT reentrant:
     *
     *      Thread waits for itself forever.
     *
     * Result:
     *
     *      Self Deadlock
     *
     * =========================================================================
     */
    private void methodB() {

        long stamp = lock.writeLock();

        try {

            System.out.println("Never reached");

        } finally {

            lock.unlockWrite(stamp);

        }
    }
}