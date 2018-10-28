package com.garloinvest.gandalf.indicator.oanda.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.garloinvest.gandalf.config.GandalfFXTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=GandalfFXTestConfiguration.class)
public class FXCandleTest {

	@Autowired
	private FXCandle fxCandle;
	
	@Test
	public void checkNullDataFromRouterService() {
		fxCandle.compareLastTwoCandlestick("EUR_USD");
	}
}
