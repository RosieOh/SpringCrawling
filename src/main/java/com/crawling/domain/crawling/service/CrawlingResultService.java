package com.crawling.domain.crawling.service;

import com.crawling.domain.crawling.entity.CrawlingResult;
import com.crawling.domain.crawling.repository.CrawlingResultRepository;
import com.crawling.domain.webdriver.dto.CrawlingResponse;
import com.crawling.domain.beautifulsoup.dto.BeautifulSoupResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CrawlingResultService {
    
    private final CrawlingResultRepository crawlingResultRepository;
    
    /**
     * WebDriver 크롤링 결과 저장
     */
    public CrawlingResult saveWebDriverResult(CrawlingResponse response, String crawlingType) {
        try {
            CrawlingResult result = CrawlingResult.builder()
                    .url(response.getUrl())
                    .title(response.getTitle())
                    .content(response.getContent())
                    .crawlingType(crawlingType)
                    .status(response.getStatus())
                    .errorMessage(response.getErrorMessage())
                    .responseTime(response.getResponseTime())
                    .build();
            
            // JSON 필드들은 엔티티의 setter 메서드를 사용하여 자동으로 JSON 변환
            if (response.getExtractedData() != null) {
                result.setExtractedData(response.getExtractedData());
            }
            if (response.getMetadata() != null) {
                result.setMetadata(response.getMetadata());
            }
            
            CrawlingResult saved = crawlingResultRepository.save(result);
            log.info("WebDriver 크롤링 결과 저장 완료: ID={}, URL={}", saved.getId(), saved.getUrl());
            return saved;
            
        } catch (Exception e) {
            log.error("WebDriver 크롤링 결과 저장 실패: {}", e.getMessage(), e);
            throw new RuntimeException("크롤링 결과 저장에 실패했습니다.", e);
        }
    }
    
    /**
     * BeautifulSoup 크롤링 결과 저장
     */
    public CrawlingResult saveBeautifulSoupResult(BeautifulSoupResponse response, String crawlingType) {
        try {
            CrawlingResult result = CrawlingResult.builder()
                    .url(response.getUrl())
                    .title(response.getTitle())
                    .content(response.getText())
                    .html(response.getHtml())
                    .mainText(response.getMainText())
                    .description(response.getDescription())
                    .articleText(response.getArticleText())
                    .navigationText(response.getNavigationText())
                    .footerText(response.getFooterText())
                    .crawlingType(crawlingType)
                    .status(response.getStatus())
                    .errorMessage(response.getErrorMessage())
                    .responseTime(response.getResponseTime())
                    .build();
            
            // JSON 필드들은 엔티티의 setter 메서드를 사용하여 자동으로 JSON 변환
            if (response.getExtractedData() != null) {
                result.setExtractedData(response.getExtractedData());
            }
            if (response.getMetaTags() != null) {
                result.setMetadata(response.getMetaTags());
            }
            if (response.getLinks() != null) {
                result.setLinks(response.getLinks());
            }
            if (response.getImages() != null) {
                result.setImages(response.getImages());
            }
            if (response.getHeadings() != null) {
                result.setHeadings(response.getHeadings());
            }
            if (response.getSelectorResults() != null) {
                result.setSelectorResults(response.getSelectorResults());
            }
            if (response.getAttributeResults() != null) {
                result.setAttributeResults(response.getAttributeResults());
            }
            
            // ElementData를 Map으로 변환하여 저장
            if (response.getElements() != null) {
                List<Map<String, Object>> elementsAsMap = response.getElements().stream()
                        .map(element -> {
                            Map<String, Object> elementMap = new HashMap<>();
                            elementMap.put("tag", element.getTag());
                            elementMap.put("text", element.getText());
                            elementMap.put("attributes", element.getAttributes());
                            elementMap.put("html", element.getHtml());
                            return elementMap;
                        })
                        .collect(java.util.stream.Collectors.toList());
                result.setElements(elementsAsMap);
            }
            
            CrawlingResult saved = crawlingResultRepository.save(result);
            log.info("BeautifulSoup 크롤링 결과 저장 완료: ID={}, URL={}", saved.getId(), saved.getUrl());
            return saved;
            
        } catch (Exception e) {
            log.error("BeautifulSoup 크롤링 결과 저장 실패: {}", e.getMessage(), e);
            throw new RuntimeException("크롤링 결과 저장에 실패했습니다.", e);
        }
    }
    
    /**
     * 크롤링 결과 조회 (ID로)
     */
    @Transactional(readOnly = true)
    public Optional<CrawlingResult> findById(Long id) {
        return crawlingResultRepository.findById(id);
    }
    
    /**
     * 모든 크롤링 결과 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public Page<CrawlingResult> findAll(Pageable pageable) {
        return crawlingResultRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    /**
     * URL로 크롤링 결과 조회
     */
    @Transactional(readOnly = true)
    public List<CrawlingResult> findByUrl(String url) {
        return crawlingResultRepository.findByUrl(url);
    }
    
    /**
     * 크롤링 타입으로 결과 조회
     */
    @Transactional(readOnly = true)
    public List<CrawlingResult> findByCrawlingType(String crawlingType) {
        return crawlingResultRepository.findByCrawlingType(crawlingType);
    }
    
    /**
     * 상태로 결과 조회
     */
    @Transactional(readOnly = true)
    public List<CrawlingResult> findByStatus(String status) {
        return crawlingResultRepository.findByStatus(status);
    }
    
    /**
     * 성공한 크롤링 결과만 조회
     */
    @Transactional(readOnly = true)
    public List<CrawlingResult> findSuccessfulResults() {
        return crawlingResultRepository.findByStatusOrderByCreatedAtDesc("SUCCESS");
    }
    
    /**
     * 특정 기간 동안의 크롤링 결과 조회
     */
    @Transactional(readOnly = true)
    public List<CrawlingResult> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return crawlingResultRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    /**
     * 제목에 키워드가 포함된 결과 조회
     */
    @Transactional(readOnly = true)
    public List<CrawlingResult> findByTitleContaining(String keyword) {
        return crawlingResultRepository.findByTitleContaining(keyword);
    }
    
    /**
     * 내용에 키워드가 포함된 결과 조회
     */
    @Transactional(readOnly = true)
    public List<CrawlingResult> findByContentContaining(String keyword) {
        return crawlingResultRepository.findByContentContaining(keyword);
    }
    
    /**
     * 특정 URL의 최신 크롤링 결과 조회
     */
    @Transactional(readOnly = true)
    public List<CrawlingResult> findLatestByUrl(String url) {
        return crawlingResultRepository.findLatestByUrl(url);
    }
    
    /**
     * 크롤링 통계 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getCrawlingStatsByType() {
        List<Object[]> results = crawlingResultRepository.getCrawlingStatsByType();
        return results.stream()
                .collect(java.util.stream.Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));
    }
    
    /**
     * 상태별 통계 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getCrawlingStatsByStatus() {
        List<Object[]> results = crawlingResultRepository.getCrawlingStatsByStatus();
        return results.stream()
                .collect(java.util.stream.Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));
    }
    
    /**
     * 일별 크롤링 통계 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getCrawlingStatsByDate() {
        List<Object[]> results = crawlingResultRepository.getCrawlingStatsByDate();
        return results.stream()
                .collect(java.util.stream.Collectors.toMap(
                        result -> result[0].toString(),
                        result -> (Long) result[1]
                ));
    }
    
    /**
     * 평균 응답 시간 조회
     */
    @Transactional(readOnly = true)
    public Double getAverageResponseTime() {
        return crawlingResultRepository.getAverageResponseTime();
    }
    
    /**
     * 가장 많이 크롤링된 URL 조회
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getMostCrawledUrls() {
        List<Object[]> results = crawlingResultRepository.getMostCrawledUrls();
        return results.stream()
                .limit(10) // 상위 10개만
                .collect(java.util.stream.Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));
    }
    
    /**
     * 크롤링 결과 삭제
     */
    public void deleteById(Long id) {
        crawlingResultRepository.deleteById(id);
        log.info("크롤링 결과 삭제 완료: ID={}", id);
    }
    
    /**
     * 특정 URL의 모든 크롤링 결과 삭제
     */
    public void deleteByUrl(String url) {
        crawlingResultRepository.deleteByUrl(url);
        log.info("URL의 모든 크롤링 결과 삭제 완료: URL={}", url);
    }
    
    /**
     * 오래된 크롤링 결과 삭제
     */
    public void deleteOldResults(LocalDateTime cutoffDate) {
        crawlingResultRepository.deleteByCreatedAtBefore(cutoffDate);
        log.info("오래된 크롤링 결과 삭제 완료: 기준일={}", cutoffDate);
    }
    
    /**
     * 전체 크롤링 결과 수 조회
     */
    @Transactional(readOnly = true)
    public long getTotalCount() {
        return crawlingResultRepository.count();
    }
    
    /**
     * 성공한 크롤링 결과 수 조회
     */
    @Transactional(readOnly = true)
    public long getSuccessCount() {
        return crawlingResultRepository.findByStatus("SUCCESS").size();
    }
    
    /**
     * 실패한 크롤링 결과 수 조회
     */
    @Transactional(readOnly = true)
    public long getErrorCount() {
        return crawlingResultRepository.findByStatus("ERROR").size();
    }
}
