package com.garloinvest.gandalf.searcher.oanda.connection;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.constants.GlobalConstants;
import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;

@Service
public class OandaConnectionFXPractice {
	private static final Logger LOG = LoggerFactory.getLogger(OandaConnectionFXPractice.class);

    @Autowired
    private Environment environment;

    public Context getConnectionFXPractice() {
        LOG.info("#######   OANDA FX-Practice ~ Establishing Connection    #######");
        PoolingHttpClientConnectionManager poolConnection = new PoolingHttpClientConnectionManager();
        poolConnection.setDefaultMaxPerRoute(GlobalConstants.CONNECTION_MAX_PER_ROUTE);
        poolConnection.setMaxTotal(GlobalConstants.CONNECTION_MAX_TOTAL);
        
        CloseableHttpClient httpClient = HttpClients.custom()
        		.setConnectionManagerShared(true)
        		.setConnectionManager(poolConnection)
        		.build();
        
        Context ctx = new ContextBuilder(environment.getProperty(GlobalConstants.DOMAIN_FX_PRACTICE))
                .setToken(environment.getProperty(GlobalConstants.TOKEN_FX_PRACTICE))
                .setApplication("Gandalf-FX")
                .setHttpClient(httpClient)
                .build();
        ctx.setHeader("Connection", "Keep-Alive");
        return ctx;
        
    }
	
}
