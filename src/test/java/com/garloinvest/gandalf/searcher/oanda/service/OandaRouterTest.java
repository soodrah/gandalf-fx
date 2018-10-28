package com.garloinvest.gandalf.searcher.oanda.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.garloinvest.gandalf.config.GandalfFXTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=GandalfFXTestConfiguration.class)
public class OandaRouterTest {
	private static final Logger LOG = LoggerFactory.getLogger(OandaRouterTest.class);
	
	@Autowired
	private OandaRouter router;

	@Test
	public void testNullDataCandlestickData() {
		router.readOandaInstrumentCandlestickPerMinute("EUR_USD");
	}
}
