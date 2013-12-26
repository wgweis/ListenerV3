package com.opticlanes.parking.server;

import com.opticlanes.parking.database.DBConnection;
import com.strategicgains.restexpress.RestExpress;

public class OpticLanesRestExpressServer extends RestExpress
{
	@Override
	public void shutdown()
	{
		// shut down the database connection
		DBConnection.cleanup();
		
		// do whatever RestExpress wants to do.
		super.shutdown();
	}
}
