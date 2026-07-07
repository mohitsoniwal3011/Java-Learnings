package concurrency;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {
    static ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
    static {
        map.put(1,1);
    }
    static void checkAndPut(){
        if(map.containsKey(1)){
            map.put(1, map.get(1) +1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<Integer, Integer> internalMap = new ConcurrentHashMap<>();

        internalMap.put(1,1); // each method is thread safe, but the methods combined are not thread safe
        internalMap.put(3,4);

        internalMap.putIfAbsent(1,5); // this is thread safe

        internalMap.computeIfAbsent(8, (key) -> key+10);


        // when we combine the methods, the block in itself is not thread safe below is example
        threadUnsafeExample();
        System.out.println(map);
        System.out.println(internalMap);

    }

    static void threadUnsafeExample() throws InterruptedException {
        Thread t1 = new Thread(() ->{
            for(int i=0; i<1000;i++){
                checkAndPut();
            }
        });

        Thread t2 = new Thread(() ->{
            for(int i=0; i<1000;i++){
                checkAndPut();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();


    }
}
