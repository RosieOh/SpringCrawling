package com.crawling.domain.beautifulsoup.example;

import com.crawling.core.annotation.LogExecutionTime;
import com.crawling.core.annotation.LogMethod;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupRequest;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupResponse;
import com.crawling.domain.beautifulsoup.service.BeautifulSoupService;
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
@RequestMapping("/api/beautifulsoup/examples")
@RequiredArgsConstructor
@LogExecutionTime
@LogMethod
@Tag(name = "BeautifulSoup 예제", description = "BeautifulSoup 스타일 크롤링 예제 API")
public class BeautifulSoupExampleController {
    
    private final BeautifulSoupService beautifulSoupService;
    
    @GetMapping("/naver-news")
    @Operation(summary = "네이버 뉴스 BeautifulSoup 파싱", description = "네이버 뉴스를 BeautifulSoup 스타일로 파싱합니다.")
    public ResponseEntity<BeautifulSoupResponse> parseNaverNews() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("headlines", ".news_area .news_tit");
        selectors.put("summaries", ".news_area .news_dsc");
        selectors.put("links", ".news_area .news_tit a");
        selectors.put("images", ".news_area img");
        
        BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                .url("https://news.naver.com")
                .selectors(selectors)
                .extractText(true)
                .extractTitle(true)
                .extractMeta(true)
                .extractLinks(true)
                .extractImages(true)
                .extractHeadings(true)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build();
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/github-trending")
    @Operation(summary = "GitHub 트렌딩 BeautifulSoup 파싱", description = "GitHub 트렌딩을 BeautifulSoup 스타일로 파싱합니다.")
    public ResponseEntity<BeautifulSoupResponse> parseGithubTrending() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("repositories", ".Box .Box-row h2 a");
        selectors.put("descriptions", ".Box .Box-row p");
        selectors.put("languages", ".Box .Box-row .d-inline-block .ml-0");
        selectors.put("stars", ".Box .Box-row .octicon-star");
        selectors.put("forks", ".Box .Box-row .octicon-repo-forked");
        
        BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                .url("https://github.com/trending")
                .selectors(selectors)
                .extractText(true)
                .extractTitle(true)
                .extractMeta(true)
                .extractLinks(true)
                .timeout(15000)
                .build();
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/stackoverflow-questions")
    @Operation(summary = "Stack Overflow 질문 BeautifulSoup 파싱", description = "Stack Overflow 질문들을 BeautifulSoup 스타일로 파싱합니다.")
    public ResponseEntity<BeautifulSoupResponse> parseStackOverflowQuestions() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("questions", ".s-post-summary .s-post-summary--content-title a");
        selectors.put("tags", ".s-post-summary .s-post-summary--meta .s-tag");
        selectors.put("votes", ".s-post-summary .s-post-summary--stats .s-post-summary--stats-item");
        selectors.put("answers", ".s-post-summary .s-post-summary--stats .s-post-summary--stats-item");
        selectors.put("views", ".s-post-summary .s-post-summary--stats .s-post-summary--stats-item");
        
        BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                .url("https://stackoverflow.com/questions")
                .selectors(selectors)
                .extractText(true)
                .extractTitle(true)
                .extractMeta(true)
                .extractLinks(true)
                .timeout(10000)
                .build();
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/reddit-python")
    @Operation(summary = "Reddit Python 커뮤니티 BeautifulSoup 파싱", description = "Reddit Python 커뮤니티를 BeautifulSoup 스타일로 파싱합니다.")
    public ResponseEntity<BeautifulSoupResponse> parseRedditPython() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("posts", "[data-testid='post-container'] h3");
        selectors.put("upvotes", "[data-testid='post-container'] [aria-label*='upvote']");
        selectors.put("comments", "[data-testid='post-container'] [aria-label*='comment']");
        selectors.put("time", "[data-testid='post-container'] time");
        
        BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                .url("https://www.reddit.com/r/Python/")
                .selectors(selectors)
                .extractText(true)
                .extractTitle(true)
                .extractMeta(true)
                .extractLinks(true)
                .timeout(15000)
                .build();
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/hacker-news")
    @Operation(summary = "Hacker News BeautifulSoup 파싱", description = "Hacker News를 BeautifulSoup 스타일로 파싱합니다.")
    public ResponseEntity<BeautifulSoupResponse> parseHackerNews() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("stories", ".athing .storylink");
        selectors.put("scores", ".subtext .score");
        selectors.put("comments", ".subtext a[href*='item?id=']");
        selectors.put("users", ".subtext .hnuser");
        selectors.put("times", ".subtext .age");
        
        BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                .url("https://news.ycombinator.com/")
                .selectors(selectors)
                .extractText(true)
                .extractTitle(true)
                .extractMeta(true)
                .extractLinks(true)
                .timeout(10000)
                .build();
        
        BeautifulSoupResponse response = beautifulSoupService.parse(request);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/custom")
    @Operation(summary = "커스텀 BeautifulSoup 파싱", description = "사용자가 지정한 URL과 선택자로 BeautifulSoup 스타일로 파싱합니다.")
    public ResponseEntity<BeautifulSoupResponse> parseCustom(
            @RequestParam String url,
            @RequestParam(required = false) String titleSelector,
            @RequestParam(required = false) String contentSelector,
            @RequestParam(required = false) String linkSelector,
            @RequestParam(required = false) String imageSelector) {
        
        Map<String, String> selectors = new HashMap<>();
        if (titleSelector != null) selectors.put("title", titleSelector);
        if (contentSelector != null) selectors.put("content", contentSelector);
        if (linkSelector != null) selectors.put("links", linkSelector);
        if (imageSelector != null) selectors.put("images", imageSelector);
        
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
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/batch-example")
    @Operation(summary = "배치 BeautifulSoup 파싱", description = "여러 URL을 동시에 BeautifulSoup 스타일로 파싱하는 예제입니다.")
    public ResponseEntity<List<BeautifulSoupResponse>> parseBatchExample() {
        List<BeautifulSoupRequest> requests = List.of(
            BeautifulSoupRequest.simple("https://news.naver.com"),
            BeautifulSoupRequest.simple("https://github.com/trending"),
            BeautifulSoupRequest.simple("https://stackoverflow.com/questions")
        );
        
        List<BeautifulSoupResponse> responses = beautifulSoupService.parseMultiple(requests);
        
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping("/parse-html")
    @Operation(summary = "HTML 문자열 BeautifulSoup 파싱", description = "HTML 문자열을 BeautifulSoup 스타일로 파싱합니다.")
    public ResponseEntity<BeautifulSoupResponse> parseHtmlExample(
            @RequestParam String html) {
        
        Map<String, String> selectors = new HashMap<>();
        selectors.put("headings", "h1, h2, h3");
        selectors.put("paragraphs", "p");
        selectors.put("links", "a");
        selectors.put("images", "img");
        
        BeautifulSoupResponse response = beautifulSoupService.parseHtml(html, selectors);
        
        return ResponseEntity.ok(response);
    }
}
