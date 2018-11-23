package com.garloinvest.gandalf.reporter.oanda.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReporterCSV {

	public void storeBuyCandleData(String prevTime, BigDecimal prevOpen, BigDecimal prevClose, 
			String lastTime, BigDecimal lastOpen, BigDecimal lastClose, 
			String currentTime, BigDecimal currentOpen, BigDecimal currentClose, String rule);

	public void storeRejectCandleData(String prevTime, BigDecimal prevOpen, BigDecimal prevClose, 
			String lastTime,BigDecimal lastOpen, BigDecimal lastClose,
			String currentTime, BigDecimal currentOpen, BigDecimal currentClose, String rule);

	public void savedCurrentPrice(String instrument, LocalDateTime time, boolean tradeable, BigDecimal buy, Long liquidityBuy,
			BigDecimal sell, Long liquiditySell);

	public void bookBuyOrder(String instrument, LocalDateTime priceTime, BigDecimal buy, BigDecimal sell, int spread);

	public void savedCurrentCandle(LocalDateTime currentTime, BigDecimal open, BigDecimal close, int currentSize);	
}
