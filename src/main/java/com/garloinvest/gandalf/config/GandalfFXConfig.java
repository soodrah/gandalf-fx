package com.garloinvest.gandalf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@PropertySource(value={"urls.properties", "oanda.properties"},ignoreResourceNotFound = true)
@PropertySource(value={"file:urls.properties", "file:oanda.properties"},ignoreResourceNotFound = true)
@PropertySource(value={"file:config/urls.properties", "file:config/oanda.properties"},ignoreResourceNotFound = true)
@PropertySource(value={"file:config/urls.yml", "file:config/oanda.yml"},ignoreResourceNotFound = true)
public class GandalfFXConfig {

	@Bean(name="readCandlestickData")
    public TaskExecutor candlePoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(100);
        threadPoolTaskExecutor.setThreadNamePrefix("CandlestickData-");
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }
}
