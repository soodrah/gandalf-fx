package com.garloinvest.gandalf.reporter.oanda.service;

import java.math.BigDecimal;

public interface ReporterCSV {

	public void savedCandleStickBUYSignal(String prevTime, BigDecimal prevOpen, BigDecimal prevClose, 
			String lastTime, BigDecimal lastOpen, BigDecimal lastClose, 
			String currentTime, BigDecimal currentOpen, BigDecimal currentClose, String rule);

	public void storeRejectCandleData(String prevTime, BigDecimal prevOpen, BigDecimal prevClose, 
			String lastTime,BigDecimal lastOpen, BigDecimal lastClose,
			String currentTime, BigDecimal currentOpen, BigDecimal currentClose, String rule);
	
}
