package com.crawling.domain.webdriver.controller;

import com.crawling.domain.webdriver.dto.CrawlingRequest;
import com.crawling.domain.webdriver.dto.CrawlingResponse;
import com.crawling.domain.webdriver.service.CrawlingService;
import com.crawling.domain.webdriver.service.CrawlingServiceFactory;
import com.crawling.global.common.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/crawling")
@RequiredArgsConstructor
@Tag(name = "크롤링", description = "웹 크롤링 API")
public class CrawlingController extends BaseController {
    
    private final CrawlingServiceFactory crawlingServiceFactory;
    
    @PostMapping("/crawl")
    @Operation(summary = "단일 URL 크롤링", description = "지정된 URL을 크롤링합니다.")
    public ResponseEntity<CrawlingResponse> crawl(
            @Valid @RequestBody CrawlingRequest request) {
        
        log.info("크롤링 요청: {} ({})", request.getUrl(), request.getType());
        
        CrawlingService service = crawlingServiceFactory.getService(request.getType());
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/crawl/multiple")
    @Operation(summary = "다중 URL 크롤링", description = "여러 URL을 동시에 크롤링합니다.")
    public ResponseEntity<List<CrawlingResponse>> crawlMultiple(
            @Valid @RequestBody List<CrawlingRequest> requests) {
        
        log.info("다중 크롤링 요청: {} 개 URL", requests.size());
        
        if (requests.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // 모든 요청이 같은 타입인지 확인
        CrawlingRequest.CrawlingType type = requests.get(0).getType();
        boolean allSameType = requests.stream()
                .allMatch(req -> req.getType() == type);
        
        if (!allSameType) {
            return ResponseEntity.badRequest().build();
        }
        
        CrawlingService service = crawlingServiceFactory.getService(type);
        List<CrawlingResponse> responses = service.crawlMultiple(requests);
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/check")
    @Operation(summary = "크롤링 가능 여부 확인", description = "URL이 크롤링 가능한지 확인합니다.")
    public ResponseEntity<Map<String, Object>> checkCrawlable(
            @Parameter(description = "확인할 URL") @RequestParam String url,
            @Parameter(description = "크롤링 타입") @RequestParam CrawlingRequest.CrawlingType type) {
        
        log.info("크롤링 가능 여부 확인: {} ({})", url, type);
        
        CrawlingService service = crawlingServiceFactory.getService(type);
        boolean isCrawlable = service.isCrawlable(url);
        
        Map<String, Object> result = Map.of(
            "url", url,
            "type", type,
            "crawlable", isCrawlable
        );
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/crawl/simple")
    @Operation(summary = "간단한 HTML 크롤링", description = "Jsoup을 사용한 간단한 HTML 크롤링입니다.")
    public ResponseEntity<CrawlingResponse> crawlSimple(
            @Parameter(description = "크롤링할 URL") @RequestParam String url,
            @Parameter(description = "타임아웃 (밀리초)") @RequestParam(required = false) Integer timeout,
            @Parameter(description = "User-Agent") @RequestParam(required = false) String userAgent) {
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url(url)
                .type(CrawlingRequest.CrawlingType.SIMPLE)
                .timeout(timeout)
                .userAgent(userAgent)
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.SIMPLE);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/crawl/javascript")
    @Operation(summary = "JavaScript 렌더링 크롤링", description = "Selenium을 사용한 JavaScript 렌더링 크롤링입니다.")
    public ResponseEntity<CrawlingResponse> crawlJavaScript(
            @Parameter(description = "크롤링할 URL") @RequestParam String url,
            @Parameter(description = "선택자 맵 (JSON 형태)") @RequestBody(required = false) Map<String, String> selectors) {
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url(url)
                .type(CrawlingRequest.CrawlingType.JAVASCRIPT)
                .selectors(selectors)
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.JAVASCRIPT);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/crawl/api")
    @Operation(summary = "API 호출 크롤링", description = "REST API를 호출하여 데이터를 가져옵니다.")
    public ResponseEntity<CrawlingResponse> crawlApi(
            @Parameter(description = "API URL") @RequestParam String url,
            @Parameter(description = "헤더 맵 (JSON 형태)") @RequestBody(required = false) Map<String, String> headers) {
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url(url)
                .type(CrawlingRequest.CrawlingType.API)
                .headers(headers)
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.API);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
}
