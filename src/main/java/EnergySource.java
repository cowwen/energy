import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EnergySource{

	private final long MAXLEVEL = 1000000;
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
	public static void main(String[] args) throws InterruptedException, ExecutionException {
        //System.out.println("Ready come to lstest Energy.");

        List<Callable<Object>> task = new ArrayList<Callable<Object>>();
        Callable<Object> run = new Callable<Object>(){
            @Override
            public Object call() throws InterruptedException{
        			/*try{
						Thread.sleep((10 - factor) * 100);
					}catch(InterruptedException ex){
						ex.printStackTrace();
					}*/
                for (int ii = 0; ii < 5; ii++) {
                    energy.useEnergy(1);
                }
                return null;
            }
        };
        final long startTime = System.nanoTime();
        for(int i = 0; i < 100000; i++){
        	task.add(run);
        }
        List<Future<?>> results = new ArrayList<Future<?>>();

        ExecutorService service = Executors.newFixedThreadPool(10);
        service.invokeAll(task);
        //System.out.println("Ready stop the energy source.");
        energy.stopEnergySource();

        service.shutdown();
        for (Future<?> f : results) {
            f.get();
        }

        final long endTime = System.nanoTime();

        final double runningTime = (endTime - startTime) / 1.0e9;
        System.out.println("End of the test. energy available : " +
                energy.getUnitsAvailable() + " stop status : "+
                energy.keepRunning + " running time : " + runningTime);
    }
}