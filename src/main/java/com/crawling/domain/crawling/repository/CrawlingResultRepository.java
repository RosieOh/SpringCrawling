package com.crawling.domain.crawling.repository;

import com.crawling.domain.crawling.entity.CrawlingResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CrawlingResultRepository extends JpaRepository<CrawlingResult, Long> {
    
    /**
     * URL로 크롤링 결과 조회
     */
    List<CrawlingResult> findByUrl(String url);
    
    /**
     * 크롤링 타입으로 결과 조회
     */
    List<CrawlingResult> findByCrawlingType(String crawlingType);
    
    /**
     * 상태로 결과 조회
     */
    List<CrawlingResult> findByStatus(String status);
    
    /**
     * 성공한 크롤링 결과만 조회
     */
    List<CrawlingResult> findByStatusOrderByCreatedAtDesc(String status);
    
    /**
     * 최근 크롤링 결과 조회 (페이징)
     */
    Page<CrawlingResult> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * 특정 기간 동안의 크롤링 결과 조회
     */
    List<CrawlingResult> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * URL과 크롤링 타입으로 결과 조회
     */
    List<CrawlingResult> findByUrlAndCrawlingType(String url, String crawlingType);
    
    /**
     * 제목에 특정 키워드가 포함된 결과 조회
     */
    @Query("SELECT c FROM CrawlingResult c WHERE c.title LIKE %:keyword%")
    List<CrawlingResult> findByTitleContaining(@Param("keyword") String keyword);
    
    /**
     * 내용에 특정 키워드가 포함된 결과 조회
     */
    @Query("SELECT c FROM CrawlingResult c WHERE c.content LIKE %:keyword%")
    List<CrawlingResult> findByContentContaining(@Param("keyword") String keyword);
    
    /**
     * 특정 URL의 최신 크롤링 결과 조회
     */
    @Query("SELECT c FROM CrawlingResult c WHERE c.url = :url ORDER BY c.createdAt DESC")
    List<CrawlingResult> findLatestByUrl(@Param("url") String url);
    
    /**
     * 크롤링 통계 조회
     */
    @Query("SELECT c.crawlingType, COUNT(c) FROM CrawlingResult c GROUP BY c.crawlingType")
    List<Object[]> getCrawlingStatsByType();
    
    /**
     * 상태별 통계 조회
     */
    @Query("SELECT c.status, COUNT(c) FROM CrawlingResult c GROUP BY c.status")
    List<Object[]> getCrawlingStatsByStatus();
    
    /**
     * 일별 크롤링 통계 조회
     */
    @Query("SELECT DATE(c.createdAt), COUNT(c) FROM CrawlingResult c GROUP BY DATE(c.createdAt) ORDER BY DATE(c.createdAt) DESC")
    List<Object[]> getCrawlingStatsByDate();
    
    /**
     * 평균 응답 시간 조회
     */
    @Query("SELECT AVG(c.responseTime) FROM CrawlingResult c WHERE c.status = 'SUCCESS'")
    Double getAverageResponseTime();
    
    /**
     * 가장 많이 크롤링된 URL 조회
     */
    @Query("SELECT c.url, COUNT(c) FROM CrawlingResult c GROUP BY c.url ORDER BY COUNT(c) DESC")
    List<Object[]> getMostCrawledUrls();
    
    /**
     * 오래된 크롤링 결과 삭제
     */
    void deleteByCreatedAtBefore(LocalDateTime cutoffDate);
    
    /**
     * 특정 URL의 모든 크롤링 결과 삭제
     */
    void deleteByUrl(String url);
}
