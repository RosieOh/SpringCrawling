package com.crawling.domain.beautifulsoup.service.impl;

import com.crawling.domain.beautifulsoup.dto.BeautifulSoupRequest;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupResponse;
import com.crawling.domain.beautifulsoup.service.BeautifulSoupService;
import com.crawling.domain.crawling.service.CrawlingResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeautifulSoupServiceImpl implements BeautifulSoupService {
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 쓰레드 풀을 10까지 당김으로써 스케줄링 자체를 10회까지 진행
    private final CrawlingResultService crawlingResultService;
    
    @Override
    public BeautifulSoupResponse parse(BeautifulSoupRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("BeautifulSoup 스타일로 크롤링 시작: {}", request.getUrl());
            
            // Jsoup 연결 설정
            var connection = Jsoup.connect(request.getUrl())
                    .timeout(request.getTimeout() != null ? request.getTimeout() : 10000)
                    .followRedirects(request.getFollowRedirects() != null ? request.getFollowRedirects() : true)
                    .ignoreHttpErrors(request.getIgnoreHttpErrors() != null ? request.getIgnoreHttpErrors() : false)
                    .userAgent(request.getUserAgent() != null ? request.getUserAgent() : 
                              "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            
            // 헤더 추가
            if (request.getHeaders() != null) {
                request.getHeaders().forEach(connection::header);
            }
            
            // 인코딩 설정
            if (request.getEncoding() != null) {
                connection.parser(org.jsoup.parser.Parser.htmlParser());
            }
            
            Document document = connection.get();
            long responseTime = System.currentTimeMillis() - startTime;
            
            // BeautifulSoup 스타일로 데이터 추출
            BeautifulSoupResponse response = extractBeautifulSoupData(document, request, responseTime);
            
            // 크롤링 결과를 데이터베이스에 저장
            try {
                crawlingResultService.saveBeautifulSoupResult(response, "BEAUTIFULSOUP");
            } catch (Exception e) {
                log.warn("크롤링 결과 저장 실패: {}", e.getMessage());
            }
            
            return response;
            
        } catch (IOException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.error("BeautifulSoup 크롤링 실패: {} - {}", request.getUrl(), e.getMessage());
            return BeautifulSoupResponse.error(request.getUrl(), e.getMessage(), responseTime);
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.error("BeautifulSoup 크롤링 중 예상치 못한 오류: {} - {}", request.getUrl(), e.getMessage());
            return BeautifulSoupResponse.error(request.getUrl(), "크롤링 중 오류가 발생했습니다: " + e.getMessage(), responseTime);
        }
    }
    
    @Override
    public List<BeautifulSoupResponse> parseMultiple(List<BeautifulSoupRequest> requests) {
        log.info("다중 BeautifulSoup 크롤링 시작: {} 개 URL", requests.size());
        
        List<CompletableFuture<BeautifulSoupResponse>> futures = requests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> parse(request), executorService))
                .collect(Collectors.toList());
        
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
    
    @Override
    public BeautifulSoupResponse parseHtml(String html, Map<String, String> selectors) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("HTML 문자열 파싱 시작");
            
            Document document = Jsoup.parse(html);
            long responseTime = System.currentTimeMillis() - startTime;
            
            BeautifulSoupRequest request = BeautifulSoupRequest.builder()
                    .url("html-content")
                    .selectors(selectors)
                    .extractText(true)
                    .extractTitle(true)
                    .extractMeta(true)
                    .build();
            
            return extractBeautifulSoupData(document, request, responseTime);
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.error("HTML 파싱 실패: {}", e.getMessage());
            return BeautifulSoupResponse.error("html-content", e.getMessage(), responseTime);
        }
    }
    
    @Override
    public boolean isParseable(String url) {
        try {
            Jsoup.connect(url)
                    .timeout(5000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .get();
            return true;
        } catch (Exception e) {
            log.warn("BeautifulSoup 파싱 불가능한 URL: {} - {}", url, e.getMessage());
            return false;
        }
    }
    
    private BeautifulSoupResponse extractBeautifulSoupData(Document document, BeautifulSoupRequest request, long responseTime) {
        // 기본 정보 추출
        String title = document.title();
        String text = document.text();
        String html = document.html();
        
        // 세분화된 텍스트 추출
        Map<String, String> segmentedText = extractSegmentedText(document);
        
        // 메타데이터 추출
        Map<String, String> metaTags = extractMetaTags(document);
        
        // 링크 추출
        List<String> links = new ArrayList<>();
        if (request.getExtractLinks() != null && request.getExtractLinks()) {
            links = extractLinks(document, request.getLinkSelectors());
        }
        
        // 이미지 추출
        List<String> images = new ArrayList<>();
        if (request.getExtractImages() != null && request.getExtractImages()) {
            images = extractImages(document, request.getImageSelectors());
        }
        
        // 헤딩 추출
        List<String> headings = new ArrayList<>();
        if (request.getExtractHeadings() != null && request.getExtractHeadings()) {
            headings = extractHeadings(document);
        }
        
        // 선택자 기반 데이터 추출
        Map<String, Object> extractedData = new HashMap<>();
        Map<String, List<String>> selectorResults = new HashMap<>();
        Map<String, String> attributeResults = new HashMap<>();
        List<BeautifulSoupResponse.ElementData> elements = new ArrayList<>();
        
        if (request.getSelectors() != null) {
            for (Map.Entry<String, String> entry : request.getSelectors().entrySet()) {
                String key = entry.getKey();
                String selector = entry.getValue();
                
                try {
                    Elements selectedElements = document.select(selector);
                    List<String> texts = selectedElements.stream()
                            .map(Element::text)
                            .collect(Collectors.toList());
                    
                    selectorResults.put(key, texts);
                    extractedData.put(key, texts.size() == 1 ? texts.get(0) : texts);
                    
                    // 요소 상세 정보 추출
                    for (Element element : selectedElements) {
                        BeautifulSoupResponse.ElementData elementData = BeautifulSoupResponse.ElementData.builder()
                                .tag(element.tagName())
                                .text(element.text())
                                .attributes(element.attributes().asList().stream()
                                        .collect(Collectors.toMap(
                                                org.jsoup.nodes.Attribute::getKey,
                                                org.jsoup.nodes.Attribute::getValue)))
                                .html(element.outerHtml())
                                .build();
                        elements.add(elementData);
                    }
                } catch (Exception e) {
                    log.warn("선택자 처리 실패: {} - {}", selector, e.getMessage());
                    selectorResults.put(key, Collections.emptyList());
                }
            }
        }
        
        // 속성 추출
        if (request.getAttributes() != null) {
            for (Map.Entry<String, String> entry : request.getAttributes().entrySet()) {
                String key = entry.getKey();
                String selector = entry.getValue();
                
                try {
                    Elements selectedElements = document.select(selector);
                    if (!selectedElements.isEmpty()) {
                        String attributeValue = selectedElements.first().attr(key);
                        attributeResults.put(key, attributeValue);
                        extractedData.put("attr_" + key, attributeValue);
                    }
                } catch (Exception e) {
                    log.warn("속성 추출 실패: {} - {}", key, e.getMessage());
                }
            }
        }
        
        log.info("BeautifulSoup 크롤링 완료: {} ({}ms)", request.getUrl(), responseTime);
        
        return BeautifulSoupResponse.builder()
                .url(request.getUrl())
                .title(title)
                .text(text)
                .html(html)
                .extractedData(extractedData)
                .metaTags(metaTags)
                .links(links)
                .images(images)
                .headings(headings)
                .selectorResults(selectorResults)
                .attributeResults(attributeResults)
                .elements(elements)
                .mainText(segmentedText.get("mainText"))
                .description(segmentedText.get("description"))
                .articleText(segmentedText.get("articleText"))
                .navigationText(segmentedText.get("navigationText"))
                .footerText(segmentedText.get("footerText"))
                .crawledAt(LocalDateTime.now())
                .responseTime(responseTime)
                .status("SUCCESS")
                .build();
    }
    
    private Map<String, String> extractMetaTags(Document document) {
        Map<String, String> metaTags = new HashMap<>();
        
        Elements metaElements = document.select("meta");
        for (Element meta : metaElements) {
            String name = meta.attr("name");
            String property = meta.attr("property");
            String content = meta.attr("content");
            
            if (!name.isEmpty() && !content.isEmpty()) {
                metaTags.put(name, content);
            } else if (!property.isEmpty() && !content.isEmpty()) {
                metaTags.put(property, content);
            }
        }
        
        return metaTags;
    }
    
    private List<String> extractLinks(Document document, List<String> linkSelectors) {
        List<String> links = new ArrayList<>();
        
        if (linkSelectors != null && !linkSelectors.isEmpty()) {
            for (String selector : linkSelectors) {
                Elements linkElements = document.select(selector);
                links.addAll(linkElements.stream()
                        .map(element -> element.attr("href"))
                        .filter(href -> !href.isEmpty())
                        .collect(Collectors.toList()));
            }
        } else {
            Elements linkElements = document.select("a[href]");
            links = linkElements.stream()
                    .map(element -> element.attr("href"))
                    .filter(href -> !href.isEmpty())
                    .collect(Collectors.toList());
        }
        
        return links;
    }
    
    private List<String> extractImages(Document document, List<String> imageSelectors) {
        List<String> images = new ArrayList<>();
        
        if (imageSelectors != null && !imageSelectors.isEmpty()) {
            for (String selector : imageSelectors) {
                Elements imageElements = document.select(selector);
                images.addAll(imageElements.stream()
                        .map(element -> element.attr("src"))
                        .filter(src -> !src.isEmpty())
                        .collect(Collectors.toList()));
            }
        } else {
            Elements imageElements = document.select("img[src]");
            images = imageElements.stream()
                    .map(element -> element.attr("src"))
                    .filter(src -> !src.isEmpty())
                    .collect(Collectors.toList());
        }
        
        return images;
    }
    
    private List<String> extractHeadings(Document document) {
        List<String> headings = new ArrayList<>();
        
        for (int i = 1; i <= 6; i++) {
            Elements headingElements = document.select("h" + i);
            headings.addAll(headingElements.stream()
                    .map(Element::text)
                    .filter(text -> !text.isEmpty())
                    .collect(Collectors.toList()));
        }
        
        return headings;
    }
    
    /**
     * 텍스트 내용을 세분화하여 분리
     */
    private Map<String, String> extractSegmentedText(Document document) {
        Map<String, String> segmentedText = new HashMap<>();
        
        // 메인 텍스트 (본문 내용)
        String mainText = extractMainText(document);
        segmentedText.put("mainText", mainText);
        
        // 설명/요약 텍스트
        String description = extractDescription(document);
        segmentedText.put("description", description);
        
        // 기사/본문 텍스트
        String articleText = extractArticleText(document);
        segmentedText.put("articleText", articleText);
        
        // 네비게이션 텍스트
        String navigationText = extractNavigationText(document);
        segmentedText.put("navigationText", navigationText);
        
        // 푸터 텍스트
        String footerText = extractFooterText(document);
        segmentedText.put("footerText", footerText);
        
        return segmentedText;
    }
    
    private String extractMainText(Document document) {
        // 메인 콘텐츠 영역에서 텍스트 추출
        Elements mainElements = document.select("main, .main, .content, .article, .post, .entry");
        if (mainElements.isEmpty()) {
            // 메인 영역이 없으면 body에서 추출
            mainElements = document.select("body");
        }
        
        StringBuilder mainText = new StringBuilder();
        for (Element element : mainElements) {
            // 스크립트, 스타일, 네비게이션 제외
            element.select("script, style, nav, .nav, .navigation, .menu, .sidebar, .footer").remove();
            String text = element.text().trim();
            if (!text.isEmpty()) {
                mainText.append(text).append("\n");
            }
        }
        
        return mainText.toString().trim();
    }
    
    private String extractDescription(Document document) {
        // 메타 description 또는 요약 텍스트
        String metaDescription = document.select("meta[name=description]").attr("content");
        if (!metaDescription.isEmpty()) {
            return metaDescription;
        }
        
        // og:description
        String ogDescription = document.select("meta[property=og:description]").attr("content");
        if (!ogDescription.isEmpty()) {
            return ogDescription;
        }
        
        // 첫 번째 p 태그나 요약 클래스
        Element summaryElement = document.select(".summary, .excerpt, .description, p").first();
        if (summaryElement != null) {
            return summaryElement.text().trim();
        }
        
        return "";
    }
    
    private String extractArticleText(Document document) {
        // 기사 본문 텍스트
        Elements articleElements = document.select("article, .article, .post-content, .entry-content, .content-body");
        if (articleElements.isEmpty()) {
            // 기사 영역이 없으면 본문에서 추출
            articleElements = document.select("body");
        }
        
        StringBuilder articleText = new StringBuilder();
        for (Element element : articleElements) {
            // 헤더, 네비게이션, 푸터, 광고 제외
            element.select("header, nav, .nav, .navigation, .menu, .sidebar, .footer, .ad, .advertisement, .ads").remove();
            String text = element.text().trim();
            if (!text.isEmpty()) {
                articleText.append(text).append("\n");
            }
        }
        
        return articleText.toString().trim();
    }
    
    private String extractNavigationText(Document document) {
        // 네비게이션 텍스트
        Elements navElements = document.select("nav, .nav, .navigation, .menu, .breadcrumb");
        StringBuilder navText = new StringBuilder();
        
        for (Element element : navElements) {
            String text = element.text().trim();
            if (!text.isEmpty()) {
                navText.append(text).append("\n");
            }
        }
        
        return navText.toString().trim();
    }
    
    private String extractFooterText(Document document) {
        // 푸터 텍스트
        Elements footerElements = document.select("footer, .footer, .site-footer");
        StringBuilder footerText = new StringBuilder();
        
        for (Element element : footerElements) {
            String text = element.text().trim();
            if (!text.isEmpty()) {
                footerText.append(text).append("\n");
            }
        }
        
        return footerText.toString().trim();
    }
}
