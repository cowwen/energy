import com.prgbook.taming.energy.EnergySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cowwen on 14-3-13.
 */
public class TestMain {
    private static Logger logger =
            LoggerFactory.getLogger(TestMain.class);
    private static final EnergySource energy = EnergySource.create();
    public static void main(String[] args) throws InterruptedException{
        logger.debug("Ready come to test Energy.");

        energy.useEnergy(10);
        energy.useEnergy(20);
        Thread.sleep(5000);
        energy.stopEnergySource();
        Thread.sleep(3000);
        logger.debug("End of the test.");
    }

}
