package com.crawling.domain.webdriver.service;

import com.crawling.domain.webdriver.dto.CrawlingRequest;
import com.crawling.domain.webdriver.dto.CrawlingResponse;

import java.util.List;

public interface CrawlingService {
    
    /**
     * 웹 페이지를 크롤링합니다.
     * 
     * @param request 크롤링 요청 정보
     * @return 크롤링 결과
     */
    CrawlingResponse crawl(CrawlingRequest request);
    
    /**
     * 여러 URL을 동시에 크롤링합니다.
     * 
     * @param requests 크롤링 요청 목록
     * @return 크롤링 결과 목록
     */
    List<CrawlingResponse> crawlMultiple(List<CrawlingRequest> requests);
    
    /**
     * 크롤링 가능 여부를 확인합니다.
     * 
     * @param url 확인할 URL
     * @return 크롤링 가능 여부
     */
    boolean isCrawlable(String url);
}
