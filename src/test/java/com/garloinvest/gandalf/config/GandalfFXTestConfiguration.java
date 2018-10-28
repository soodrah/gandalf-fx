package com.garloinvest.gandalf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import com.garloinvest.gandalf.indicator.oanda.service.FXCandle;
import com.garloinvest.gandalf.indicator.oanda.service.impl.FXCandleImpl;
import com.garloinvest.gandalf.reporter.oanda.service.ReporterCSV;
import com.garloinvest.gandalf.reporter.oanda.service.impl.ReporterCSVImpl;
import com.garloinvest.gandalf.searcher.oanda.connection.OandaConnectionFXPractice;
import com.garloinvest.gandalf.searcher.oanda.service.OandaRouter;
import com.garloinvest.gandalf.searcher.oanda.service.impl.OandaRouterImpl;

@Configuration
@ActiveProfiles("test")
@PropertySource("classpath:test.properties")
public class GandalfFXTestConfiguration {
	
	@Bean
	public ReporterCSV getReporterCSVService() {
		return new ReporterCSVImpl();
	}
	
	@Bean
	public OandaRouter getOandaRouterService() {
		return new OandaRouterImpl();
	}
	
	@Bean
	public OandaConnectionFXPractice getOandaConnection() {
		return new OandaConnectionFXPractice();
	}
	
	@Bean
	public FXCandle getFXCandleData() {
		return new FXCandleImpl();
	}
}
