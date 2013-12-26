package com.opticlanes.parking.utility;

import org.apache.log4j.Logger;

/**
 * Implementation of this javascript
 * 
 * 	This script is pretty basic, but if you use it, please let me know.  Thanks!
 *	Andrew Hedges, andrew(at)hedges(dot)name
 *
	<script type="text/javascript">
	
	var Rm = 3961; // mean radius of the earth (miles) at 39 degrees from the equator
	var Rk = 6373; // mean radius of the earth (km) at 39 degrees from the equator
		
	// main function 
	function findDistance(frm) {
		var t1, n1, t2, n2, lat1, lon1, lat2, lon2, dlat, dlon, a, c, dm, dk, mi, km;
		
		// get values for lat1, lon1, lat2, and lon2
		t1 = frm.lat1.value;
		n1 = frm.lon1.value;
		t2 = frm.lat2.value;
		n2 = frm.lon2.value;
		
		// convert coordinates to radians
		lat1 = deg2rad(t1);
		lon1 = deg2rad(n1);
		lat2 = deg2rad(t2);
		lon2 = deg2rad(n2);
		
		// find the differences between the coordinates
		dlat = lat2 - lat1;
		dlon = lon2 - lon1;
		
		// here's the heavy lifting
		a  = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
		c  = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a)); // great circle distance in radians
		dm = c * Rm; // great circle distance in miles
		dk = c * Rk; // great circle distance in km
		
		// round the results down to the nearest 1/1000
		mi = round(dm);
		km = round(dk);
		
		// display the result
		frm.mi.value = mi;
		frm.km.value = km;
	}
	
	
	// convert degrees to radians
	function deg2rad(deg) {
		rad = deg * Math.PI/180; // radians = degrees * pi/180
		return rad;
	}
	
	
	// round to the nearest 1/1000
	function round(x) {
		return Math.round( x * 1000) / 1000;
	}
	
	</script>
 * @author admin
 *
 */
public class GeoProximitySearch 
{
	static final Logger logger = Logger.getLogger(GeoProximitySearch.class);
	
	static final Integer Rm = 3961; // mean radius of the earth (miles) at 39 degrees from the equator
	static final Integer Rk = 6373; // mean radius of the earth (km) at 39 degrees from the equator

	// round to the nearest 1/1000
	private static double round(double x) 
	{
		return Math.round(x * 1000) / 1000.0;
	}

	// convert degrees to radians
	private static double deg2rad(double deg) 
	{
		return deg * Math.PI/180; // radians = degrees * pi/180
	}

	// main function 
	public static double findDistance(double lat1, double lon1, double lat2, double lon2) 
	{
		// convert coordinates to radians
		double lat1Rad = deg2rad(lat1);
		double lon1Rad = deg2rad(lon1);
		double lat2Rad = deg2rad(lat2);
		double lon2Rad = deg2rad(lon2);
		
		// find the differences between the coordinates
		double dlat = lat2Rad - lat1Rad;
		double dlon = lon2Rad - lon1Rad;
		
		// here's the heavy lifting
		// a  = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
		double a  = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon/2),2);
		
		double c  = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a)); // great circle distance in radians

		double dm = c * Rm; // great circle distance in miles	
		
//		double dk = c * Rk; // great circle distance in km
		
		// round the results down to the nearest 1/1000
		double mi = round(dm);
//		double km = round(dk);
		
		// return the result
		logger.debug("calculated distance as " + mi);
		
		return mi;
	}

	/**
	 * The default values from the original web page
	 * 
	 * @return should return exactly the same answer as the javascript version
	 */
	@SuppressWarnings("unused")
	private static double doit()
	{
		return findDistance(38.898556, -77.037852, 38.897147, -77.043934);
	}
}
