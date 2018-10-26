package com.garloinvest.gandalf.reporter.oanda.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
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
public class ReporterCSVTest {
	private static final Logger LOG = LoggerFactory.getLogger(ReporterCSVTest.class);
	private String prevTime;
	private BigDecimal prevOpen;
	private BigDecimal prevClose;
	private String lastTime;
	private BigDecimal lastOpen;
	private BigDecimal lastClose;
	
	
	@Autowired
	private ReporterCSV reporter;
	
	@Before
	public void init() {
		prevTime = "2018-10-25T17:39:03.700";
		prevOpen = new BigDecimal("1.13748");
		prevClose = new BigDecimal("1.13726");
		lastTime = "2018-10-25T17:45:00.439";
		lastOpen = new BigDecimal("1.13747");
		lastClose = new BigDecimal("1.13732");
	}
	
	@Test
	public void testReadAndIncreaseLineFromConfigFile() throws InterruptedException {
		for(int i = 0; i < 1000; i++) {
			reporter.savedCandleStickBUYSignal(prevTime, prevOpen, prevClose, lastTime, lastOpen, lastClose);
			Thread.sleep(1000);
		}	
	}
	
}
