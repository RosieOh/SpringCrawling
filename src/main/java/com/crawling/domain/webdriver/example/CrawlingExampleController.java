package com.crawling.domain.webdriver.example;

import com.crawling.domain.webdriver.dto.CrawlingRequest;
import com.crawling.domain.webdriver.dto.CrawlingResponse;
import com.crawling.domain.webdriver.service.CrawlingService;
import com.crawling.domain.webdriver.service.CrawlingServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/crawling/examples")
@RequiredArgsConstructor
@Tag(name = "크롤링 예제", description = "다양한 크롤링 예제 API")
public class CrawlingExampleController {
    
    private final CrawlingServiceFactory crawlingServiceFactory;
    
    @GetMapping("/naver-news")
    @Operation(summary = "네이버 뉴스 크롤링 예제", description = "네이버 뉴스 메인 페이지를 크롤링합니다.")
    public ResponseEntity<CrawlingResponse> crawlNaverNews() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("headlines", ".news_area .news_tit");
        selectors.put("summaries", ".news_area .news_dsc");
        selectors.put("links", ".news_area .news_tit a");
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url("https://news.naver.com")
                .type(CrawlingRequest.CrawlingType.SIMPLE)
                .selectors(selectors)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.SIMPLE);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/github-trending")
    @Operation(summary = "GitHub 트렌딩 크롤링 예제", description = "GitHub 트렌딩 저장소를 크롤링합니다.")
    public ResponseEntity<CrawlingResponse> crawlGithubTrending() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("repositories", ".Box .Box-row h2 a");
        selectors.put("descriptions", ".Box .Box-row p");
        selectors.put("languages", ".Box .Box-row .d-inline-block .ml-0");
        selectors.put("stars", ".Box .Box-row .octicon-star");
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url("https://github.com/trending")
                .type(CrawlingRequest.CrawlingType.SIMPLE)
                .selectors(selectors)
                .timeout(15000)
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.SIMPLE);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/stackoverflow-questions")
    @Operation(summary = "Stack Overflow 질문 크롤링 예제", description = "Stack Overflow의 최신 질문들을 크롤링합니다.")
    public ResponseEntity<CrawlingResponse> crawlStackOverflowQuestions() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("questions", ".s-post-summary .s-post-summary--content-title a");
        selectors.put("tags", ".s-post-summary .s-post-summary--meta .s-tag");
        selectors.put("votes", ".s-post-summary .s-post-summary--stats .s-post-summary--stats-item");
        selectors.put("answers", ".s-post-summary .s-post-summary--stats .s-post-summary--stats-item");
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url("https://stackoverflow.com/questions")
                .type(CrawlingRequest.CrawlingType.SIMPLE)
                .selectors(selectors)
                .timeout(10000)
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.SIMPLE);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/spa-example")
    @Operation(summary = "SPA 크롤링 예제", description = "JavaScript로 렌더링되는 SPA 페이지를 크롤링합니다.")
    public ResponseEntity<CrawlingResponse> crawlSpaExample() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("dynamic_content", ".dynamic-content");
        selectors.put("loaded_data", ".loaded-data");
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url("https://example.com/spa-page") // 실제 SPA URL로 변경 필요
                .type(CrawlingRequest.CrawlingType.JAVASCRIPT)
                .selectors(selectors)
                .timeout(30000)
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.JAVASCRIPT);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/api-example")
    @Operation(summary = "API 크롤링 예제", description = "REST API를 호출하여 JSON 데이터를 가져옵니다.")
    public ResponseEntity<CrawlingResponse> crawlApiExample() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("User-Agent", "CrawlingBot/1.0");
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url("https://jsonplaceholder.typicode.com/posts")
                .type(CrawlingRequest.CrawlingType.API)
                .headers(headers)
                .timeout(10000)
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.API);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/custom")
    @Operation(summary = "커스텀 크롤링 예제", description = "사용자가 지정한 URL과 선택자로 크롤링합니다.")
    public ResponseEntity<CrawlingResponse> crawlCustom(
            @RequestParam String url,
            @RequestParam(required = false) String titleSelector,
            @RequestParam(required = false) String contentSelector,
            @RequestParam(required = false) String linkSelector) {
        
        Map<String, String> selectors = new HashMap<>();
        if (titleSelector != null) selectors.put("title", titleSelector);
        if (contentSelector != null) selectors.put("content", contentSelector);
        if (linkSelector != null) selectors.put("links", linkSelector);
        
        CrawlingRequest request = CrawlingRequest.builder()
                .url(url)
                .type(CrawlingRequest.CrawlingType.SIMPLE)
                .selectors(selectors)
                .timeout(15000)
                .build();
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.SIMPLE);
        CrawlingResponse response = service.crawl(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/batch-example")
    @Operation(summary = "배치 크롤링 예제", description = "여러 URL을 동시에 크롤링하는 예제입니다.")
    public ResponseEntity<List<CrawlingResponse>> crawlBatchExample() {
        List<CrawlingRequest> requests = List.of(
            CrawlingRequest.builder()
                .url("https://news.naver.com")
                .type(CrawlingRequest.CrawlingType.SIMPLE)
                .build(),
            CrawlingRequest.builder()
                .url("https://github.com/trending")
                .type(CrawlingRequest.CrawlingType.SIMPLE)
                .build(),
            CrawlingRequest.builder()
                .url("https://stackoverflow.com/questions")
                .type(CrawlingRequest.CrawlingType.SIMPLE)
                .build()
        );
        
        CrawlingService service = crawlingServiceFactory.getService(CrawlingRequest.CrawlingType.SIMPLE);
        List<CrawlingResponse> responses = service.crawlMultiple(requests);
        
        return ResponseEntity.ok(responses);
    }
}
