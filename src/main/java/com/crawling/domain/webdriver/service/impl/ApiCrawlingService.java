package com.crawling.domain.webdriver.service.impl;

import com.crawling.domain.webdriver.dto.CrawlingRequest;
import com.crawling.domain.webdriver.dto.CrawlingResponse;
import com.crawling.domain.webdriver.service.CrawlingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service("apiCrawlingService")
@RequiredArgsConstructor
public class ApiCrawlingService implements CrawlingService {
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public CrawlingResponse crawl(CrawlingRequest request) {
        long startTime = System.currentTimeMillis();
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            log.info("API 호출을 통한 크롤링 시작: {}", request.getUrl());
            
            // HTTP 요청 생성
            var httpRequest = new HttpGet(request.getUrl());
            
            // 헤더 설정
            if (request.getHeaders() != null) {
                request.getHeaders().forEach(httpRequest::setHeader);
            }
            
            // User-Agent 설정
            if (request.getUserAgent() != null) {
                httpRequest.setHeader("User-Agent", request.getUserAgent());
            } else {
                httpRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            }
            
            // Accept 헤더 설정
            httpRequest.setHeader("Accept", "application/json, text/html, */*");
            
            // 요청 실행
            try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
                long responseTime = System.currentTimeMillis() - startTime;
                
                int statusCode = response.getCode();
                String responseBody = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                
                if (statusCode >= 200 && statusCode < 300) {
                    // JSON 응답 파싱
                    Map<String, Object> extractedData = parseJsonResponse(responseBody);
                    
                    // 메타데이터 생성
                    Map<String, String> metadata = new HashMap<>();
                    metadata.put("status_code", String.valueOf(statusCode));
                    metadata.put("content_type", response.getFirstHeader("Content-Type") != null ? 
                                response.getFirstHeader("Content-Type").getValue() : "unknown");
                    metadata.put("content_length", String.valueOf(responseBody.length()));
                    
                    log.info("API 크롤링 완료: {} ({}ms)", request.getUrl(), responseTime);
                    
                    return CrawlingResponse.success(request.getUrl(), "API Response", responseBody, 
                                                  extractedData, metadata, responseTime);
                } else {
                    log.error("API 호출 실패: {} - HTTP {}", request.getUrl(), statusCode);
                    return CrawlingResponse.error(request.getUrl(), 
                                                "HTTP " + statusCode + ": " + responseBody, responseTime);
                }
            }
            
        } catch (IOException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.error("API 크롤링 실패: {} - {}", request.getUrl(), e.getMessage());
            return CrawlingResponse.error(request.getUrl(), e.getMessage(), responseTime);
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.error("API 크롤링 중 예상치 못한 오류: {} - {}", request.getUrl(), e.getMessage());
            return CrawlingResponse.error(request.getUrl(), "API 호출 중 오류가 발생했습니다: " + e.getMessage(), responseTime);
        }
    }
    
    @Override
    public List<CrawlingResponse> crawlMultiple(List<CrawlingRequest> requests) {
        log.info("다중 API 크롤링 시작: {} 개 URL", requests.size());
        
        List<CompletableFuture<CrawlingResponse>> futures = requests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> crawl(request), executorService))
                .collect(Collectors.toList());
        
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isCrawlable(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            var httpRequest = new HttpGet(url);
            httpRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            
            try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
                int statusCode = response.getCode();
                return statusCode >= 200 && statusCode < 300;
            }
        } catch (Exception e) {
            log.warn("API 크롤링 불가능한 URL: {} - {}", url, e.getMessage());
            return false;
        }
    }
    
    private Map<String, Object> parseJsonResponse(String responseBody) {
        Map<String, Object> extractedData = new HashMap<>();
        
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            extractedData = objectMapper.convertValue(jsonNode, Map.class);
        } catch (Exception e) {
            log.warn("JSON 파싱 실패: {}", e.getMessage());
            extractedData.put("raw_response", responseBody);
        }
        
        return extractedData;
    }
}
