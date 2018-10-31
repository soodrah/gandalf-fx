package com.garloinvest.gandalf.rules.oanda.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.indicator.oanda.service.FXCandle;
import com.garloinvest.gandalf.indicator.oanda.service.FXPosition;
import com.garloinvest.gandalf.rules.oanda.service.OandaBuyRuleService;

@Service
public class OandaBuyRuleServiceImpl implements OandaBuyRuleService {
	private static final Logger LOG = LoggerFactory.getLogger(OandaBuyRuleServiceImpl.class);
	
	@Autowired
	private FXCandle candle;
	@Autowired
	private FXPosition position;

	@Override
	public boolean buySignalCandlestick() {
		LOG.info("OandaBuyRuleServiceImpl: {} Time: {}",Thread.currentThread().getName(),LocalDateTime.now());
		return candle.compareLastTwoCandlestick("EUR_USD");
	}

	@Override
	public boolean isAnyOpenPosition() {
		if(!position.getAllPositions().isEmpty()) {
			//TODO: Analyze each position that is open
			return true;
		}
		return false;
	}

}
