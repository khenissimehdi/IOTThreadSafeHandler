package examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import core.classes.IOTHandler;
import core.fakeApi.Heat4J;

public class ApplicationTemp {

    public static void main(String[] args) throws InterruptedException {
        var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");
        var threads = new ArrayList<Thread>();
        record Data(String name, int temp) {
            public Data {
                Objects.requireNonNull(name);
            }

            @Override
            public String toString() {
                return "Temperature in the room " + name + '\'' + " : " + temp;
            }
        }
        var iotHandler = new IOTHandler<Data>(rooms.size());


        IntStream.range(0, rooms.size()).forEach(i -> {
            var thread = new Thread(() -> {
                for(;;) {
                    try {
                        if(iotHandler.isEveryonePutAValue()) {
                            iotHandler.setValueFor(false);
                            System.out.println(iotHandler);
                            var value = iotHandler.db().values().stream().
                                    map(Data::temp).mapToInt(Integer::intValue).average();
                            if(value.isEmpty()) {
                                System.out.println("Failed to get the temperature");
                            } else {
                                System.out.println("the average is : " + value.getAsDouble());
                            }
                             continue;
                        }

                        var temperature = Heat4J.retrieveTemperature(rooms.get(i));
                        var data = new Data(rooms.get(i), temperature);

                        iotHandler.put(Thread.currentThread(), data);

                    } catch (InterruptedException e) {
                       throw new AssertionError(e);
                    }

                }
            });
            thread.start();
            threads.add(thread);
        });

    }
}