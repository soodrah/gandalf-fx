package com.garloinvest.gandalf.searcher.oanda.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;

@Service
public class OandaConnectionFXPractice {
	private static final Logger LOG = LoggerFactory.getLogger(OandaConnectionFXPractice.class);

    @Autowired
    private Environment environment;

    public Context getConnectionFXPractice() {
        LOG.info("#######   OANDA FX-Practice ~ Establishing Connection    #######");
        return new ContextBuilder(environment.getProperty("oanda.fxpracticeapi.domain"))
                .setToken(environment.getProperty("oanda.fxTradePractice.token"))
                .setApplication("Gandalf-FX")
                .build();
    }
	
}
