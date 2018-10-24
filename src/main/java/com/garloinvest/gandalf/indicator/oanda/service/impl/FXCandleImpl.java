package com.garloinvest.gandalf.indicator.oanda.service.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

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
	public boolean compareLastTwoCandlestick(String instrumentName) {
		Map<String, Map<LocalDateTime, OandaInstrumentCandlestick>> candlestickData = router
				.readOandaInstrumentCandlestickPerMinute(instrumentName);

		if (null == candlestickData || candlestickData.isEmpty()) {
			LOG.error("Candlestick is empty or Null");
			return false;
		}

		LOG.info("Reading Candlestick");

		for (Map.Entry<String, Map<LocalDateTime, OandaInstrumentCandlestick>> entryInstrument : candlestickData
				.entrySet()) {
			TreeMap<LocalDateTime, OandaInstrumentCandlestick> candleMap = (TreeMap<LocalDateTime, OandaInstrumentCandlestick>) entryInstrument
					.getValue();
			LocalDateTime lastTime = candleMap.lastKey();
			LocalDateTime prevTime = lastTime.minusMinutes(1);
			OandaInstrumentCandlestick prevCandle = candleMap.get(prevTime);
			OandaInstrumentCandlestick lastCandle = candleMap.get(lastTime);

			if (!prevCandle.isComplete()) {
				break;
			}

			// Verifying if the prevCandle was up and if the lastCandle is going up respect
			// the prevCandle
			if (prevCandle.getOpen().compareTo(prevCandle.getClose()) < 0) {
				if (lastCandle.getOpen().compareTo(prevCandle.getClose()) >= 0) {
					System.out.println("****************Indicator Service:\n");
					System.out.println("****************Time: "+lastTime.toString()+"\n");
					System.out.println("****************PrevCandle Open:"+prevCandle.getOpen().toString()+"\n");
					System.out.println("****************PrevCandle Close:"+prevCandle.getClose().toString()+"\n");
					System.out.println("****************LastCandle Open:"+lastCandle.getOpen().toString()+"\n");
					System.out.println("****************LastCandle Close:"+prevCandle.getClose().toString()+"\n");
					return true;
				}
			}
		}

		return false;
	}

}
