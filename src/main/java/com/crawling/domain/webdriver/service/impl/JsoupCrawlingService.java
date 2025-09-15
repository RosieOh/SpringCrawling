package com.crawling.domain.webdriver.service.impl;

import com.crawling.domain.webdriver.dto.CrawlingRequest;
import com.crawling.domain.webdriver.dto.CrawlingResponse;
import com.crawling.domain.webdriver.service.CrawlingService;
import com.crawling.domain.crawling.service.CrawlingResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service("jsoupCrawlingService")
@RequiredArgsConstructor
public class JsoupCrawlingService implements CrawlingService {
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final CrawlingResultService crawlingResultService;
    
    @Override
    public CrawlingResponse crawl(CrawlingRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("Jsoup을 사용하여 크롤링 시작: {}", request.getUrl());
            
            // Jsoup 연결 설정
            var connection = Jsoup.connect(request.getUrl())
                    .timeout(request.getTimeout() != null ? request.getTimeout() : 10000)
                    .followRedirects(true)
                    .userAgent(request.getUserAgent() != null ? request.getUserAgent() : 
                              "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            
            // 헤더 추가
            if (request.getHeaders() != null) {
                request.getHeaders().forEach(connection::header);
            }
            
            Document document = connection.get();
            long responseTime = System.currentTimeMillis() - startTime;
            
            // 기본 정보 추출
            String title = document.title();
            String content = document.text();
            
            // 메타데이터 추출
            Map<String, String> metadata = extractMetadata(document);
            
            // 선택자 기반 데이터 추출
            Map<String, Object> extractedData = new HashMap<>();
            if (request.getSelectors() != null) {
                extractedData = extractDataBySelectors(document, request.getSelectors());
            }
            
            log.info("크롤링 완료: {} ({}ms)", request.getUrl(), responseTime);
            
            CrawlingResponse response = CrawlingResponse.success(request.getUrl(), title, content, 
                                          extractedData, metadata, responseTime);
            
            // 크롤링 결과를 데이터베이스에 저장
            try {
                crawlingResultService.saveWebDriverResult(response, "SIMPLE");
            } catch (Exception e) {
                log.warn("크롤링 결과 저장 실패: {}", e.getMessage());
            }
            
            return response;
            
        } catch (IOException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.error("크롤링 실패: {} - {}", request.getUrl(), e.getMessage());
            return CrawlingResponse.error(request.getUrl(), e.getMessage(), responseTime);
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.error("크롤링 중 예상치 못한 오류: {} - {}", request.getUrl(), e.getMessage());
            return CrawlingResponse.error(request.getUrl(), "크롤링 중 오류가 발생했습니다: " + e.getMessage(), responseTime);
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
        try {
            Jsoup.connect(url)
                    .timeout(5000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .get();
            return true;
        } catch (Exception e) {
            log.warn("크롤링 불가능한 URL: {} - {}", url, e.getMessage());
            return false;
        }
    }
    
    private Map<String, String> extractMetadata(Document document) {
        Map<String, String> metadata = new HashMap<>();
        
        // 메타 태그 추출
        Elements metaTags = document.select("meta");
        for (Element meta : metaTags) {
            String name = meta.attr("name");
            String property = meta.attr("property");
            String content = meta.attr("content");
            
            if (!name.isEmpty() && !content.isEmpty()) {
                metadata.put("meta:" + name, content);
            } else if (!property.isEmpty() && !content.isEmpty()) {
                metadata.put("meta:" + property, content);
            }
        }
        
        // 링크 정보
        Elements links = document.select("a[href]");
        metadata.put("link_count", String.valueOf(links.size()));
        
        // 이미지 정보
        Elements images = document.select("img[src]");
        metadata.put("image_count", String.valueOf(images.size()));
        
        // 페이지 언어
        String lang = document.select("html").attr("lang");
        if (!lang.isEmpty()) {
            metadata.put("language", lang);
        }
        
        return metadata;
    }
    
    private Map<String, Object> extractDataBySelectors(Document document, Map<String, String> selectors) {
        Map<String, Object> extractedData = new HashMap<>();
        
        for (Map.Entry<String, String> entry : selectors.entrySet()) {
            String key = entry.getKey();
            String selector = entry.getValue();
            
            try {
                Elements elements = document.select(selector);
                
                if (elements.isEmpty()) {
                    extractedData.put(key, null);
                } else if (elements.size() == 1) {
                    extractedData.put(key, elements.first().text());
                } else {
                    List<String> texts = elements.stream()
                            .map(Element::text)
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
