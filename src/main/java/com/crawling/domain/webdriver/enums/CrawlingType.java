package com.crawling.domain.webdriver.enums;

public enum CrawlingType {
    SIMPLE,      // Jsoup을 사용한 간단한 HTML 파싱
    JAVASCRIPT,  // Selenium을 사용한 JavaScript 렌더링
    API          // REST API 호출
}