package com.opticlanes.parking;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.opticlanes.parking.config.Configuration;

public class ConfigurationTest 
{
	Logger logger = Logger.getLogger(ConfigurationTest.class);
	
	@Test
	public void testConfiguration()
	{
		logger.info(Configuration.getInstance().isProximitySearch());
		logger.info(Configuration.getInstance().getProximitySearchDistance());
	}
}
