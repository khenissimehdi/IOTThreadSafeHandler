package core.classes;

import java.util.HashMap;

public class IOTHandler<T> {
    private final HashMap<Thread, T> threadToValueHashMap = new HashMap<>();
    private final int devicesNumber;
    private int currentAddedValues;
    private boolean everyonePutAValue;

    public IOTHandler(int devicesNumber){
        synchronized (threadToValueHashMap){
            this.devicesNumber = devicesNumber;
        }
    }

    public void put(Thread thread, T value) throws InterruptedException {
        synchronized (threadToValueHashMap) {

            threadToValueHashMap.put(thread, value);
            currentAddedValues++;

            while (currentAddedValues < devicesNumber) {
                threadToValueHashMap.wait();
            }
            System.out.println("heyo");
            threadToValueHashMap.notifyAll();
            //currentAddedValues = 0;
        }
    }

    public T getValueFor(Thread thread){
        synchronized (threadToValueHashMap) {
           return threadToValueHashMap.get(thread);
        }
    }

    public boolean isEveryonePutAValue() {
        synchronized (threadToValueHashMap) {
            return everyonePutAValue;
        }
    }
}
