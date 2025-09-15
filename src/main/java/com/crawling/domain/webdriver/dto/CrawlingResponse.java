package com.crawling.domain.webdriver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlingResponse {
    
    private String url;
    private String title;
    private String content;
    private Map<String, Object> extractedData;
    private Map<String, String> metadata;
    private LocalDateTime crawledAt;
    private Long responseTime;
    private String status;
    private String errorMessage;
    
    public static CrawlingResponse success(String url, String title, String content, 
                                         Map<String, Object> extractedData, 
                                         Map<String, String> metadata, 
                                         Long responseTime) {
        return CrawlingResponse.builder()
                .url(url)
                .title(title)
                .content(content)
                .extractedData(extractedData)
                .metadata(metadata)
                .crawledAt(LocalDateTime.now())
                .responseTime(responseTime)
                .status("SUCCESS")
                .build();
    }
    
    public static CrawlingResponse error(String url, String errorMessage, Long responseTime) {
        return CrawlingResponse.builder()
                .url(url)
                .crawledAt(LocalDateTime.now())
                .responseTime(responseTime)
                .status("ERROR")
                .errorMessage(errorMessage)
                .build();
    }
}
