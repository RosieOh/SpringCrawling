package com.crawling.domain.webdriver.dto;

import com.crawling.domain.webdriver.enums.CrawlingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlingRequest {
    
    @NotBlank(message = "URL은 필수입니다")
    private String url;
    
    @NotNull(message = "크롤링 타입은 필수입니다")
    private CrawlingType type;
    
    private Map<String, String> selectors;
    private Map<String, String> headers;
    private Integer timeout;
    private Boolean useJavaScript;
    private String userAgent;
}
