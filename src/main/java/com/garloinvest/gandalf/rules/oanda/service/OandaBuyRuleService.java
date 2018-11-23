package com.garloinvest.gandalf.rules.oanda.service;

public interface OandaBuyRuleService {

	public boolean buySignalCandlestick();
	public boolean isAnyOpenPosition();
	public boolean buySignalPriceSpreadRange();
}
