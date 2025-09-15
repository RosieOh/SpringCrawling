package com.crawling.domain.beautifulsoup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeautifulSoupRequest {
    
    @NotBlank(message = "URL은 필수입니다")
    private String url;
    
    private String userAgent;
    private Integer timeout;
    private Map<String, String> headers;
    private List<String> allowedDomains;
    private List<String> disallowedPaths;
    private Boolean followRedirects;
    private Boolean ignoreHttpErrors;
    private String encoding;
    
    // BeautifulSoup 스타일 선택자 설정
    private Map<String, String> selectors;
    private Map<String, String> attributes;
    private List<String> textSelectors;
    private List<String> linkSelectors;
    private List<String> imageSelectors;
    
    // 데이터 추출 설정
    private Boolean extractText;
    private Boolean extractLinks;
    private Boolean extractImages;
    private Boolean extractMeta;
    private Boolean extractTitle;
    private Boolean extractHeadings;
    
    public static BeautifulSoupRequest simple(String url) {
        return BeautifulSoupRequest.builder()
                .url(url)
                .extractText(true)
                .extractTitle(true)
                .extractMeta(true)
                .followRedirects(true)
                .ignoreHttpErrors(false)
                .build();
    }
    
    public static BeautifulSoupRequest withSelectors(String url, Map<String, String> selectors) {
        return BeautifulSoupRequest.builder()
                .url(url)
                .selectors(selectors)
                .extractText(true)
                .extractTitle(true)
                .followRedirects(true)
                .build();
    }
}
