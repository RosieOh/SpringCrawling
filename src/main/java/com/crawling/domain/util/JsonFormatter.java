package com.crawling.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonFormatter {
    
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    /**
     * JSON 문자열을 예쁘게 포맷팅합니다.
     */
    public static String prettyFormat(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return "";
        }
        
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            log.warn("JSON 포맷팅 실패: {}", e.getMessage());
            return jsonString; // 원본 문자열 반환
        }
    }
    
    /**
     * JSON 문자열이 유효한지 확인합니다.
     */
    public static boolean isValidJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return false;
        }
        
        try {
            objectMapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
    
    /**
     * JSON 문자열을 압축합니다 (한 줄로).
     */
    public static String compactFormat(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return "";
        }
        
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            log.warn("JSON 압축 실패: {}", e.getMessage());
            return jsonString; // 원본 문자열 반환
        }
    }
    
    /**
     * 객체를 JSON 문자열로 변환합니다.
     */
    public static String convertObjectToJson(Object object) {
        if (object == null) {
            return null;
        }
        
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("객체를 JSON으로 변환 실패: {}", e.getMessage());
            return null;
        }
    }
}
