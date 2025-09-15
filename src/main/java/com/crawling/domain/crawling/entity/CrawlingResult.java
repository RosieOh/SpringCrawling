package com.crawling.domain.crawling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "crawling_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlingResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String url;
    
    @Column(length = 500)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(columnDefinition = "TEXT")
    private String html;
    
    // 텍스트 내용을 세분화한 컬럼들
    @Column(columnDefinition = "TEXT")
    private String mainText;  // 메인 텍스트 내용
    
    @Column(columnDefinition = "TEXT")
    private String description;  // 설명/요약 텍스트
    
    @Column(columnDefinition = "TEXT")
    private String articleText;  // 기사/본문 텍스트
    
    @Column(columnDefinition = "TEXT")
    private String navigationText;  // 네비게이션 텍스트
    
    @Column(columnDefinition = "TEXT")
    private String footerText;  // 푸터 텍스트
    
    @Column(length = 50)
    private String crawlingType;
    
    @Column(length = 20)
    private String status;
    
    @Column(length = 1000)
    private String errorMessage;
    
    @Column
    private Long responseTime;
    
    @Column(columnDefinition = "TEXT")
    private String extractedDataJson;
    
    @Column(columnDefinition = "TEXT")
    private String metadataJson;
    
    @Column(columnDefinition = "TEXT")
    private String linksJson;
    
    @Column(columnDefinition = "TEXT")
    private String imagesJson;
    
    @Column(columnDefinition = "TEXT")
    private String headingsJson;
    
    @Column(columnDefinition = "TEXT")
    private String selectorResultsJson;
    
    @Column(columnDefinition = "TEXT")
    private String attributeResultsJson;
    
    @Column(columnDefinition = "TEXT")
    private String elementsJson;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // JSON 필드들을 Map으로 변환하는 헬퍼 메서드들
    public void setExtractedData(Map<String, Object> extractedData) {
        this.extractedDataJson = convertMapToJson(extractedData);
    }
    
    public Map<String, Object> getExtractedData() {
        return convertJsonToMap(this.extractedDataJson);
    }
    
    public void setMetadata(Map<String, String> metadata) {
        this.metadataJson = convertMapToJson(metadata);
    }
    
    public Map<String, String> getMetadata() {
        return convertJsonToMap(this.metadataJson);
    }
    
    public void setLinks(java.util.List<String> links) {
        this.linksJson = convertListToJson(links);
    }
    
    public java.util.List<String> getLinks() {
        return convertJsonToList(this.linksJson);
    }
    
    public void setImages(java.util.List<String> images) {
        this.imagesJson = convertListToJson(images);
    }
    
    public java.util.List<String> getImages() {
        return convertJsonToList(this.imagesJson);
    }
    
    public void setHeadings(java.util.List<String> headings) {
        this.headingsJson = convertListToJson(headings);
    }
    
    public java.util.List<String> getHeadings() {
        return convertJsonToList(this.headingsJson);
    }
    
    public void setSelectorResults(Map<String, java.util.List<String>> selectorResults) {
        this.selectorResultsJson = convertMapToJson(selectorResults);
    }
    
    public Map<String, java.util.List<String>> getSelectorResults() {
        return convertJsonToMap(this.selectorResultsJson);
    }
    
    public void setAttributeResults(Map<String, String> attributeResults) {
        this.attributeResultsJson = convertMapToJson(attributeResults);
    }
    
    public Map<String, String> getAttributeResults() {
        return convertJsonToMap(this.attributeResultsJson);
    }
    
    public void setElements(java.util.List<Map<String, Object>> elements) {
        this.elementsJson = convertListToJson(elements);
    }
    
    public java.util.List<Map<String, Object>> getElements() {
        return convertJsonToList(this.elementsJson);
    }
    
    // JSON 변환 헬퍼 메서드들
    private String convertMapToJson(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            return null;
        }
    }
    
    private String convertListToJson(java.util.List<?> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> T convertJsonToMap(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return (T) mapper.readValue(json, Map.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> T convertJsonToList(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return (T) mapper.readValue(json, java.util.List.class);
        } catch (Exception e) {
            return null;
        }
    }
}
