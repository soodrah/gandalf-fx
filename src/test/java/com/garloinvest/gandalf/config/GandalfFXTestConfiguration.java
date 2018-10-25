package com.garloinvest.gandalf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import com.garloinvest.gandalf.reporter.oanda.service.ReporterCSV;
import com.garloinvest.gandalf.reporter.oanda.service.impl.ReporterCSVImpl;

@Configuration
@ActiveProfiles("test")
@PropertySource("classpath:test.properties")
public class GandalfFXTestConfiguration {
	
	@Bean
	public ReporterCSV getReporterCSVService() {
		return new ReporterCSVImpl();
	}
}
