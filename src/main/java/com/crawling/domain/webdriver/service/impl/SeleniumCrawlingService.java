package com.crawling.domain.webdriver.service.impl;

import com.crawling.domain.webdriver.dto.CrawlingRequest;
import com.crawling.domain.webdriver.dto.CrawlingResponse;
import com.crawling.domain.webdriver.service.CrawlingService;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service("seleniumCrawlingService")
@RequiredArgsConstructor
public class SeleniumCrawlingService implements CrawlingService {
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    @Override
    public CrawlingResponse crawl(CrawlingRequest request) {
        long startTime = System.currentTimeMillis();
        WebDriver driver = null;
        
        try {
            log.info("Selenium을 사용하여 크롤링 시작: {}", request.getUrl());
            
            // WebDriver 설정
            driver = createWebDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            
            // 페이지 로드
            driver.get(request.getUrl());
            
            // JavaScript 로딩 대기
            wait.until(webDriver -> 
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            
            // 추가 대기 (동적 콘텐츠 로딩)
            Thread.sleep(2000);
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            // 기본 정보 추출
            String title = driver.getTitle();
            String content = driver.findElement(By.tagName("body")).getText();
            
            // 메타데이터 추출
            Map<String, String> metadata = extractMetadata(driver);
            
            // 선택자 기반 데이터 추출
            Map<String, Object> extractedData = new HashMap<>();
            if (request.getSelectors() != null) {
                extractedData = extractDataBySelectors(driver, request.getSelectors());
            }
            
            log.info("크롤링 완료: {} ({}ms)", request.getUrl(), responseTime);
            
            return CrawlingResponse.success(request.getUrl(), title, content, 
                                          extractedData, metadata, responseTime);
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.error("크롤링 실패: {} - {}", request.getUrl(), e.getMessage());
            return CrawlingResponse.error(request.getUrl(), e.getMessage(), responseTime);
        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    log.warn("WebDriver 종료 중 오류: {}", e.getMessage());
                }
            }
        }
    }
    
    @Override
    public List<CrawlingResponse> crawlMultiple(List<CrawlingRequest> requests) {
        log.info("다중 크롤링 시작: {} 개 URL", requests.size());
        
        List<CompletableFuture<CrawlingResponse>> futures = requests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> crawl(request), executorService))
                .collect(Collectors.toList());
        
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isCrawlable(String url) {
        WebDriver driver = null;
        try {
            driver = createWebDriver();
            driver.get(url);
            return true;
        } catch (Exception e) {
            log.warn("크롤링 불가능한 URL: {} - {}", url, e.getMessage());
            return false;
        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    log.warn("WebDriver 종료 중 오류: {}", e.getMessage());
                }
            }
        }
    }
    
    private WebDriver createWebDriver() {
        // Chrome 드라이버 설정
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 헤드리스 모드
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        
        return new ChromeDriver(options);
    }
    
    private Map<String, String> extractMetadata(WebDriver driver) {
        Map<String, String> metadata = new HashMap<>();
        
        try {
            // 메타 태그 추출
            List<WebElement> metaTags = driver.findElements(By.tagName("meta"));
            for (WebElement meta : metaTags) {
                String name = meta.getAttribute("name");
                String property = meta.getAttribute("property");
                String content = meta.getAttribute("content");
                
                if (name != null && !name.isEmpty() && content != null && !content.isEmpty()) {
                    metadata.put("meta:" + name, content);
                } else if (property != null && !property.isEmpty() && content != null && !content.isEmpty()) {
                    metadata.put("meta:" + property, content);
                }
            }
            
            // 링크 정보
            List<WebElement> links = driver.findElements(By.tagName("a"));
            metadata.put("link_count", String.valueOf(links.size()));
            
            // 이미지 정보
            List<WebElement> images = driver.findElements(By.tagName("img"));
            metadata.put("image_count", String.valueOf(images.size()));
            
            // 페이지 URL
            metadata.put("current_url", driver.getCurrentUrl());
            
        } catch (Exception e) {
            log.warn("메타데이터 추출 중 오류: {}", e.getMessage());
        }
        
        return metadata;
    }
    
    private Map<String, Object> extractDataBySelectors(WebDriver driver, Map<String, String> selectors) {
        Map<String, Object> extractedData = new HashMap<>();
        
        for (Map.Entry<String, String> entry : selectors.entrySet()) {
            String key = entry.getKey();
            String selector = entry.getValue();
            
            try {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                
                if (elements.isEmpty()) {
                    extractedData.put(key, null);
                } else if (elements.size() == 1) {
                    extractedData.put(key, elements.get(0).getText());
                } else {
                    List<String> texts = elements.stream()
                            .map(WebElement::getText)
                            .collect(Collectors.toList());
                    extractedData.put(key, texts);
                }
            } catch (Exception e) {
                log.warn("선택자 처리 실패: {} - {}", selector, e.getMessage());
                extractedData.put(key, null);
            }
        }
        
        return extractedData;
    }
}
