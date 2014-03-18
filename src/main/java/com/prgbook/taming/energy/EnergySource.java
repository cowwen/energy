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

	private final long MAX_LEVEL = 1000000;
	private long level = MAX_LEVEL;
    private static final ScheduledExecutorService replenishTimer =
            Executors.newScheduledThreadPool(10);
    private ScheduledFuture<?> replenishTask;


	private boolean keepRunning = true;

	private EnergySource(){ }

    private void init() {
        replenishTask = replenishTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                replenish();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static EnergySource create() {
        final EnergySource energySource = new EnergySource();
        energySource.init();
        return energySource;
    }

	public long getUnitsAvailable(){
		return level;
	}

	public boolean useEnergy(final long units){
		if(units > 0 && level >= units){
			level -= units;
			return true;
		}
		return false;
	}

	public void stopEnergySource() {
        replenishTask.cancel(false);
    }

	public void replenish() {
        if (level < MAX_LEVEL) {
            level++;
        }
    }
}