package com.crawling.domain.web.controller;

import com.crawling.core.annotation.LogExecutionTime;
import com.crawling.core.annotation.LogMethod;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupRequest;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupResponse;
import com.crawling.domain.beautifulsoup.service.BeautifulSoupService;
import com.crawling.domain.webdriver.dto.CrawlingRequest;
import com.crawling.domain.webdriver.dto.CrawlingResponse;
import com.crawling.domain.webdriver.service.CrawlingServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
@LogExecutionTime
@LogMethod
public class CrawlingTestController {
    
    private final BeautifulSoupService beautifulSoupService;
    private final CrawlingServiceFactory crawlingServiceFactory;
    
    @GetMapping
    public String testHome() {
        return "crawling-test/index";
    }
    
    @GetMapping("/beautifulsoup")
    public String beautifulSoupTest() {
        return "crawling-test/beautifulsoup";
    }
    
    @GetMapping("/webdriver")
    public String webDriverTest() {
        return "crawling-test/webdriver";
    }
    
    @PostMapping("/beautifulsoup/parse")
    @ResponseBody
    public Map<String, Object> parseBeautifulSoup(@RequestParam String url,
                                                  @RequestParam(required = false) String titleSelector,
                                                  @RequestParam(required = false) String contentSelector,
                                                  @RequestParam(required = false) String linkSelector,
                                                  @RequestParam(required = false) String imageSelector) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, String> selectors = new HashMap<>();
            if (titleSelector != null && !titleSelector.isEmpty()) {
                selectors.put("title", titleSelector);
            }
            if (contentSelector != null && !contentSelector.isEmpty()) {
                selectors.put("content", contentSelector);
            }
            if (linkSelector != null && !linkSelector.isEmpty()) {
                selectors.put("links", linkSelector);
            }
            if (imageSelector != null && !imageSelector.isEmpty()) {
                selectors.put("images", imageSelector);
            }
            
            BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                    .url(url)
                    .selectors(selectors)
                    .extractText(true)
                    .extractTitle(true)
                    .extractMeta(true)
                    .extractLinks(true)
                    .extractImages(true)
                    .extractHeadings(true)
                    .timeout(15000)
                    .build();
            
            BeautifulSoupResponse response = beautifulSoupService.parse(request);
            
            result.put("success", true);
            result.put("data", response);
            
        } catch (Exception e) {
            log.error("BeautifulSoup 파싱 오류: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @PostMapping("/webdriver/crawl")
    @ResponseBody
    public Map<String, Object> crawlWebDriver(@RequestParam String url,
                                              @RequestParam String type,
                                              @RequestParam(required = false) String titleSelector,
                                              @RequestParam(required = false) String contentSelector,
                                              @RequestParam(required = false) String linkSelector,
                                              @RequestParam(required = false) String imageSelector) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, String> selectors = new HashMap<>();
            if (titleSelector != null && !titleSelector.isEmpty()) {
                selectors.put("title", titleSelector);
            }
            if (contentSelector != null && !contentSelector.isEmpty()) {
                selectors.put("content", contentSelector);
            }
            if (linkSelector != null && !linkSelector.isEmpty()) {
                selectors.put("links", linkSelector);
            }
            if (imageSelector != null && !imageSelector.isEmpty()) {
                selectors.put("images", imageSelector);
            }
            
            CrawlingRequest.CrawlingType crawlingType = CrawlingRequest.CrawlingType.valueOf(type.toUpperCase());
            
            CrawlingRequest request = CrawlingRequest.builder()
                    .url(url)
                    .type(crawlingType)
                    .selectors(selectors)
                    .timeout(15000)
                    .build();
            
            var service = crawlingServiceFactory.getService(crawlingType);
            CrawlingResponse response = service.crawl(request);
            
            result.put("success", true);
            result.put("data", response);
            
        } catch (Exception e) {
            log.error("WebDriver 크롤링 오류: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @PostMapping("/beautifulsoup/parse-html")
    @ResponseBody
    public Map<String, Object> parseHtml(@RequestParam String html,
                                         @RequestParam(required = false) String titleSelector,
                                         @RequestParam(required = false) String contentSelector,
                                         @RequestParam(required = false) String linkSelector,
                                         @RequestParam(required = false) String imageSelector) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, String> selectors = new HashMap<>();
            if (titleSelector != null && !titleSelector.isEmpty()) {
                selectors.put("title", titleSelector);
            }
            if (contentSelector != null && !contentSelector.isEmpty()) {
                selectors.put("content", contentSelector);
            }
            if (linkSelector != null && !linkSelector.isEmpty()) {
                selectors.put("links", linkSelector);
            }
            if (imageSelector != null && !imageSelector.isEmpty()) {
                selectors.put("images", imageSelector);
            }
            
            BeautifulSoupResponse response = beautifulSoupService.parseHtml(html, selectors);
            
            result.put("success", true);
            result.put("data", response);
            
        } catch (Exception e) {
            log.error("HTML 파싱 오류: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}
