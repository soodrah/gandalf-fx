package com.garloinvest.gandalf.rules.oanda.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.indicator.oanda.service.FXCandle;
import com.garloinvest.gandalf.indicator.oanda.service.FXPosition;
import com.garloinvest.gandalf.indicator.oanda.service.FXPrice;
import com.garloinvest.gandalf.rules.oanda.service.OandaBuyRuleService;

@Service
public class OandaBuyRuleServiceImpl implements OandaBuyRuleService {
	private static final Logger LOG = LoggerFactory.getLogger(OandaBuyRuleServiceImpl.class);
	
	
	@Autowired
	private FXCandle candle;
	@Autowired
	private FXPosition position;
	@Autowired
	private FXPrice price;

	@Override
	public boolean buySignalCandlestick() {
		LOG.info("OandaBuyRuleServiceImpl: {} Time: {}",Thread.currentThread().getName(),LocalDateTime.now());
		return candle.compareLastThreeCandlestick("EUR_USD");
	}

	@Override
	public boolean isAnyOpenPosition() {
		LOG.info("OandaBuyRuleServiceImpl: {} Time: {}",Thread.currentThread().getName(),LocalDateTime.now());
		if(!position.getAllPositions().isEmpty()) {
			//NONSONARTODO: Analyze each position that is open
			return true;
		}
		return false;
	}

	@Override
	public boolean buySignalPriceSpreadRange() {
		LOG.info("OandaBuyRuleServiceImpl: {} Time: {}",Thread.currentThread().getName(),LocalDateTime.now());
		return price.getCurrentPriceAllInstruments();
	}

}
