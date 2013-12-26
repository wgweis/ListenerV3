package com.opticlanes.parking;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.opticlanes.parking.dao.InquiryDao;
import com.opticlanes.parking.dao.ParkingLotDao;
import com.opticlanes.parking.model.Inquiry;
import com.opticlanes.parking.server.OpticLanesParkingServer;

/*
 * these tests should only be run in the IDE as they depend on the server and cassandra being up.
 * Remove the 's' on the end of the class name if you really want to try and run them outside of the
 * IDE.
 */
public class OpticLanesParkingDaoIDETests {
    private static Logger LOG = LoggerFactory.getLogger(OpticLanesParkingDaoIDETests.class);
    
    // set up an Inquiry object to use for dqo and socket connection tests.
    private static Inquiry inquiry = new Inquiry();
    static
    {
    	inquiry.setCompassOrient("compassOrient");
    	inquiry.setGpsLatitude("gpsLatitude");
    	inquiry.setGpsLongtitude("gpsLongtitude");
    	inquiry.setMacId("macId");
    	inquiry.setOsType("osType");
    	inquiry.setOsVer("osVer");
    }
    
    @Test
    public void testOpticLanesInquiryInsertOld() throws JsonSyntaxException, ConnectionException
    {
        Inquiry inquiry = new Inquiry();
        inquiry.setCompassOrient("compassOrient");
        inquiry.setGpsLatitude("gpsLatitude");
        inquiry.setGpsLongtitude("gpsLongtitude");
        inquiry.setMacId("macId");
        inquiry.setOsType("osType");
        inquiry.setOsVer("osVer");
        new InquiryDao().insertInquiryFromString(new Gson().toJson(inquiry));
    }

    @Test
    public void testOpticLanesInquiryInsert() throws JsonSyntaxException, ConnectionException
    {
    	new InquiryDao().insertInquiryFromString(new Gson().toJson(inquiry));
    	assertTrue(true);
    }
    
    @Test
    public void testParkingLotDao()
    {
    	// First try and get a list for Roseville.  This list should be empty
    	LOG.debug("ParkingLot response: " + new ParkingLotDao().getParkingLotReponse(38.808348F, -121.32453199999998F));
    	
    	// now try something returning a match
    	LOG.debug("ParkingLot response: " + new ParkingLotDao().getParkingLotReponse(37.438831F, -122.141363F));
    	
    }
    
//    @Test
//    public void testParkingRequest() throws IOException 
//    {
//    	Socket kkSocket = null;
//    	try 
//    	{
//            StringBuffer sb = new StringBuffer();
//
//    	    kkSocket = new Socket("localhost", OpticLanesParkingServer.SERVER_PORT);
//            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
//            
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(kkSocket.getInputStream()));
//
//            out.println(new Gson().toJson(inquiry));
//
//            String line = null;
//            while ((line = in.readLine()) != null)
//            {
//            	sb.append(line);
//            }
//            
//            LOG.info("received response: " + sb.toString());
//            
//    	} 
//    	catch (UnknownHostException e) 
//    	{
//    		LOG.error("UnknownHostException for localhost", e);
//    		assertTrue(false);
//    	} 
//    	catch (IOException e) 
//    	{
//    		LOG.error("I/O Exception for localhost", e);
//    		assertTrue(false);
//    	}
//    	finally
//    	{
//    		if (kkSocket == null)
//    		{
//    			// this will throw an uncaught Exception causing the test to fail
//    			kkSocket.close();
//    		}
//    	}
//    }
    
//    /**
//     * If we get here try a few times and see what happens
//     * @throws IOException
//     */
//    @Test
//    public void testSeveralParkingRequests() throws IOException
//    {
//    	for (int i = 0; i < 10; i++)
//    	{
//    		testParkingRequest();
//    		LOG.info("Test " + i + " successful.");
//    	}
//    }
}
