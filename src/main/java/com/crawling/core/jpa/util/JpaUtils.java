package com.crawling.core.jpa.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * JPA 유틸리티 클래스
 * JPA 관련 공통 기능을 제공합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Component
public class JpaUtils {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * 네이티브 쿼리를 실행합니다.
     * 
     * @param sql 네이티브 SQL 쿼리
     * @param params 쿼리 파라미터
     * @return 실행 결과
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> executeNativeQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        
        if (params != null) {
            params.forEach(query::setParameter);
        }
        
        return query.getResultList();
    }
    
    /**
     * JPQL 쿼리를 실행합니다.
     * 
     * @param jpql JPQL 쿼리
     * @param params 쿼리 파라미터
     * @return 실행 결과
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> executeJpqlQuery(String jpql, Map<String, Object> params) {
        Query query = entityManager.createQuery(jpql);
        
        if (params != null) {
            params.forEach(query::setParameter);
        }
        
        return query.getResultList();
    }
    
    /**
     * 단일 결과를 반환하는 네이티브 쿼리를 실행합니다.
     * 
     * @param sql 네이티브 SQL 쿼리
     * @param params 쿼리 파라미터
     * @return 실행 결과
     */
    public Object executeNativeQueryForSingleResult(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        
        if (params != null) {
            params.forEach(query::setParameter);
        }
        
        return query.getSingleResult();
    }
    
    /**
     * 단일 결과를 반환하는 JPQL 쿼리를 실행합니다.
     * 
     * @param jpql JPQL 쿼리
     * @param params 쿼리 파라미터
     * @return 실행 결과
     */
    public Object executeJpqlQueryForSingleResult(String jpql, Map<String, Object> params) {
        Query query = entityManager.createQuery(jpql);
        
        if (params != null) {
            params.forEach(query::setParameter);
        }
        
        return query.getSingleResult();
    }
    
    /**
     * 업데이트 쿼리를 실행합니다.
     * 
     * @param sql 업데이트 SQL 쿼리
     * @param params 쿼리 파라미터
     * @return 영향받은 행 수
     */
    public int executeUpdateQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        
        if (params != null) {
            params.forEach(query::setParameter);
        }
        
        return query.executeUpdate();
    }
    
    /**
     * 엔티티를 플러시합니다.
     */
    public void flush() {
        entityManager.flush();
    }
    
    /**
     * 엔티티를 클리어합니다.
     */
    public void clear() {
        entityManager.clear();
    }
    
    /**
     * 엔티티를 새로고침합니다.
     * 
     * @param entity 새로고침할 엔티티
     */
    public void refresh(Object entity) {
        entityManager.refresh(entity);
    }
    
    /**
     * 엔티티를 분리합니다.
     * 
     * @param entity 분리할 엔티티
     */
    public void detach(Object entity) {
        entityManager.detach(entity);
    }
    
    /**
     * 엔티티가 관리되는지 확인합니다.
     * 
     * @param entity 확인할 엔티티
     * @return 관리 여부
     */
    public boolean contains(Object entity) {
        return entityManager.contains(entity);
    }
    
    /**
     * 엔티티를 병합합니다.
     * 
     * @param entity 병합할 엔티티
     * @param <T> 엔티티 타입
     * @return 병합된 엔티티
     */
    @SuppressWarnings("unchecked")
    public <T> T merge(T entity) {
        return (T) entityManager.merge(entity);
    }
    
    /**
     * 엔티티를 제거합니다.
     * 
     * @param entity 제거할 엔티티
     */
    public void remove(Object entity) {
        entityManager.remove(entity);
    }
    
    /**
     * 엔티티를 영속화합니다.
     * 
     * @param entity 영속화할 엔티티
     */
    public void persist(Object entity) {
        entityManager.persist(entity);
    }
}
