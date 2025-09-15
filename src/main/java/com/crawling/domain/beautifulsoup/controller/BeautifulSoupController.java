package com.crawling.domain.beautifulsoup.controller;

import com.crawling.core.annotation.LogExecutionTime;
import com.crawling.core.annotation.LogMethod;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupRequest;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupResponse;
import com.crawling.domain.beautifulsoup.service.BeautifulSoupService;
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
@RequestMapping("/api/beautifulsoup")
@RequiredArgsConstructor
@LogExecutionTime
@LogMethod
@Tag(name = "BeautifulSoup 크롤링", description = "BeautifulSoup 스타일 웹 크롤링 API")
public class BeautifulSoupController {
    
    private final BeautifulSoupService beautifulSoupService;
    
    @PostMapping("/parse")
    @Operation(summary = "BeautifulSoup 스타일 파싱", description = "BeautifulSoup 스타일로 웹 페이지를 파싱")
    public ResponseEntity<BeautifulSoupResponse> parse(
            @Valid @RequestBody BeautifulSoupRequest request) {
        
        log.info("BeautifulSoup 파싱 요청: {}", request.getUrl());
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/parse/multiple")
    @Operation(summary = "다중 URL BeautifulSoup 파싱", description = "여러 URL을 동시에 BeautifulSoup 스타일로 파싱")
    public ResponseEntity<List<BeautifulSoupResponse>> parseMultiple(
            @Valid @RequestBody List<BeautifulSoupRequest> requests) {
        
        log.info("다중 BeautifulSoup 파싱 요청: {} 개 URL", requests.size());
        
        if (requests.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<BeautifulSoupResponse> responses = beautifulSoupService.parseMultiple(requests);
        
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping("/parse/html")
    @Operation(summary = "HTML 문자열 파싱", description = "HTML 문자열을 직접 BeautifulSoup 스타일로 파싱")
    public ResponseEntity<BeautifulSoupResponse> parseHtml(
            @Parameter(description = "파싱할 HTML 문자열") @RequestParam String html,
            @Parameter(description = "선택자 맵 (JSON 형태)") @RequestBody(required = false) Map<String, String> selectors) {
        
        log.info("HTML 문자열 파싱 요청");
        
        BeautifulSoupResponse response = beautifulSoupService.parseHtml(html, selectors);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check")
    @Operation(summary = "파싱 가능 여부 확인", description = "URL이 BeautifulSoup으로 파싱 가능한지 확인")
    public ResponseEntity<Map<String, Object>> checkParseable(
            @Parameter(description = "확인할 URL") @RequestParam String url) {
        
        log.info("BeautifulSoup 파싱 가능 여부 확인: {}", url);
        
        boolean isParseable = beautifulSoupService.isParseable(url);
        
        Map<String, Object> result = Map.of(
            "url", url,
            "parseable", isParseable
        );
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/parse/simple")
    @Operation(summary = "간단한 파싱", description = "기본 설정으로 간단하게 파싱")
    public ResponseEntity<BeautifulSoupResponse> parseSimple(
            @Parameter(description = "파싱할 URL") @RequestParam String url) {
        
        BeautifulSoupRequest request = BeautifulSoupRequest.simple(url);
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/parse/with-selectors")
    @Operation(summary = "선택자 기반 파싱", description = "선택자를 지정하여 파싱")
    public ResponseEntity<BeautifulSoupResponse> parseWithSelectors(
            @Parameter(description = "파싱할 URL") @RequestParam String url,
            @Parameter(description = "선택자 맵 (JSON 형태)") @RequestBody Map<String, String> selectors) {
        
        BeautifulSoupRequest request = BeautifulSoupRequest.withSelectors(url, selectors);
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/parse/links")
    @Operation(summary = "링크 추출", description = "웹 페이지에서 모든 링크를 추출")
    public ResponseEntity<BeautifulSoupResponse> parseLinks(
            @Parameter(description = "파싱할 URL") @RequestParam String url,
            @Parameter(description = "링크 선택자") @RequestParam(required = false) String linkSelector) {
        
        BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                .url(url)
                .extractLinks(true)
                .linkSelectors(linkSelector != null ? List.of(linkSelector) : null)
                .build();
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/parse/images")
    @Operation(summary = "이미지 추출", description = "웹 페이지에서 모든 이미지를 추출")
    public ResponseEntity<BeautifulSoupResponse> parseImages(
            @Parameter(description = "파싱할 URL") @RequestParam String url,
            @Parameter(description = "이미지 선택자") @RequestParam(required = false) String imageSelector) {
        
        BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                .url(url)
                .extractImages(true)
                .imageSelectors(imageSelector != null ? List.of(imageSelector) : null)
                .build();
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/parse/meta")
    @Operation(summary = "메타데이터 추출", description = "웹 페이지의 메타데이터를 추출")
    public ResponseEntity<BeautifulSoupResponse> parseMeta(
            @Parameter(description = "파싱할 URL") @RequestParam String url) {
        
        BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                .url(url)
                .extractMeta(true)
                .extractTitle(true)
                .build();
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
}
