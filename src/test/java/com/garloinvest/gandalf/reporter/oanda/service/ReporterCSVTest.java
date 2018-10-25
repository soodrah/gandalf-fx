package com.garloinvest.gandalf.reporter.oanda.service;

import org.junit.Assert;
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
	
	@Autowired
	private ReporterCSV reporter;
	
	@Test
	public void testReadAndIncreaseLineFromConfigFile() {
		int expectedLine = 0;
		int actualLine = 0;
		for(int i =0; i <= 10;i++) {
			actualLine = reporter.readLines();
			reporter.increaseLines(actualLine);
		}
		actualLine = reporter.readLines();
		reporter.resetLines();
		actualLine = reporter.readLines();
		Assert.assertTrue("Error: The number of Lines are different", expectedLine == actualLine);
	}
	
}
