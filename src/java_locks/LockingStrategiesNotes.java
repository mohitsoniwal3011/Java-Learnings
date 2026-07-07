package java_locks;

/**
 * ============================================================================
 * Locking Strategies Notes
 * ============================================================================
 *
 * Concurrency can be handled at multiple levels:
 *
 *      1. JVM Locks
 *      2. Database Locks
 *      3. Distributed Locks
 *
 * JVM Locks:
 *
 *      synchronized
 *      ReentrantLock
 *      ReentrantReadWriteLock
 *      StampedLock
 *
 * ============================================================================
 */
public class LockingStrategiesNotes {

    /**
     * =========================================================================
     * Coarse-Grained Locking
     * =========================================================================
     *
     * One lock protects multiple resources.
     *
     * Example:
     *
     *      One lock for entire booking system.
     *
     * =========================================================================
     *
     * Example:
     *
     *      User A -> Seat A1
     *      User B -> Seat B2
     *      User C -> Seat C3
     *
     * Even though seats are different:
     *
     *      User A proceeds
     *      User B waits
     *      User C waits
     *
     * because all operations share one lock.
     *
     * =========================================================================
     *
     * Advantages:
     *
     *      Easy to implement
     *      Easier to reason about
     *      Lower risk of bugs
     *
     * Disadvantages:
     *
     *      Poor concurrency
     *      Poor scalability
     *      Threads wait unnecessarily
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * Fine-Grained Locking
     * =========================================================================
     *
     * Lock only the specific resource being modified.
     *
     * Example:
     *
     *      One lock per seat
     *      One lock per account
     *      One lock per parking spot
     *
     * =========================================================================
     *
     * Example:
     *
     *      User A -> Seat A1
     *      User B -> Seat B2
     *      User C -> Seat C3
     *
     * All three can proceed simultaneously.
     *
     * =========================================================================
     *
     * Only competing operations block each other.
     *
     * Example:
     *
     *      User A -> Seat A1
     *      User B -> Seat A1
     *
     * Now locking becomes necessary.
     *
     * =========================================================================
     *
     * Advantages:
     *
     *      Better throughput
     *      Better scalability
     *      Higher concurrency
     *
     * Disadvantages:
     *
     *      More code
     *      More complex
     *      Higher deadlock risk
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * Interview Rule
     * =========================================================================
     *
     * Prefer fine-grained locking whenever possible.
     *
     * Lock the smallest resource required.
     *
     * Examples:
     *
     *      Seat Lock
     *      User Lock
     *      Parking Spot Lock
     *      Inventory Item Lock
     *
     * Avoid locking the entire service.
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * Pessimistic Locking
     * =========================================================================
     *
     * Database-level locking strategy.
     *
     * Assumption:
     *
     *      Conflicts are likely.
     *
     * Lock first.
     * Perform operation later.
     *
     * =========================================================================
     *
     * Example:
     *
     *      SELECT *
     *      FROM seats
     *      WHERE seat_id='A1'
     *      FOR UPDATE;
     *
     * Database immediately locks row.
     *
     * =========================================================================
     *
     * Scenario:
     *
     * Transaction A:
     *
     *      Locks Seat A1
     *
     * Transaction B:
     *
     *      Tries to lock Seat A1
     *
     * Result:
     *
     *      Transaction B waits.
     *
     * =========================================================================
     *
     * Advantages:
     *
     *      Very safe
     *      No conflicting updates
     *      Easy to reason about
     *
     * Disadvantages:
     *
     *      Lower throughput
     *      Waiting
     *      Lock contention
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * Optimistic Locking
     * =========================================================================
     *
     * Database-level locking strategy.
     *
     * Assumption:
     *
     *      Conflicts are rare.
     *
     * Do not lock immediately.
     *
     * Instead:
     *
     *      Read data
     *      Modify data
     *      Verify nobody changed it
     *
     * =========================================================================
     *
     * Usually implemented using:
     *
     *      version column
     *
     * Example:
     *
     *      id | status | version
     *
     *      A1 | AVAILABLE | 1
     *
     * =========================================================================
     *
     * User A reads:
     *
     *      version = 1
     *
     * User B reads:
     *
     *      version = 1
     *
     * =========================================================================
     *
     * User A updates:
     *
     *      UPDATE seat
     *      SET status='BOOKED',
     *          version=2
     *      WHERE id='A1'
     *      AND version=1;
     *
     * Success.
     *
     * =========================================================================
     *
     * User B updates:
     *
     *      UPDATE seat
     *      SET status='BOOKED',
     *          version=2
     *      WHERE id='A1'
     *      AND version=1;
     *
     * Affected Rows = 0
     *
     * because version changed.
     *
     * User B must retry.
     *
     * =========================================================================
     *
     * Advantages:
     *
     *      High throughput
     *      No waiting
     *      Better scalability
     *
     * Disadvantages:
     *
     *      Retry logic required
     *      More application complexity
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * Pessimistic vs Optimistic Locking
     * =========================================================================
     *
     * Pessimistic:
     *
     *      Lock First
     *      Work Later
     *
     * Optimistic:
     *
     *      Work First
     *      Validate Later
     *
     * =========================================================================
     *
     * Pessimistic Locking:
     *
     *      Banking
     *      Seat Booking
     *      Payment Systems
     *
     * =========================================================================
     *
     * Optimistic Locking:
     *
     *      Product Catalog
     *      User Profile
     *      Configuration Store
     *      Inventory Reads
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * JVM Lock vs Database Lock
     * =========================================================================
     *
     * ReentrantLock:
     *
     *      Protects only current JVM.
     *
     * Example:
     *
     *      App Instance 1
     *      App Instance 2
     *      App Instance 3
     *
     * Each JVM has its own lock.
     *
     * =========================================================================
     *
     * Database Lock:
     *
     *      Shared across all application instances.
     *
     * Therefore:
     *
     *      Booking systems
     *      Payment systems
     *      Inventory systems
     *
     * often require database locking
     * in addition to JVM locking.
     *
     * =========================================================================
     */

    /**
     * =========================================================================
     * Interview Summary
     * =========================================================================
     *
     * Coarse-Grained Locking
     *      One lock for many resources.
     *
     * Fine-Grained Locking
     *      One lock per resource.
     *
     * Pessimistic Locking
     *      Lock first.
     *      Work later.
     *
     * Optimistic Locking
     *      Work first.
     *      Validate later.
     *
     * ReentrantLock
     *      JVM level protection.
     *
     * Database Locks
     *      Cross-instance protection.
     *
     * =========================================================================
     */
}