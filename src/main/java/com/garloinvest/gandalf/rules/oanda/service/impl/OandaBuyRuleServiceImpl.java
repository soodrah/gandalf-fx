package com.garloinvest.gandalf.rules.oanda.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.indicator.oanda.service.FXCandle;
import com.garloinvest.gandalf.rules.oanda.service.OandaBuyRuleService;

@Service
public class OandaBuyRuleServiceImpl implements OandaBuyRuleService {
	private static final Logger LOG = LoggerFactory.getLogger(OandaBuyRuleServiceImpl.class);
	
	@Autowired
	private FXCandle candle;

	@Async("readCandlestickData")
	@Scheduled(fixedRate=60000)
	@Override
	public void buySignalCandlestick() {
		boolean candleSignal = candle.compareLastTwoCandlestick("EUR_USD");
		System.out.println("****************Rules Service:\n");
		System.out.println("****************Candle Signal to BUY is:"+candleSignal+"\n");
	}

}
