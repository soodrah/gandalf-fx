package com.garloinvest.gandalf.trader.service;

public interface TraderOperation {

	/**
	 * This method will look up for this confirmations:
	 * 	1. If it's a working day
	 * 	2. If it's Friday after 15:00
	 * 	3. If there is any position open
	 * 	4. Start looking for a BUY/SELL signal
	 */
	public void startOp();
}
