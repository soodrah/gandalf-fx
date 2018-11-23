package com.garloinvest.gandalf.indicator.oanda.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.indicator.oanda.service.FXCandle;
import com.garloinvest.gandalf.reporter.oanda.service.ReporterCSV;
import com.garloinvest.gandalf.searcher.oanda.dto.OandaInstrumentCandlestick;
import com.garloinvest.gandalf.searcher.oanda.service.OandaRouter;

@Service
public class FXCandleImpl implements FXCandle {
	private static final Logger LOG = LoggerFactory.getLogger(FXCandleImpl.class);

	@Autowired
	private OandaRouter router;
	@Autowired
	private ReporterCSV reporterCSV;

	@Override
	public boolean compareLastThreeCandlestick(String instrumentName) {
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
			LocalDateTime currentTime = candleMap.lastKey();
			LocalDateTime lastTime = currentTime.minusMinutes(1);
			LocalDateTime prevTime = currentTime.minusMinutes(2);
			OandaInstrumentCandlestick prevCandle = candleMap.get(prevTime);
			OandaInstrumentCandlestick lastCandle = candleMap.get(lastTime);
			OandaInstrumentCandlestick currentCandle = candleMap.get(currentTime);

			if (!lastCandle.isComplete()) {
				LOG.error("Error: FXCandleImple last Candle is NOT completed");
				break;
			}

			
			//Run three set of patterns base on the Size of each Candle and its Trend
			//This calculation its customize for EUR_USD, for other currency needs to apply different conversion
			int pOpen = prevCandle.getOpen().multiply(BigDecimal.valueOf(100000)).intValue();	 //prevOpen
			int pClose = prevCandle.getClose().multiply(BigDecimal.valueOf(100000)).intValue();	 //prevClose
			int prevSize = pClose - pOpen;
			int lOpen = lastCandle.getOpen().multiply(BigDecimal.valueOf(100000)).intValue();	 //lastOpen
			int lClose = lastCandle.getClose().multiply(BigDecimal.valueOf(100000)).intValue();	 //lastClose
			int lastSize = lClose - lOpen;
			int cOpen = currentCandle.getOpen().multiply(BigDecimal.valueOf(100000)).intValue();  //currentOpen	
			int cClose = currentCandle.getClose().multiply(BigDecimal.valueOf(100000)).intValue();//currentClose
			int currentSize = cClose - cOpen;
			if (prevSize >= 0 && lastSize >= 0 && currentSize >= 0) { //Rule no.1
					LOG.info("****************BUY!!!\n");
					/*reporterCSV.storeBuyCandleData(prevTime.toString(),prevCandle.getOpen(),prevCandle.getClose(),
							lastTime.toString(),lastCandle.getOpen(),lastCandle.getClose(),
							currentTime.toString(),currentCandle.getOpen(),currentCandle.getClose(), "Rule1");*/
					reporterCSV.savedCurrentCandle(currentTime,currentCandle.getOpen(),currentCandle.getClose(),currentSize);
					return true;
			}else if(prevSize < 0) {
				if(lastSize > -prevSize || (lastSize + currentSize) > - prevSize) { //Rule no.2
					LOG.info("****************BUY!!!\n");
					/*reporterCSV.storeBuyCandleData(prevTime.toString(),prevCandle.getOpen(),prevCandle.getClose(),
							lastTime.toString(),lastCandle.getOpen(),lastCandle.getClose(),
							currentTime.toString(),currentCandle.getOpen(),currentCandle.getClose(), "Rule2");*/
					reporterCSV.savedCurrentCandle(currentTime,currentCandle.getOpen(),currentCandle.getClose(),currentSize);
					return true;
				}
			}else if(lastSize < 0 && currentSize >= 0) {
				if(currentSize > - lastSize || (prevSize + currentSize) > - lastSize) { //Rule no.3
					LOG.info("****************BUY!!!\n");
					/*reporterCSV.storeBuyCandleData(prevTime.toString(),prevCandle.getOpen(),prevCandle.getClose(),
							lastTime.toString(),lastCandle.getOpen(),lastCandle.getClose(),
							currentTime.toString(),currentCandle.getOpen(),currentCandle.getClose(), "Rule3");*/
					reporterCSV.savedCurrentCandle(currentTime,currentCandle.getOpen(),currentCandle.getClose(),currentSize);
					return true;
				}
			}else {
				LOG.info("****************NOT SIGNAL FOR BUY :(\n");
				reporterCSV.storeRejectCandleData(prevTime.toString(),prevCandle.getOpen(),prevCandle.getClose(),
						lastTime.toString(),lastCandle.getOpen(),prevCandle.getClose(),
						currentTime.toString(),currentCandle.getOpen(),currentCandle.getClose(), "NONE");
			}
		}

		return false;
	}

}
