package com.crawling.domain.web.controller;

import com.crawling.core.annotation.LogExecutionTime;
import com.crawling.core.annotation.LogMethod;
import com.crawling.domain.crawling.entity.CrawlingResult;
import com.crawling.domain.crawling.service.CrawlingResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/test/results")
@RequiredArgsConstructor
@LogExecutionTime
@LogMethod
public class CrawlingResultWebController {
    
    private final CrawlingResultService crawlingResultService;
    
    @GetMapping
    public String resultsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String field,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CrawlingResult> results;
        
        if (type != null && !type.isEmpty()) {
            // 타입별 조회는 페이징을 지원하지 않으므로 전체 조회 후 수동으로 페이징 처리
            List<CrawlingResult> allResults = crawlingResultService.findByCrawlingType(type);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<CrawlingResult> pageContent = allResults.subList(start, end);
            results = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, allResults.size());
        } else if (status != null && !status.isEmpty()) {
            // 상태별 조회도 페이징을 지원하지 않으므로 전체 조회 후 수동으로 페이징 처리
            List<CrawlingResult> allResults = crawlingResultService.findByStatus(status);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<CrawlingResult> pageContent = allResults.subList(start, end);
            results = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, allResults.size());
        } else if (search != null && !search.isEmpty()) {
            // 검색도 페이징을 지원하지 않으므로 전체 조회 후 수동으로 페이징 처리
            List<CrawlingResult> allResults;
            if ("content".equals(field)) {
                allResults = crawlingResultService.findByContentContaining(search);
            } else {
                allResults = crawlingResultService.findByTitleContaining(search);
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<CrawlingResult> pageContent = allResults.subList(start, end);
            results = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, allResults.size());
        } else {
            results = crawlingResultService.findAll(pageable);
        }
        
        // 통계 정보
        Map<String, Object> stats = Map.of(
            "totalCount", crawlingResultService.getTotalCount(),
            "successCount", crawlingResultService.getSuccessCount(),
            "errorCount", crawlingResultService.getErrorCount(),
            "averageResponseTime", crawlingResultService.getAverageResponseTime()
        );
        
        model.addAttribute("results", results);
        model.addAttribute("stats", stats);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("currentType", type);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentField", field);
        
        return "crawling-test/results";
    }
    
    
    @GetMapping("/stats")
    public String statsPage(Model model) {
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
        
        model.addAttribute("stats", stats);
        return "crawling-test/stats";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteResult(@PathVariable Long id) {
        crawlingResultService.deleteById(id);
        return "redirect:/test/results";
    }
    
    @PostMapping("/url/delete")
    public String deleteResultsByUrl(@RequestParam String url) {
        crawlingResultService.deleteByUrl(url);
        return "redirect:/test/results";
    }

    @GetMapping("/{id}")
    public String getCrawlingResultDetail(@PathVariable Long id, Model model) {
        CrawlingResult result = crawlingResultService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("크롤링 결과를 찾을 수 없습니다: " + id));
        
        model.addAttribute("result", result);
        return "crawling-test/detail";
    }
}
