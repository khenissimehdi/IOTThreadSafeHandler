package examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import core.classes.IOTHandler;
import core.fakeApi.Heat4J;

public class ApplicationTemp {

    public static void main(String[] args) throws InterruptedException {
        var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");
        var threads = new ArrayList<Thread>();
        var iotHandler = new IOTHandler<Integer>(rooms.size());

        var temperatures = new ArrayList<Integer>();

        IntStream.range(0, rooms.size()).forEach(i -> {
            var thread = new Thread(() -> {
                for(;;) {
                    try {
                        var temperature = Heat4J.retrieveTemperature(rooms.get(i));
                        System.out.println(i);
                        System.out.println("Temperature in room " + rooms.get(i) + " : " + temperature);
                        iotHandler.put(Thread.currentThread(), temperature);

                    } catch (InterruptedException e) {
                       throw new AssertionError(e);
                    }

                }
            });
            thread.start();
            threads.add(thread);
        });

        /* for (String room : rooms) {
            var temperature = Heat4J.retrieveTemperature(room);

            temperatures.add(temperature);
        }*/

		/*
		  You can use the class to get the average temperature in all rooms.

        var value = temperatures.stream().mapToInt(Integer::intValue).average();
        if(value.isEmpty()) {
            System.out.println("Failed to get the temperature");
        } else {
            System.out.println(value.getAsDouble());
        }*/

    }
}