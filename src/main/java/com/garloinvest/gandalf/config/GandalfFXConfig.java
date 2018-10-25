package com.garloinvest.gandalf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@PropertySource(value={"urls.properties", "oanda.properties"},ignoreResourceNotFound = true)
@PropertySource(value={"file:urls.properties", "file:oanda.properties"},ignoreResourceNotFound = true)
@PropertySource(value={"file:config/urls.properties", "file:config/oanda.properties"},ignoreResourceNotFound = true)
@PropertySource(value={"file:config/urls.yml", "file:config/oanda.yml"},ignoreResourceNotFound = true)
public class GandalfFXConfig implements SchedulingConfigurer{
	private final int POOL_SIZE = 10;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
		threadPoolTaskScheduler.setThreadNamePrefix("GANDALF-FX-");
		threadPoolTaskScheduler.initialize();
		
		taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}
}
