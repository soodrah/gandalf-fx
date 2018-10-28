package com.garloinvest.gandalf.searcher.oanda.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.garloinvest.gandalf.searcher.oanda.dto.OandaInstrumentCandlestick;
import com.garloinvest.gandalf.searcher.oanda.dto.OandaInstrumentPrice;

public interface OandaRouter {
	
	 public Map<String, Map<LocalDateTime, OandaInstrumentCandlestick>> readOandaInstrumentCandlestickPerMinute(String instrumentName);
	 public Map<String, OandaInstrumentPrice> readOandaInstrumentPrice(List<String> instruments);

}
