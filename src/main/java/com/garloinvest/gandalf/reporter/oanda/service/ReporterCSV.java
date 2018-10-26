package com.garloinvest.gandalf.reporter.oanda.service;

import java.math.BigDecimal;

public interface ReporterCSV {

	public void savedCandleStickBUYSignal(String prevTime, BigDecimal prevOpen, 
			BigDecimal prevClose, String lastTime, BigDecimal lastOpen, BigDecimal lastClose);
	
}
