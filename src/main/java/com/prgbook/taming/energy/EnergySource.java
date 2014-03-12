package com.prgbook.taming.energy;

public class EnergySource{
	private final long MAXLEVEL = 100;
	private long level = MAXLEVEL;
	private boolean keepRunning = true;

	public EnergySource(){
		new Thread(new Runnable(){
			public void run(){
				replenish();
			}
		}).start();
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

	public boolean stopEnergySource(){
		return false;
	}

	public void replenish(){
		while(keepRunning){
			if(level < MAXLEVEL) level++;

			try{
				Thread.sleep(1000);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}
}