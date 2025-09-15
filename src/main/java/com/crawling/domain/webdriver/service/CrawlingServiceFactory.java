package com.crawling.domain.webdriver.service;

import com.crawling.domain.webdriver.dto.CrawlingRequest;
import com.crawling.domain.webdriver.enums.CrawlingType;
import com.crawling.domain.webdriver.service.impl.ApiCrawlingService;
import com.crawling.domain.webdriver.service.impl.JsoupCrawlingService;
import com.crawling.domain.webdriver.service.impl.SeleniumCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlingServiceFactory {
    
    private final JsoupCrawlingService jsoupCrawlingService;
    private final SeleniumCrawlingService seleniumCrawlingService;
    private final ApiCrawlingService apiCrawlingService;
    
    public CrawlingService getService(CrawlingType type) {
        return switch (type) {
            case SIMPLE -> jsoupCrawlingService;
            case JAVASCRIPT -> seleniumCrawlingService;
            case API -> apiCrawlingService;
        };
    }
}
