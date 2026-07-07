package java_locks;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockNotes {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private String sharedString;

    public void readValue(){
        try{
            rwLock.readLock().lock();
            System.out.println("Value of String: "+sharedString);

        }finally {
            rwLock.readLock().unlock();
        }
    }


}
