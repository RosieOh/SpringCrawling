package com.crawling.domain.beautifulsoup.service;

import com.crawling.domain.beautifulsoup.dto.BeautifulSoupRequest;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupResponse;

import java.util.List;
import java.util.Map;

public interface BeautifulSoupService {
    
    /**
     * BeautifulSoup 스타일로 웹 페이지를 크롤링합니다.
     * 
     * @param request 크롤링 요청 정보
     * @return 크롤링 결과
     */
    BeautifulSoupResponse parse(BeautifulSoupRequest request);
    
    /**
     * 여러 URL을 동시에 크롤링합니다.
     * 
     * @param requests 크롤링 요청 목록
     * @return 크롤링 결과 목록
     */
    List<BeautifulSoupResponse> parseMultiple(List<BeautifulSoupRequest> requests);
    
    /**
     * HTML 문자열을 직접 파싱합니다.
     * 
     * @param html HTML 문자열
     * @param selectors 선택자 맵
     * @return 파싱 결과
     */
    BeautifulSoupResponse parseHtml(String html, Map<String, String> selectors);
    
    /**
     * 크롤링 가능 여부를 확인합니다.
     * 
     * @param url 확인할 URL
     * @return 크롤링 가능 여부
     */
    boolean isParseable(String url);
}
