package com.crawling.domain.beautifulsoup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeautifulSoupResponse {
    
    private String url;
    private String title;
    private String text;
    private String html;
    private Map<String, Object> extractedData;
    private Map<String, String> metaTags;
    private List<String> links;
    private List<String> images;
    private List<String> headings;
    private LocalDateTime crawledAt;
    private Long responseTime;
    private String status;
    private String errorMessage;
    
    // 세분화된 텍스트 필드들
    private String mainText;  // 메인 텍스트 내용
    private String description;  // 설명/요약 텍스트
    private String articleText;  // 기사/본문 텍스트
    private String navigationText;  // 네비게이션 텍스트
    private String footerText;  // 푸터 텍스트
    
    // BeautifulSoup 스타일 응답 데이터
    private Map<String, List<String>> selectorResults;
    private Map<String, String> attributeResults;
    private List<ElementData> elements;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ElementData {
        private String tag;
        private String text;
        private Map<String, String> attributes;
        private String html;
    }
    
    public static BeautifulSoupResponse success(String url, String title, String text, 
                                              Map<String, Object> extractedData, 
                                              Map<String, String> metaTags,
                                              List<String> links,
                                              List<String> images,
                                              List<String> headings,
                                              Long responseTime) {
        return BeautifulSoupResponse.builder()
                .url(url)
                .title(title)
                .text(text)
                .extractedData(extractedData)
                .metaTags(metaTags)
                .links(links)
                .images(images)
                .headings(headings)
                .crawledAt(LocalDateTime.now())
                .responseTime(responseTime)
                .status("SUCCESS")
                .build();
    }
    
    public static BeautifulSoupResponse error(String url, String errorMessage, Long responseTime) {
        return BeautifulSoupResponse.builder()
                .url(url)
                .crawledAt(LocalDateTime.now())
                .responseTime(responseTime)
                .status("ERROR")
                .errorMessage(errorMessage)
                .build();
    }
}
