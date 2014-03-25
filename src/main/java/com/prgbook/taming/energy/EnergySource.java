package com.prgbook.taming.energy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class EnergySource{
    private static Logger logger =
            LoggerFactory.getLogger(EnergySource.class);

	public static final long MAXLEVEL = 10000;
    private final AtomicLong level = new AtomicLong(MAXLEVEL);
    private static final ScheduledExecutorService replenishTimer = Executors.newScheduledThreadPool(10);
    private ScheduledFuture<?> replenishTask;

	private EnergySource(){
	}

    public static EnergySource create() {
        EnergySource energySource = new EnergySource();
        energySource.init();
        return energySource;
    }

    private void init() {
        replenishTask = replenishTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                replenish();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

	public long getUnitsAvailable(){
		return level.get();
	}

	public boolean useEnergy(final long units) {
        boolean flag = false;
        while (!flag) {
            final long currentLevel = level.get();
            if (units > 0 && currentLevel >= units) {
                flag = level.compareAndSet(currentLevel, currentLevel - units);
                logger.info("Used units : " + units + " level : " + level.get() +
                        " flag:" + flag + " currentLevel:" + currentLevel + " getLevel:" + level.get());
            }
        }
        return flag;
    }

	public synchronized void stopEnergySource(){
        logger.debug("Change the Status.");
        replenishTask.cancel(false);
        replenishTimer.shutdown();
	}

    public void replenish() {
        //logger.debug("Ready add level");
        if (level.get() < MAXLEVEL) level.incrementAndGet();
    }
}