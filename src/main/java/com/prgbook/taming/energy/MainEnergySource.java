package com.prgbook.taming.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by cowwen on 14-3-19.
 */
public class MainEnergySource {
    private static final EnergySource energySource = EnergySource.create();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();

        final long start = System.nanoTime();

        Callable<Object> run = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                for (int i = 0; i < 7; i++) {
                    energySource.useEnergy(1);
                }
                return null;
            }
        };

        for (int i = 0; i < EnergySource.MAXLEVEL * 0.1; i++) {
            tasks.add(run);
        }

        ExecutorService service = Executors.newFixedThreadPool(10);

        List<Future<Object>> rls = service.invokeAll(tasks);

        service.shutdown();

        for (Future<Object> f : rls) {
            f.get();
        }

        energySource.stopEnergySource();

        final long end = System.nanoTime();

        final double time = (end - start) / (1.0e9);

        System.out.println("running time: " + time + " available energy: "
                + energySource.getUnitsAvailable() );

    }
}
