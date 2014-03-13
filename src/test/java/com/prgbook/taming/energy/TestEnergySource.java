package com.prgbook.taming.energy;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestEnergySource{
	private static Logger logger =
		LoggerFactory.getLogger(TestEnergySource.class);

    private static final EnergySource energy = EnergySource.create();
	@Test
	public void testRunningEnergy() throws InterruptedException{
		logger.debug("Ready come to test Energy.");

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
}