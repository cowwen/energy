package com.prgbook.taming.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by cowwen on 14-3-17.
 */
public class MainEnergySource {
    private static final EnergySource energySource = EnergySource.create();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final long start = System.nanoTime();
        List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();

        Callable<Object> task = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                for (int i = 0; i < 7; i++) {
                    energySource.useEnergy(1);
                }
                return null;
            }
        };

        for (int i = 0; i < 10; i++) {
            tasks.add(task);
        }

        ExecutorService service = Executors.newFixedThreadPool(10);

        List<Future<Object>> rsl = service.invokeAll(tasks);

        service.shutdown();

        for (Future f : rsl) {
            f.get();
        }

        energySource.stopEnergySource();

        final long end = System.nanoTime();
        final double running = (end - start) / 1.0e9;
        System.out.println("running time: " + running + " available energy: "
                + energySource.getUnitsAvailable() );
    }
}
