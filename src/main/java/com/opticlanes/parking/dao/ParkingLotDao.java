package com.opticlanes.parking.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.netflix.astyanax.ExceptionCallback;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.serializers.UUIDSerializer;
import com.netflix.astyanax.util.RangeBuilder;
import com.opticlanes.parking.config.Configuration;
import com.opticlanes.parking.database.DBConnection;
import com.opticlanes.parking.model.ParkingLot;
import com.opticlanes.parking.utility.GeoProximitySearch;

public class ParkingLotDao 
{
    private static final Logger logger =  Logger.getLogger(ParkingLotDao.class);
    private static final String PARKING_LOT_TABLE = "parkinglot";
    private static final String PARKING_AVAILIBILTY_TABLE = "parkingavailability";

    private static final ColumnFamily<UUID, String> KEY_FAMILY = new ColumnFamily<UUID, String>(PARKING_LOT_TABLE,
    		UUIDSerializer.get(), StringSerializer.get());
    
    private static final ColumnFamily<UUID, String> PARKING_AVAILIBILITY_FAMILY = new ColumnFamily<UUID, String>(PARKING_AVAILIBILTY_TABLE,
    		UUIDSerializer.get(), StringSerializer.get());
    
    public static final ColumnFamily<Long, String> CF_COUNTER1 =
    	    new ColumnFamily<Long, String>(
    	    	PARKING_AVAILIBILTY_TABLE,
    	        LongSerializer.get(),
    	        StringSerializer.get());
    
    //TODO:  in this first cut of the application the response to a request will always be the same, so this
    //       may as well be a singleton
    private static List<ParkingLot> parkingResponse;
    
    private List<ParkingLot> getParkingLots(double latitude, double longitude)
    {
    	List<ParkingLot> parkingLotList = new ArrayList<ParkingLot>();
    	boolean useProximitySearch = Configuration.getInstance().isProximitySearch();
    	logger.debug("useProximitySearch is " + useProximitySearch);
    	
    	double maxMiles = Configuration.getInstance().getProximitySearchDistance();
    	logger.debug("maxMiles to search for is " + maxMiles);
    	
    	Rows<UUID, String> rows = null;
    	try 
    	{
    	    rows = DBConnection.getKeyspace().prepareQuery(KEY_FAMILY)
    	        .getAllRows()
    	        .withColumnRange(new RangeBuilder().build())
    	        .setExceptionCallback(new ExceptionCallback() {
    	             public boolean onException(ConnectionException e) {
    	                 try {
    	                     Thread.sleep(1000);
    	                 } catch (InterruptedException e1) {
    	                 }
    	                 return true;
    	             }})
    	        .execute().getResult();
    	} 
    	catch (ConnectionException e) 
    	{
    		logger.error("Error retrieving ParkingLot rows", e);
    	}
    	
		for (Row<UUID, String> row : rows) 
		{
					
			ColumnList<String> columns = row.getColumns();

			double plLatitude = columns.getDoubleValue("lotlatitude", null);
			double plLongitude = columns.getDoubleValue("lotlongitude", null);
			
			if (!useProximitySearch || GeoProximitySearch.findDistance(latitude, longitude, plLatitude, plLongitude) < maxMiles)
			{	
				ParkingLot parkingLot = new ParkingLot();
	
				parkingLot.setParkingID(row.getKey());
				parkingLot.setName(columns.getStringValue("name", null));
				parkingLot.setStreetNO(columns.getStringValue("streetno", null));
				parkingLot.setStreet(columns.getStringValue("street", null));
				parkingLot.setCity(columns.getStringValue("city", null));
				parkingLot.setState(columns.getStringValue("state", null));
				parkingLot.setZip(columns.getIntegerValue("zip", null));
				parkingLot.setLotLatitude(columns.getDoubleValue("lotlatitude", null));
				parkingLot.setLotLongitude(columns.getDoubleValue("lotlongitude", null));
				parkingLot.setCapacity(columns.getIntegerValue("parkingcapacity", null));
				
				// Get current availibility from the parkingAvailability table
				try 
				{
					Map<String, Object> currentParking = getParkingLotAvailibilty(row.getKey());
					Long occupied = (Long) currentParking.get("occupied");
					
					Long capacity = parkingLot.getCapacity().longValue();
					occupied = occupied < 0L? 0L: occupied > capacity? capacity: occupied; 
					parkingLot.setOccupied(occupied);
					
					Long empty = (Long) currentParking.get("empty");
					empty = empty < 0L? 0L: empty > capacity? capacity: empty; 
					parkingLot.setEmpty(empty);
					
					parkingLot.setVersion((Long)  currentParking.get("version"));
				} 
				catch (ConnectionException e) 
				{
					logger.error("Could not get current Parking availibility for parking Lot Id " + row.getKey(), e);
				}
	
				// set the amenities field
				parkingLot.setAmenities(columns.getStringValue("amenities", "").split("\\^"));
				parkingLotList.add(parkingLot);
			}
		}
		return parkingLotList;
    }
    
    /**
     * No Longer a Singleton!
     * 
     * @return current list of Parking Lots.
     * 
     */
    public List<ParkingLot> getParkingLotReponse(double latitude, double longitude)
    {
//    	if (parkingResponse == null)
//    	{
//    		parkingResponse = this.getParkingLots();
//    	}
//    	return parkingResponse;
    
    	logger.info("Returning list of Parking Lots");
    	return this.getParkingLots(latitude, longitude);
    }

    //Todo: check for empty result
    public Map<String, Object> getParkingLotDetail(String rowKey) throws ConnectionException, IllegalArgumentException
    {
    	UUID rowKeyUUID = UUID.fromString(rowKey);
        OperationResult<ColumnList<String>> result = DBConnection.getKeyspace().prepareQuery(KEY_FAMILY).getKey(rowKeyUUID)
                .execute();
        ColumnList<String> columns = result.getResult();
        logger.debug("Read child [" + rowKey + "]");
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        
        returnMap.put("parkingID", rowKeyUUID);
        returnMap.put("name", columns.getStringValue("name", null));
		returnMap.put("streetno", columns.getStringValue("streetno", null));
		returnMap.put("street", columns.getStringValue("street", null));
		returnMap.put("city", columns.getStringValue("city", null));
		returnMap.put("state", columns.getStringValue("state", null));
		Integer zip = columns.getIntegerValue("zip", new Integer(0));
		returnMap.put("zip", zip);
		returnMap.put("lotlatitude", columns.getDoubleValue("lotlatitude", null));
		returnMap.put("lotlongitude", columns.getDoubleValue("lotlongitude", null));
		
		// Get current availibility from the parkingAvailability table
		returnMap.putAll(getParkingLotAvailibilty(rowKey));
        
        return returnMap;
    }

    private Map<String, Object> getParkingLotAvailibilty(UUID parkingId) throws ConnectionException
    {
    	Map<String, Object> returnMap = new HashMap<String, Object>();
        OperationResult<ColumnList<String>> result = DBConnection.getKeyspace().prepareQuery(PARKING_AVAILIBILITY_FAMILY).getKey(parkingId).execute();
        ColumnList<String> columns = result.getResult();
    	
        returnMap.put("parkingID", parkingId);
        returnMap.put("version", columns.getLongValue("version", null));
        returnMap.put("occupied", columns.getLongValue("occupied", null));
        returnMap.put("empty", columns.getLongValue("empty", null));
        
    	return returnMap;
    }

    /**
     *  
     * CREATE TABLE  parkingAvailability ( parkingID  UUID, version COUNTER, occupied  COUNTER, empty COUNTER, PRIMARY KEY (parkingID) ) WITH COMPACT STORAGE;
     * @return
     * @throws ConnectionException 
     */
    public Map<String, Object> getParkingLotAvailibilty(String parkingId) throws ConnectionException
    {
    	return getParkingLotAvailibilty(UUID.fromString(parkingId)); 
    }

}
