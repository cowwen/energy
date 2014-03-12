package com.prgbook.taming.energy;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEnergySource{
	public static Logger logger =
		LoggerFactory.getLogger(TestEnergySource.class);
	
	@Test
	public void testRunningEnergy() throws InterruptedException{
		logger.debug("Ready come to test Energy.");
		EnergySource energy = new EnergySource();
		energy.useEnergy(10);
		energy.useEnergy(20);
		Thread.sleep(1000);
		energy.stopEnergySource();
		logger.debug("End of the test.");
	}
}