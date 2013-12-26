package com.opticlanes.parking.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.opticlanes.parking.config.Configuration;

public class DBConnection 
{
	private static final Logger LOG = LoggerFactory.getLogger(DBConnection.class);
    private static Keyspace keyspace;
	    
    //TODO:  These static variables should be replaced by a properties file
    private static final String HOST = "localhost";
    private static final String PORT = "9160";
    private static final String CLUSTER = "Test Cluster";
    private static final String KEYSPACE = "rpis_opticlanes";

	    
    private static AstyanaxContext<Keyspace> astyanaxContext;

    private  static AstyanaxContext<Keyspace> getContext() 
    {
    	if (astyanaxContext == null)
    	{	
    		try 
    		{
    			LOG.debug("Attempting connection with id = " + Configuration.getInstance().getCASSANDRA_UID() + ", pwd = " + Configuration.getInstance().getCASSANDRA_PWD());
    
    			astyanaxContext = new AstyanaxContext.Builder()
    				.forCluster(CLUSTER)
    				.forKeyspace(KEYSPACE)
    				.withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.NONE))
   					.withConnectionPoolConfiguration(
                           new ConnectionPoolConfigurationImpl("MyConnectionPool")
                           .setMaxConnsPerHost(1)
                           .setSeeds(HOST + ":" + PORT).setAuthenticationCredentials(new SimpleAuthenticationCredentials(Configuration.getInstance().getCASSANDRA_UID(), Configuration.getInstance().getCASSANDRA_PWD())))
                           .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                    .buildKeyspace(ThriftFamilyFactory.getInstance());
    			astyanaxContext.start();
    			
    			LOG.info("Started Connection to database");
    		} 
    		catch (Throwable e) 
    		{
    			LOG.error("Could not connect to cassandra.", e);
    			System.exit(-1);
    		}
    	}
    	return astyanaxContext;
    }

    public static void cleanup() {
System.err.println("Shutting Down.");   
		if (astyanaxContext != null)
		{	
			astyanaxContext.shutdown();
		}	
    }

    /**
     * Server is responsible for shutting down the astyanax Context when shutting down.
     * @return
     */
	public static Keyspace getKeyspace() 
	{
		if (keyspace == null)
		{
			keyspace = getContext().getEntity();
			try 
			{
    			// test the connection
				keyspace.describeKeyspace();
			} 
			catch (ConnectionException e) 
			{
    			LOG.error("Could not get keyspace + " + KEYSPACE, e);
    			System.exit(-1);
			}
		}
		return keyspace;
	}
}
