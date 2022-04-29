package core.classes;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Set;

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

            if(currentAddedValues >= devicesNumber) {
                everyonePutAValue = true;
                currentAddedValues = 0;

            }

        }
    }

    public T getValueFor(Thread thread){
        synchronized (threadToValueHashMap) {
           return threadToValueHashMap.get(thread);
        }
    }

    public AbstractMap<Thread, T> db() {
        synchronized (threadToValueHashMap) {
            return new AbstractMap<>() {
                @Override
                public Set<Entry<Thread, T>> entrySet() {
                    return threadToValueHashMap.entrySet();
                }
            };
        }
    }

    public void setValueFor(boolean t){
        synchronized (threadToValueHashMap) {
            everyonePutAValue = false;
        }
    }


    public boolean isEveryonePutAValue() {
        synchronized (threadToValueHashMap) {
            return everyonePutAValue;
        }
    }


    @Override
    public String toString() {
        var sb = new StringBuilder();
        threadToValueHashMap.forEach((key, value) -> sb.append(value).append("\n"));
        sb.append("\n");
        return sb.toString();
    }
}
