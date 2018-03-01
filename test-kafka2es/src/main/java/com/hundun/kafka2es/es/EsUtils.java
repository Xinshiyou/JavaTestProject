package com.hundun.kafka2es.es;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.hundun.common.utils.PropertiesUtil;
import com.hundun.kafka2es.util.ConstantUtil;

public class EsUtils {

	private static final Logger logger = Logger.getLogger(EsUtils.class);
	private static TransportClient client = null;

	static {
		if (client == null) {
			client = new PreBuiltTransportClient(Settings.EMPTY);
		}

		String path = System.getProperty("user.dir") + "/conf/" + ConstantUtil.ES_CONF_FILE;
		String orgHosts = "";
		try {
			orgHosts = PropertiesUtil.getProperty(path, "es.hosts");
		} catch (IOException e1) {
			logger.error(e1.getMessage(), e1);
		}
		String[] hosts = orgHosts.split("\\,");
		if (hosts.length < 1) {
			logger.error("Error ElasticSearch hosts");
		} else {
			
			for (Object object : hosts) {
				try {
					client.addTransportAddress(
							new TransportAddress(InetAddress.getByName(object.toString().split(":")[0]),
									Integer.parseInt(object.toString().split(":")[1])));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public static void write2Es(String index, String type, List<Map<String, Object>> dataSets) {

		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Map<String, Object> dataSet : dataSets) {
			bulkRequest.add(client.prepareIndex(index, type).setSource(dataSet));
		}

		bulkRequest.execute().actionGet();
		// if (client != null) {
		// client.close();
		// }
	}

	public static void close() {
		if (client != null) {
			client.close();
		}
	}
}