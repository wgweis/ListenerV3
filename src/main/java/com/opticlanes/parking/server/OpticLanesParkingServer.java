package com.opticlanes.parking.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opticlanes.parking.config.Configuration;
import com.opticlanes.parking.config.Constants;
import com.strategicgains.restexpress.Flags;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import com.strategicgains.restexpress.plugin.cache.CacheControlPlugin;
import com.strategicgains.restexpress.plugin.route.RoutesMetadataPlugin;
import com.strategicgains.restexpress.util.Environment;

public class OpticLanesParkingServer 
{
    private static final Logger LOG = LoggerFactory.getLogger(OpticLanesParkingServer.class);

	public static void main(String[] args) throws Exception
	{
		Configuration config = loadEnvironment(args);
		RestExpress server = new OpticLanesRestExpressServer()
		    .setName(config.getName())
		    .setDefaultFormat(config.getDefaultFormat())
		    .addMessageObserver(new SimpleConsoleLogMessageObserver());

		defineRoutes(config, server);

		new RoutesMetadataPlugin()							// Support discoverability.
			.register(server);

		new CacheControlPlugin()							// Support caching headers.
			.register(server);

		server.bind(config.getPort());
		server.awaitShutdown();
	}

	private static void defineRoutes(Configuration config, RestExpress server)
	{
		// Maps /kickstart uri with required orderId and optional format identifier
		// to the KickStartService.  Accepts only GET, PUT, DELETE HTTP methods.
		// Names this route to allow returning links from read resources in
		// KickStartService methods via call to LinkUtils.asLinks().
		server.uri("/api/parkingLots/{latitude}/{longitude}/{uniqueID}", config.getParkingLotProximityListController())
			.action("readAll", HttpMethod.GET)
			.name(Constants.PROXIMITY_REQUEST_URI)
			.flag(Flags.Cache.DONT_CACHE);					// Expressly deny cache-ability.
		
		// Maps /kickstart uri with required orderId and optional format identifier
		// to the KickStartService.  Accepts only GET, PUT, DELETE HTTP methods.
		// Names this route to allow returning links from read resources in
		// KickStartService methods via call to LinkUtils.asLinks().
		server.uri("/api/parkingLots/detail/{parkingLotUUID}", config.getParkingLotDetailController())
			.method(HttpMethod.GET)
			.name(Constants.PARKINGLOT_DETAIL_URI)
			.flag(Flags.Cache.DONT_CACHE);					// Expressly deny cache-ability.

		// Maps /kickstart uri with required orderId and optional format identifier
		// to the KickStartService.  Accepts only GET, PUT, DELETE HTTP methods.
		// Names this route to allow returning links from read resources in
		// KickStartService methods via call to LinkUtils.asLinks().
		server.uri("/api/parkingLots/availibility/{parkingLotUUID}", config.getParkingLotAvailibilityController())
			.method(HttpMethod.GET)
			.name(Constants.PARKINGLOT_AVAILIBILITY_URI)
			.flag(Flags.Cache.DONT_CACHE);					// Expressly deny cache-ability.
	}

	private static Configuration loadEnvironment(String[] args) throws FileNotFoundException, IOException
    {
	    if (args.length > 0)
		{
			return Environment.from(args[0], Configuration.class);
		}
	    return Environment.fromDefault(Configuration.class);
    }
}