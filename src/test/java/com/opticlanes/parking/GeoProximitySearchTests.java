package com.opticlanes.parking;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.opticlanes.parking.utility.GeoProximitySearch;


public class GeoProximitySearchTests 
{
	Logger logger = Logger.getLogger(GeoProximitySearchTests.class);

	@Test
    public void testDistances()
    {
			GeoProximitySearch gst = new GeoProximitySearch();
			
			double myHomeLat = 38.808348F;
			double myHomeLon = -121.32453199999998F;
			
			double[][] testAddresses = {
										{38.808348F, -121.32453199999998F}, 
										{38.779882F, -121.332404F}, 
										{38.779882F, -121.332404F}, 
										{38.7894F, -121.358563F},
										{38.800586F, -121.293559F},
										{38.783204F, -121.265012F},
										{38.79677, -121.29172},
										{38.78367, -121.268385},
										{38.7408, -121.2499}
									   };
			
			for (double[] values: testAddresses)
			{
				System.err.format("%.3f miles%n", gst.findDistance(myHomeLat, myHomeLon, values[0], values[1]));
			}
    }
}
