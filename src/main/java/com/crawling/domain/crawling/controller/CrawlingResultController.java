package com.crawling.domain.crawling.controller;

import com.crawling.domain.crawling.entity.CrawlingResult;
import com.crawling.domain.crawling.service.CrawlingResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/crawling-results")
@RequiredArgsConstructor
@Tag(name = "크롤링 결과", description = "크롤링 결과 조회 및 관리 API")
public class CrawlingResultController {
    
    private final CrawlingResultService crawlingResultService;
    
    @GetMapping
    @Operation(summary = "크롤링 결과 목록 조회", description = "페이징을 지원하는 크롤링 결과 목록을 조회합니다.")
    public ResponseEntity<Page<CrawlingResult>> getAllResults(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<CrawlingResult> results = crawlingResultService.findAll(pageable);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "크롤링 결과 상세 조회", description = "ID로 특정 크롤링 결과를 조회합니다.")
    public ResponseEntity<CrawlingResult> getResultById(
            @Parameter(description = "크롤링 결과 ID") @PathVariable Long id) {
        
        Optional<CrawlingResult> result = crawlingResultService.findById(id);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/formatted")
    @Operation(summary = "JSON 데이터 포맷팅", description = "특정 결과의 JSON 데이터를 예쁘게 포맷팅하여 조회합니다.")
    public ResponseEntity<Map<String, Object>> getFormattedResult(
            @Parameter(description = "크롤링 결과 ID") @PathVariable Long id) {
        
        Optional<CrawlingResult> resultOpt = crawlingResultService.findById(id);
        if (resultOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        CrawlingResult result = resultOpt.get();
        Map<String, Object> formattedData = new HashMap<>();
        formattedData.put("id", result.getId());
        formattedData.put("url", result.getUrl());
        formattedData.put("title", result.getTitle());
        formattedData.put("crawlingType", result.getCrawlingType());
        formattedData.put("status", result.getStatus());
        formattedData.put("responseTime", result.getResponseTime());
        formattedData.put("createdAt", result.getCreatedAt());
        formattedData.put("errorMessage", result.getErrorMessage());
        
        // JSON 필드들을 포맷팅 (Object를 JSON 문자열로 변환)
        if (result.getExtractedData() != null) {
            String jsonString = com.crawling.domain.util.JsonFormatter.convertObjectToJson(result.getExtractedData());
            formattedData.put("extractedData", com.crawling.domain.util.JsonFormatter.prettyFormat(jsonString));
        }
        if (result.getMetadata() != null) {
            String jsonString = com.crawling.domain.util.JsonFormatter.convertObjectToJson(result.getMetadata());
            formattedData.put("metadata", com.crawling.domain.util.JsonFormatter.prettyFormat(jsonString));
        }
        if (result.getLinks() != null) {
            String jsonString = com.crawling.domain.util.JsonFormatter.convertObjectToJson(result.getLinks());
            formattedData.put("links", com.crawling.domain.util.JsonFormatter.prettyFormat(jsonString));
        }
        if (result.getImages() != null) {
            String jsonString = com.crawling.domain.util.JsonFormatter.convertObjectToJson(result.getImages());
            formattedData.put("images", com.crawling.domain.util.JsonFormatter.prettyFormat(jsonString));
        }
        if (result.getHeadings() != null) {
            String jsonString = com.crawling.domain.util.JsonFormatter.convertObjectToJson(result.getHeadings());
            formattedData.put("headings", com.crawling.domain.util.JsonFormatter.prettyFormat(jsonString));
        }
        if (result.getSelectorResults() != null) {
            String jsonString = com.crawling.domain.util.JsonFormatter.convertObjectToJson(result.getSelectorResults());
            formattedData.put("selectorResults", com.crawling.domain.util.JsonFormatter.prettyFormat(jsonString));
        }
        if (result.getAttributeResults() != null) {
            String jsonString = com.crawling.domain.util.JsonFormatter.convertObjectToJson(result.getAttributeResults());
            formattedData.put("attributeResults", com.crawling.domain.util.JsonFormatter.prettyFormat(jsonString));
        }
        if (result.getElements() != null) {
            String jsonString = com.crawling.domain.util.JsonFormatter.convertObjectToJson(result.getElements());
            formattedData.put("elements", com.crawling.domain.util.JsonFormatter.prettyFormat(jsonString));
        }
        
        // 텍스트 콘텐츠는 길이 제한
        if (result.getContent() != null) {
            String content = result.getContent();
            if (content.length() > 1000) {
                content = content.substring(0, 1000) + "... (총 " + content.length() + "자)";
            }
            formattedData.put("content", content);
        }
        
        if (result.getHtml() != null) {
            String html = result.getHtml();
            if (html.length() > 2000) {
                html = html.substring(0, 2000) + "... (총 " + html.length() + "자)";
            }
            formattedData.put("html", html);
        }
        
        return ResponseEntity.ok(formattedData);
    }
    
    @GetMapping("/url")
    @Operation(summary = "URL로 크롤링 결과 조회", description = "특정 URL의 크롤링 결과를 조회합니다.")
    public ResponseEntity<List<CrawlingResult>> getResultsByUrl(
            @Parameter(description = "크롤링한 URL") @RequestParam String url) {
        
        List<CrawlingResult> results = crawlingResultService.findByUrl(url);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/type")
    @Operation(summary = "크롤링 타입으로 결과 조회", description = "크롤링 타입별 결과를 조회합니다.")
    public ResponseEntity<List<CrawlingResult>> getResultsByType(
            @Parameter(description = "크롤링 타입") @RequestParam String type) {
        
        List<CrawlingResult> results = crawlingResultService.findByCrawlingType(type);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/status")
    @Operation(summary = "상태로 크롤링 결과 조회", description = "성공/실패 상태별 결과를 조회합니다.")
    public ResponseEntity<List<CrawlingResult>> getResultsByStatus(
            @Parameter(description = "크롤링 상태") @RequestParam String status) {
        
        List<CrawlingResult> results = crawlingResultService.findByStatus(status);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/successful")
    @Operation(summary = "성공한 크롤링 결과 조회", description = "성공한 크롤링 결과만 조회합니다.")
    public ResponseEntity<List<CrawlingResult>> getSuccessfulResults() {
        
        List<CrawlingResult> results = crawlingResultService.findSuccessfulResults();
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/search")
    @Operation(summary = "크롤링 결과 검색", description = "제목이나 내용에서 키워드를 검색합니다.")
    public ResponseEntity<List<CrawlingResult>> searchResults(
            @Parameter(description = "검색 키워드") @RequestParam String keyword,
            @Parameter(description = "검색 필드 (title, content)") @RequestParam(defaultValue = "title") String field) {
        
        List<CrawlingResult> results;
        if ("content".equals(field)) {
            results = crawlingResultService.findByContentContaining(keyword);
        } else {
            results = crawlingResultService.findByTitleContaining(keyword);
        }
        
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/latest")
    @Operation(summary = "최신 크롤링 결과 조회", description = "특정 URL의 최신 크롤링 결과를 조회합니다.")
    public ResponseEntity<List<CrawlingResult>> getLatestResults(
            @Parameter(description = "크롤링한 URL") @RequestParam String url) {
        
        List<CrawlingResult> results = crawlingResultService.findLatestByUrl(url);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/stats")
    @Operation(summary = "크롤링 통계 조회", description = "크롤링 통계 정보를 조회합니다.")
    public ResponseEntity<Map<String, Object>> getCrawlingStats() {
        
        Map<String, Object> stats = Map.of(
            "totalCount", crawlingResultService.getTotalCount(),
            "successCount", crawlingResultService.getSuccessCount(),
            "errorCount", crawlingResultService.getErrorCount(),
            "averageResponseTime", crawlingResultService.getAverageResponseTime(),
            "statsByType", crawlingResultService.getCrawlingStatsByType(),
            "statsByStatus", crawlingResultService.getCrawlingStatsByStatus(),
            "statsByDate", crawlingResultService.getCrawlingStatsByDate(),
            "mostCrawledUrls", crawlingResultService.getMostCrawledUrls()
        );
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/type")
    @Operation(summary = "크롤링 타입별 통계", description = "크롤링 타입별 통계를 조회합니다.")
    public ResponseEntity<Map<String, Long>> getStatsByType() {
        
        Map<String, Long> stats = crawlingResultService.getCrawlingStatsByType();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/status")
    @Operation(summary = "상태별 통계", description = "크롤링 상태별 통계를 조회합니다.")
    public ResponseEntity<Map<String, Long>> getStatsByStatus() {
        
        Map<String, Long> stats = crawlingResultService.getCrawlingStatsByStatus();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/date")
    @Operation(summary = "일별 통계", description = "일별 크롤링 통계를 조회합니다.")
    public ResponseEntity<Map<String, Long>> getStatsByDate() {
        
        Map<String, Long> stats = crawlingResultService.getCrawlingStatsByDate();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/urls")
    @Operation(summary = "인기 URL 통계", description = "가장 많이 크롤링된 URL 통계를 조회합니다.")
    public ResponseEntity<Map<String, Long>> getMostCrawledUrls() {
        
        Map<String, Long> stats = crawlingResultService.getMostCrawledUrls();
        return ResponseEntity.ok(stats);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "크롤링 결과 삭제", description = "특정 크롤링 결과를 삭제합니다.")
    public ResponseEntity<Void> deleteResult(
            @Parameter(description = "크롤링 결과 ID") @PathVariable Long id) {
        
        crawlingResultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/url")
    @Operation(summary = "URL별 크롤링 결과 삭제", description = "특정 URL의 모든 크롤링 결과를 삭제합니다.")
    public ResponseEntity<Void> deleteResultsByUrl(
            @Parameter(description = "크롤링한 URL") @RequestParam String url) {
        
        crawlingResultService.deleteByUrl(url);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/cleanup")
    @Operation(summary = "오래된 결과 정리", description = "지정된 날짜 이전의 크롤링 결과를 삭제합니다.")
    public ResponseEntity<Void> cleanupOldResults(
            @Parameter(description = "기준 날짜 (yyyy-MM-dd)") @RequestParam String cutoffDate) {
        
        LocalDateTime cutoff = LocalDateTime.parse(cutoffDate + "T00:00:00");
        crawlingResultService.deleteOldResults(cutoff);
        return ResponseEntity.noContent().build();
    }
}
