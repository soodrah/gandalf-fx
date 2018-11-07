package com.garloinvest.gandalf.indicator.oanda.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.indicator.oanda.service.FXPrice;
import com.garloinvest.gandalf.reporter.oanda.service.ReporterCSV;
import com.garloinvest.gandalf.searcher.oanda.dto.OandaInstrumentPrice;
import com.garloinvest.gandalf.searcher.oanda.service.OandaRouter;

@Service
public class FXPriceImpl implements FXPrice {
	private static final Logger LOG = LoggerFactory.getLogger(FXPriceImpl.class);
	
	@Autowired
	private OandaRouter router;
	@Autowired
	private ReporterCSV reporter;

	@Override
	public void getCurrentPriceAllInstruments() {
		List<String> instruments = new ArrayList<>();
		instruments.add("EUR_USD");
		Map<String,OandaInstrumentPrice> bid_ask_price_map= router.readOandaInstrumentPrice(instruments);
		for(Map.Entry<String, OandaInstrumentPrice> entry : bid_ask_price_map.entrySet()) {
            System.out.println("Instrument ->"+entry.getKey());
            OandaInstrumentPrice price = entry.getValue();
            System.out.println("Time ->"+price.getTime());
            System.out.println("isTradeable ->"+price.isTradeable());
            System.out.println("Buy ->"+price.getBuy());
            System.out.println("LiquidityBuy ->"+price.getLiquidityBuy());
            System.out.println("Sell ->"+price.getSell());
            System.out.println("LiquiditySell ->"+price.getLiquiditySell());
            System.out.println("------------------------------");
            /*reporter.savedCurrentPrice(entry.getKey(),price.getTime(),price.isTradeable(),
            		price.getBuy(),price.getLiquidityBuy(),price.getSell(),price.getLiquiditySell());*/
            if(price.isTradeable()) {
            	int buyP = price.getBuy().multiply(BigDecimal.valueOf(100000)).intValue();
            	int sellP = price.getSell().multiply(BigDecimal.valueOf(100000)).intValue();
            	int spread = buyP - sellP;
            }
            
        }
	}

}
