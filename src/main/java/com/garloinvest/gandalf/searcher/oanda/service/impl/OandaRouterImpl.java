package com.garloinvest.gandalf.searcher.oanda.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.garloinvest.gandalf.searcher.oanda.connection.OandaConnectionFXPractice;
import com.garloinvest.gandalf.searcher.oanda.dto.OandaInstrumentCandlestick;
import com.garloinvest.gandalf.searcher.oanda.dto.OandaInstrumentPrice;
import com.garloinvest.gandalf.searcher.oanda.service.OandaRouter;
import com.garloinvest.gandalf.util.DateUtil;
import com.oanda.v20.Context;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.pricing_common.PriceBucket;
import com.oanda.v20.primitives.InstrumentName;

@Service
public class OandaRouterImpl implements OandaRouter {
	private static final Logger LOG = LoggerFactory.getLogger(OandaRouterImpl.class);
	
	@Autowired
    private Environment environment;
    @Autowired
    private OandaConnectionFXPractice connection;
	
	@Override
	public Map<String, Map<LocalDateTime, OandaInstrumentCandlestick>> readOandaInstrumentCandlestickPerMinute(
			String instrumentName) {
		Map<String, Map<LocalDateTime, OandaInstrumentCandlestick>> instrument_candle_map = new HashMap<>();
        Map<LocalDateTime, OandaInstrumentCandlestick> candlestickMap = new TreeMap<>();
        Context context = connection.getConnectionFXPractice();
        InstrumentCandlesRequest request = new InstrumentCandlesRequest(new InstrumentName(instrumentName));
        request.setGranularity(CandlestickGranularity.M1);

        try {
            InstrumentCandlesResponse response = context.instrument.candles(request);
            LOG.info("Response Executed for Instrument -> {} at every -> {} Local Time -> {}",response.getInstrument(),
            		response.getGranularity().toString(),LocalDateTime.now());
            for(Candlestick candlestick: response.getCandles()) {
                OandaInstrumentCandlestick candlestickNow = new OandaInstrumentCandlestick();
                if(null != candlestick.getTime()) {
                	candlestickNow.setTime(DateUtil.convertFromOandaDateTimeToJavaLocalDateTime(candlestick.getTime()));
                }
                
                candlestickNow.setComplete(candlestick.getComplete());
                candlestickNow.setVolume(candlestick.getVolume());
                candlestickNow.setOpen(candlestick.getMid().getO().bigDecimalValue());
                candlestickNow.setClose(candlestick.getMid().getC().bigDecimalValue());
                candlestickNow.setHigh(candlestick.getMid().getH().bigDecimalValue());
                candlestickNow.setLow(candlestick.getMid().getL().bigDecimalValue());

                candlestickMap.put(candlestickNow.getTime(),candlestickNow);
                instrument_candle_map.put(response.getInstrument().toString(),candlestickMap);
            }
        } catch (RequestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecuteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return instrument_candle_map;

	}

	@Override
	public Map<String, OandaInstrumentPrice> readOandaInstrumentPrice(List<String> instruments) {
		Map<String,OandaInstrumentPrice> bid_ask_price_map = new HashMap<>();
        Context context = connection.getConnectionFXPractice();
        PricingGetRequest request = new PricingGetRequest(new AccountID(environment.getProperty("oanda.fxTradePractice.accountId")),instruments);
        try {
            PricingGetResponse response = context.pricing.get(request);
            LOG.info("Response Executed at -> {}", response.getTime());
            for(ClientPrice clientPrice : response.getPrices()) {
                OandaInstrumentPrice priceNow = new OandaInstrumentPrice();
                priceNow.setTradeable(clientPrice.getTradeable());
                priceNow.setTime(DateUtil.convertFromOandaDateTimeToJavaLocalDateTime(clientPrice.getTime()));
                for(PriceBucket priceBucket : clientPrice.getBids()) {
                    priceNow.setSell(priceBucket.getPrice().bigDecimalValue());
                    priceNow.setLiquiditySell(priceBucket.getLiquidity());
                }
                for(PriceBucket priceBucket : clientPrice.getAsks()) {
                    priceNow.setBuy(priceBucket.getPrice().bigDecimalValue());
                    priceNow.setLiquidityBuy(priceBucket.getLiquidity());
                }

                bid_ask_price_map.put(clientPrice.getInstrument().toString(),priceNow);
            }
        } catch (RequestException e) {
            e.printStackTrace();
            LOG.error("Error with Pricing Request -> {}", e.getErrorMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            LOG.error("Error with Execution -> {}", e.getMessage());
        }
        return bid_ask_price_map;

	}

}
