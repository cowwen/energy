package com.prgbook.taming.energy;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TestEnergySource{
	private static Logger logger =
		LoggerFactory.getLogger(TestEnergySource.class);


	@Test
	public void testRunningEnergy() throws InterruptedException{
		logger.debug("Ready come to test Energy.");
        final EnergySource energy = EnergySource.create();
        List<Callable<Object>> task = new ArrayList<Callable<Object>>();

        for(int i = 0; i < 10; i++) {
            task.add(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (int j = 0; j < 7; j++) {
                        energy.useEnergy(1);
                    }
                    return null;
                }
            });
        }
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.invokeAll(task);
        logger.debug("Energy level at end: " + energy.getUnitsAvailable());
		energy.stopEnergySource();
		logger.debug("End of the test.");
        Thread.sleep(1000000);
	}

    private static boolean done = false;
    @Test
    public void testRaceCondition() throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.debug("Ready enter the loop");
                while (!done) {
                    logger.debug("in the loop.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                logger.debug("Exit the loop.");
            }
        }).start();
        Thread.sleep(100);
        //done = true;
        logger.debug("change the done.");
    }

    private static Map<String, String> testMap = new HashMap<String, String>();

    @Test
    public void testConcurrentModification() throws InterruptedException, ExecutionException {
        List<Callable<Object>> task = new ArrayList<Callable<Object>>();

        for(int i = 0; i < 10; i++) {
            task.add(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (int j = 0; j < 7; j++) {
                        testMap.put("j" + j + " " + Thread.currentThread().getName(), j + "");
                        Thread.sleep(1);
                    }
                    return null;
                }
            });
        }

        for(int i = 0; i < 10; i++) {
            task.add(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (String key : testMap.keySet()) {
                        key = key + key;
                    }
                    return null;
                }
            });
        }

        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Future<Object>> list = service.invokeAll(task);

        service.shutdown();

        for (Future<Object> obj : list) {
            obj.get();
        }

        for (String key : testMap.keySet()) {
            logger.info(key + " " + testMap.get(key) + ".");
        }

    }
}