package com.garloinvest.gandalf.indicator.oanda.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.indicator.oanda.service.FXCandle;
import com.garloinvest.gandalf.searcher.oanda.dto.OandaInstrumentCandlestick;
import com.garloinvest.gandalf.searcher.oanda.service.OandaRouter;

@Service
public class FXCandleImpl implements FXCandle {
	private static final Logger LOG = LoggerFactory.getLogger(FXCandleImpl.class);
	
	@Autowired
	private OandaRouter router;

	@Override
	public void readCandlestickData(String instrumentName) {
		Map<String, Map<LocalDateTime, OandaInstrumentCandlestick>> candlestickData = router.readOandaInstrumentCandlestickPerMinute(instrumentName);
		LOG.info("*******************Testing INDICATOR***************");
		LOG.info("\nTime: {}\nCandleStickData size: {}\nThread: {}",LocalDateTime.now(),candlestickData.size(),Thread.currentThread().getName());
	}

}
