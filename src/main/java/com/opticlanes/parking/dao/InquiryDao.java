package com.opticlanes.parking.dao;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.serializers.UUIDSerializer;
import com.netflix.astyanax.util.TimeUUIDUtils;
import com.opticlanes.parking.database.DBConnection;
import com.opticlanes.parking.model.Inquiry;

public class InquiryDao 
{
    private static final Logger LOG = LoggerFactory.getLogger(InquiryDao.class);
    public static final String TABLE_NAME = "inquiry";

    private static final ColumnFamily<UUID, String> KEY_FAMILY = new ColumnFamily<UUID, String>(TABLE_NAME,
    		UUIDSerializer.get(), StringSerializer.get());
	
	public void insertInquiry(Inquiry inquiry) throws ConnectionException
	{
		if (inquiry == null)
		{
			LOG.error("Inquiry is null.  Cannot insert");
			return;
		}
		
		UUID rowKey = UUID.randomUUID();
		UUID inquiryTime = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
		
        MutationBatch mutation = DBConnection.getKeyspace().prepareMutationBatch();
        
        mutation.withRow(KEY_FAMILY, rowKey).putColumn("macid", inquiry.getMacId(), null);
        mutation.withRow(KEY_FAMILY, rowKey).putColumn("gpslatitude", inquiry.getGpsLatitude(), null);
        mutation.withRow(KEY_FAMILY, rowKey).putColumn("gpslongtitude", inquiry.getGpsLongtitude(), null);
        mutation.withRow(KEY_FAMILY, rowKey).putColumn("compassorient", inquiry.getCompassOrient(), null);
        mutation.withRow(KEY_FAMILY, rowKey).putColumn("ostype", inquiry.getOsType(), null);
        mutation.withRow(KEY_FAMILY, rowKey).putColumn("osver", inquiry.getOsVer(), null);
        mutation.withRow(KEY_FAMILY, rowKey).putColumn("inquirytime", inquiryTime, null);
        
		mutation.execute();
        LOG.debug("Inserted Inquiry [" + rowKey + "]");
	}
	
	public void insertInquiryFromString(String gsonString) throws JsonSyntaxException, ConnectionException
	{
		LOG.debug("received request: " + gsonString);
		this.insertInquiry(new Gson().fromJson(gsonString, Inquiry.class));
	}
}
