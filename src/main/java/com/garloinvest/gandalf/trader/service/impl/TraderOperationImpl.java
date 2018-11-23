package com.garloinvest.gandalf.trader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.indicator.oanda.service.FXPrice;
import com.garloinvest.gandalf.rules.oanda.service.OandaBuyRuleService;
import com.garloinvest.gandalf.trader.service.TraderOperation;
import com.garloinvest.gandalf.trader.service.TraderSchedule;

@Service
public class TraderOperationImpl implements TraderOperation {
	private static final Logger LOG = LoggerFactory.getLogger(TraderOperationImpl.class);

	@Autowired
	private TraderSchedule schedule;
	@Autowired
	private OandaBuyRuleService rule;
	@Autowired
	private FXPrice price;

	@Override
	@Scheduled(cron = "0/1 * * * * ?")
	public void startOp() {
		if (schedule.onOff()) { 
			if (schedule.closeTrade()) {
				LOG.info("*******  Closing Positions  ********");
				if (rule.isAnyOpenPosition()) {
					// NONSONARTODO: Close any open position at the best price
				}
			} else if (rule.isAnyOpenPosition()) {
				// NONSONARTODO: Checked for any open position, and if any run the watch-list
			} else if (schedule.buyerSignalPerMinute()) {
				// NONSONARTODO: Execute a place Order
				LOG.info("*******  Executed Place BUY Order  *******");
			}else {
				price.getCurrentPriceAllInstruments();
			}
		}
	}

}
