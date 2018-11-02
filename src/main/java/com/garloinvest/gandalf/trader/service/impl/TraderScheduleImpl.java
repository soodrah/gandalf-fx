package com.garloinvest.gandalf.trader.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.constants.GlobalConstants;
import com.garloinvest.gandalf.rules.oanda.service.OandaBuyRuleService;
import com.garloinvest.gandalf.trader.service.TraderSchedule;

@Service
public class TraderScheduleImpl implements TraderSchedule {
	private static final Logger LOG = LoggerFactory.getLogger(TraderScheduleImpl.class);
	
	@Autowired
	private OandaBuyRuleService rules;
	
	@Override
//	@Scheduled(fixedRate=1000)
	public boolean onOff() {
		
		String dayOfWeek = LocalDateTime.now().getDayOfWeek().toString();
		int hour = LocalDateTime.now().getHour();
		int minute = LocalDateTime.now().getMinute();
		
		if(GlobalConstants.START_DAY.equalsIgnoreCase(dayOfWeek)) {
			if(GlobalConstants.START_HOUR == hour && GlobalConstants.START_MINUT <= minute) {
				LOG.info("*******  GANDALF-FX STARTED  *******");
				LOG.info("\nDay: {}\nHour: {}\nMinute: {}",dayOfWeek,hour,minute);
				return true;
			}else {
				LOG.info("It is : {}, but cannot start yet",dayOfWeek);
				return false;
			}
			
		}else if(GlobalConstants.SHUTDOWN_DAY.equalsIgnoreCase(dayOfWeek)) {
				if(GlobalConstants.SHUTDOWN_HOUR <= hour && GlobalConstants.SHUTDOWN_MINUT <= minute) {
					LOG.info("*******  GANDALF-FX SHUTDOWN  *******");
					LOG.info("\nDay: {}\nHour: {}\nMinute: {}",dayOfWeek,hour,minute);
					return false;
				}
		}else if (GlobalConstants.OFF_DAY.equalsIgnoreCase(dayOfWeek)) {
			return false;
		}
		LOG.info("\nDay: {}\nHour: {}\nMinute: {}",dayOfWeek,hour,minute);
		return true;
	}

	@Override
	public boolean closeTrade() {
		String dayOfWeek = LocalDateTime.now().getDayOfWeek().toString();
		int hour = LocalDateTime.now().getHour();
		int minute = LocalDateTime.now().getMinute();
		
		if(GlobalConstants.SHUTDOWN_DAY.equalsIgnoreCase(dayOfWeek) && GlobalConstants.CLOSE_TRADE_HOUR == hour
				&& GlobalConstants.CLOSE_TRADE_MINUT <= minute) {
			LOG.info("*******  GANDALF-FX SHUTDOWN  *******");
			LOG.info("\nDay: {}\nHour: {}\nMinute: {}",dayOfWeek,hour,minute);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean buyerSignalPerMinute() {
		//TODO: call all the signals for BUY
		//i.e candlestick, price
		boolean rulesCandlestick = rules.buySignalCandlestick();
		if(rulesCandlestick) {
			LOG.info("*******  A BUY signal from Rules Candelstick  ******");
			return true;
		}
		return false;
	}

}
