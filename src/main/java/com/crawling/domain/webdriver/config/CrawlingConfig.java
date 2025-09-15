package com.crawling.domain.webdriver.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class CrawlingConfig {
    
    @Bean(name = "crawlingTaskExecutor")
    public Executor crawlingTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Crawling-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        
        return executor;
    }
    
    @Bean
    public WebDriverManager webDriverManager() {

        // WebDriver 자동 관리 설정
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        
        log.info("WebDriver Manager 설정 완료");
        
        return WebDriverManager.getInstance();
    }
}
