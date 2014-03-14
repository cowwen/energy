import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EnergySource{

	private final long MAXLEVEL = 100;
	private long level = MAXLEVEL;
	private boolean keepRunning = true;

	private EnergySource(){}

	private void init(){
		new Thread(new Runnable(){
			public void run(){
				replenish();
				System.out.println("End of the instance thread.");
			}
		}).start();
	}

	public static EnergySource create(){
		EnergySource energy = new EnergySource();
		energy.init();
		return energy;
	}

	public long getUnitsAvailable(){
		return level;
	}

	public boolean useEnergy(final long units){
		if(units > 0 && level >= units){
			level -= units;
            System.out.println("level : " + level);
            /*
            try{
				Thread.sleep(1000);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
			*/
			return true;
		}
		return false;
	}

	public boolean stopEnergySource(){
        //System.out.println("Change the Status.");
        keepRunning = false;
		return keepRunning;
	}

	public void replenish(){
		//System.out.println("keepRunning : " + keepRunning);
		while(keepRunning){
            //System.out.println("Ready add level");
			if(level < MAXLEVEL) level++;
			/*
			try{
				Thread.sleep(1000);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
			*/
			
		}
	}

	private static final EnergySource energy = create();
	public static void main(String[] args) throws InterruptedException{
        //System.out.println("Ready come to lstest Energy.");

        List<Callable<Object>> task = new ArrayList<Callable<Object>>();

        for(int i = 0; i < 10; i++){
        	final int factor = i;
        	task.add(new Callable<Object>(){
        		@Override
        		public Object call() throws InterruptedException{
        			try{
						Thread.sleep(factor * 1000);
					}catch(InterruptedException ex){
						ex.printStackTrace();
					}
					for (int ii = 0; ii < 5; ii++) {
						energy.useEnergy(1);
					}
        			return null;
        		}
        	});
        }

        ExecutorService service = Executors.newFixedThreadPool(10);
        service.invokeAll(task);
        //System.out.println("Ready stop the energy source.");
        energy.stopEnergySource();
        System.out.println("End of the test. energy available : " + 
        	energy.getUnitsAvailable() + " stop status : "+
        	energy.keepRunning);
        service.shutdown();
    }
}