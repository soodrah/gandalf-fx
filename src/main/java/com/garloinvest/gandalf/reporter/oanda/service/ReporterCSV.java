package com.garloinvest.gandalf.reporter.oanda.service;

import java.math.BigDecimal;

public interface ReporterCSV {

	public void savedCandleStickBUYSignal(String localTime, String prevTime, BigDecimal bigDecimal, 
			BigDecimal bigDecimal2, String lastTime, BigDecimal bigDecimal3, BigDecimal bigDecimal4);
}
