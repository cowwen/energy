package com.prgbook.taming.energy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EnergySource{
    private static Logger logger =
            LoggerFactory.getLogger(EnergySource.class);

	private final long MAXLEVEL = 100;
	private long level = MAXLEVEL;
	private boolean keepRunning = true;
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

	public synchronized long getUnitsAvailable(){
		return level;
	}

	public synchronized boolean useEnergy(final long units){
		if(units > 0 && level >= units){
			level -= units;
            logger.info("Used units : " + units + " level : " + level);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
		}
		return false;
	}

	public synchronized boolean stopEnergySource(){
        logger.debug("Change the Status.");
        keepRunning = false;
        replenishTask.cancel(false);
        return keepRunning;
	}

	public synchronized void replenish(){
		while(keepRunning){
            logger.debug("Ready add level");
			if(level < MAXLEVEL) level++;

			try{
				Thread.sleep(1000);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}
}