package com.opticlanes.parking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.opticlanes.parking.config.Configuration;
import com.opticlanes.parking.config.Constants;
import com.opticlanes.parking.dao.InquiryDao;
import com.opticlanes.parking.dao.ParkingLotDao;
import com.opticlanes.parking.model.Inquiry;
import com.opticlanes.parking.model.ParkingLot;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.route.Route;

/**
 * Handles REST URI requests for URIs of the form
 * http://dev.opticlanes.com/api/parkingLots/{lattitude}/{longitude}/{uniqueID}
 * 
 * Get the Latitude and Longitude and return a list of parking lots within X miles of the location.
 * Insert a record into the inquiry table with the uniqueID parameter. 
 *  
 * @author warrenweis
 */
public class ParkingLotProximityListController
{
	private static Logger logger = Logger.getLogger(ParkingLotProximityListController.class);
			
	/**
	 * Reads a requested object from persistent storage.
	 * 
	 * @param request
	 * @param response
	 * @return Best practice is to return the actual DTO or domain object here.  The Map returned from
	 *         this particular implementation is for demo purposes only.
	 */
	public List<ParkingLot> readAll(Request request, Response response)
	{
		String clientLatitude = request.getHeader(Constants.CLIENT_LATITUDE);
		String clientLongitude  = request.getHeader(Constants.CLIENT_LONGITUDE);
		String clientId = request.getHeader(Constants.CLIENT_ID);
		
		Inquiry inquiry = new Inquiry();
		try 
		{
			inquiry = new Inquiry();
			inquiry.setGpsLatitude(clientLatitude);
			inquiry.setGpsLongtitude(clientLongitude);
			inquiry.setMacId(clientId);
			inquiry.setCompassOrient("");
			inquiry.setOsType("");
			inquiry.setOsVer("");

			new InquiryDao().insertInquiry(inquiry);
		} 
		catch (ConnectionException e) 
		{
			System.err.println("Could not insert inquriy inforation for " + inquiry);
		}
		
		try
		{
			return new ParkingLotDao().getParkingLotReponse(Double.valueOf(clientLatitude), Double.valueOf(clientLongitude));
		}
		catch (Exception e)
		{
			// return an empty list if the response cannot be understood
			logger.warn("Error retrieving parking lot list for latitude, longitude " + clientLatitude + ", " + clientLongitude);
			return null;
		}
	}
}
