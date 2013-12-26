package com.opticlanes.parking.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.opticlanes.parking.controller.ParkingLotAvailibilityController;
import com.opticlanes.parking.controller.ParkingLotDetailController;
import com.opticlanes.parking.controller.ParkingLotProximityListController;
import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.util.Environment;

public class Configuration extends Environment
{
	private static Logger logger = Logger.getLogger(Configuration.class);
	
	private static final String NAME_PROPERTY = "name";
	private static final String PORT_PROPERTY = "port";
	private static final String DEFAULT_FORMAT_PROPERTY = "defaultFormat";
	private String CASSANDRA_UID;
	private String CASSANDRA_PWD;
	private int port = 80;
	private String name;
	private String defaultFormat;
    private boolean proximitySearch = false;
    private double proxmitySearchDistance = 0.0;
	
	private ParkingLotProximityListController parkingLotProximityListController = new ParkingLotProximityListController();
	private ParkingLotDetailController parkingLotDetailController = new ParkingLotDetailController();
	private ParkingLotAvailibilityController parkingLotAvailibilityController = new ParkingLotAvailibilityController();
	
	private static Configuration instance = null;
	
	@Override
	protected void fillValues(Properties p)
	{
		this.name = p.getProperty(NAME_PROPERTY, RestExpress.DEFAULT_NAME);
		this.port = Integer.parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(RestExpress.DEFAULT_PORT)));
		this.defaultFormat = p.getProperty(DEFAULT_FORMAT_PROPERTY, Format.JSON);
		this.CASSANDRA_UID = p.getProperty("cassandraUID");
		this.CASSANDRA_PWD = p.getProperty("cassandraPWD");
		this.proximitySearch =  Boolean.valueOf(p.getProperty("proximitySearch"));
		this.proxmitySearchDistance = Double.valueOf(p.getProperty("proximitySearchDistance"));
	}

	/**
	 * I can't make the constructor private for the singleton as this is used by the REST implementation.
	 * @return
	 */
	public static Configuration getInstance()
	{
		if (instance == null)
		{
			try 
			{
				instance = from("dev", Configuration.class);
			} 
			catch (IOException e) 
			{
				logger.fatal("Could not load instance", e);
				System.exit(1);
			}
		}
		return instance;
	}
	
	public String getDefaultFormat()
	{
		return defaultFormat;
	}

	public int getPort()
	{
		return port;
	}

	public String getName()
	{
		return name;
	}
	
	public ParkingLotProximityListController getParkingLotProximityListController()
	{
		return parkingLotProximityListController;
	}

	public ParkingLotDetailController getParkingLotDetailController() 
	{
		return parkingLotDetailController;
	}

	public String getCASSANDRA_UID() {
		return CASSANDRA_UID;
	}

	public String getCASSANDRA_PWD() {
		return CASSANDRA_PWD;
	}

	public ParkingLotAvailibilityController getParkingLotAvailibilityController() {
		return parkingLotAvailibilityController;
	}

	public void setParkingLotAvailibilityController(
			ParkingLotAvailibilityController parkingLotAvailibilityController) {
		this.parkingLotAvailibilityController = parkingLotAvailibilityController;
	}
	
	/**
	 * @return the useProxmitySearch
	 */
	public boolean isProximitySearch() 
	{
		return proximitySearch;
	}

	/**
	 * @return the proxmitySearchDistance
	 */
	public double getProximitySearchDistance() 
	{
		return proxmitySearchDistance;
	}
}
