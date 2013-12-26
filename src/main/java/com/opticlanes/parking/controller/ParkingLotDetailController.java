package com.opticlanes.parking.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opticlanes.parking.config.Constants;
import com.opticlanes.parking.dao.ParkingLotDao;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * Handles REST URI requests for URIs of the form
 * http://dev.opticlanes.com/api/parkingLotDetail/{parkingLotUUID}
 * 
 * example:  http://localhost:8081/api/parkingLots/detail/756716f7-2e54-4715-9f00-91dcbea6cf54
 * 
 * Get the Latitude and Longitude and return a list of parking lots within X miles of the location.
 * Insert a record into the inquiry table with the uniqueID parameter. 
 *  
 * @author warrenweis
 */
public class ParkingLotDetailController
{
    private static final Logger LOG = LoggerFactory.getLogger(ParkingLotDetailController.class);

	/**
	 * Reads a requested object from persistent storage.
	 * 
	 * @param request
	 * @param response
	 * @return Best practice is to return the actual DTO or domain object here.  The Map returned from
	 *         this particular implementation is for demo purposes only.
	 */
	public Map<String, Object> read(Request request, Response response)
	{
		String parkingLotUUID = request.getHeader(Constants.PARKINGLOT_UUID);
		try 
		{
			return new ParkingLotDao().getParkingLotDetail(parkingLotUUID);
		} 
		catch (Exception e) 
		{
			LOG.error("Could not retrieve parking Lot Details for " + parkingLotUUID, e);
			response.setResponseCode(400);
			return null;
		}	
	}
}
