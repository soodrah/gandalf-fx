package com.garloinvest.gandalf.trader.service;

public interface TraderSchedule {

	/**
	 * This method will verify two dates:
	 * 	StartDate -> Market is Open -> True
	 * 		parameter: Day of Week -> Sunday
	 * 				   Hour: 20:00 EST
	 * 	ShutDownDate -> Market is Close -> False
	 * 		parameter: Day of Week -> Friday
	 * 				   Hour: 17:00 EST
	 * This call will run every second. 
	 * In the Future, it will have an additional feature for Holidays** 
	 * @return
	 */
	public boolean onOff();
	
	/**
	 * This method will be executed if there is any position open on the last day of the week,
	 * to avoid big gap in the price when the Market open the next week.
	 * @return
	 */
	public boolean closeTrade();
	
	/**
	 * This method will trigger the rules service for those service who needs to run every minute.
	 * The output will be a true/false signal to proceed on place an order.
	 * @return
	 */
	public boolean buyerSignalPerMinute();
}
