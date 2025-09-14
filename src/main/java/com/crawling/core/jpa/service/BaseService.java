package com.crawling.core.jpa.service;

import com.tspoon.core.jpa.entity.BaseEntity;
import com.tspoon.core.jpa.repository.BaseRepository;
import com.tspoon.global.error.ErrorCode;
import com.tspoon.global.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 모든 서비스의 기본 클래스
 * 공통적인 CRUD 기능을 제공합니다.
 * 
 * @param <T> 엔티티 타입
 * @param <ID> ID 타입
 * @author tspoon
 * @version 1.0
 */
@Slf4j
public abstract class BaseService<T extends BaseEntity, ID> {
    
    protected final BaseRepository<T, ID> repository;
    
    protected BaseService(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }
    
    /**
     * 엔티티를 저장합니다.
     * 
     * @param entity 저장할 엔티티
     * @return 저장된 엔티티
     */
    @Transactional
    public T save(T entity) {
        log.debug("Saving entity: {}", entity.getClass().getSimpleName());
        return repository.save(entity);
    }
    
    /**
     * 엔티티 목록을 저장합니다.
     * 
     * @param entities 저장할 엔티티 목록
     * @return 저장된 엔티티 목록
     */
    @Transactional
    public List<T> saveAll(List<T> entities) {
        log.debug("Saving {} entities", entities.size());
        return repository.saveAll(entities);
    }
    
    /**
     * ID로 엔티티를 조회합니다.
     * 
     * @param id 엔티티 ID
     * @return 엔티티 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        return repository.findByIdAndNotDeleted(id);
    }
    
    /**
     * ID로 엔티티를 조회합니다. (삭제된 것 포함)
     * 
     * @param id 엔티티 ID
     * @return 엔티티 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<T> findByIdIncludingDeleted(ID id) {
        return repository.findById(id);
    }
    
    /**
     * 모든 엔티티를 조회합니다.
     * 
     * @return 엔티티 목록
     */
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAllNotDeleted();
    }
    
    /**
     * 페이징된 엔티티 목록을 조회합니다.
     * 
     * @param pageable 페이지 정보
     * @return 엔티티 페이지
     */
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    /**
     * 엔티티를 논리 삭제합니다.
     * 
     * @param id 엔티티 ID
     * @param deletedBy 삭제자
     */
    @Transactional
    public void softDelete(ID id, String deletedBy) {
        T entity = repository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "엔티티를 찾을 수 없습니다."));
        
        entity.delete(deletedBy);
        repository.save(entity);
        
        log.info("Soft deleted entity with id: {} by: {}", id, deletedBy);
    }
    
    /**
     * 엔티티를 복구합니다.
     * 
     * @param id 엔티티 ID
     */
    @Transactional
    public void restore(ID id) {
        T entity = repository.findByIdAndDeleted(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "삭제된 엔티티를 찾을 수 없습니다."));
        
        entity.restore();
        repository.save(entity);
        
        log.info("Restored entity with id: {}", id);
    }
    
    /**
     * 엔티티를 영구 삭제합니다.
     * 
     * @param id 엔티티 ID
     */
    @Transactional
    public void hardDelete(ID id) {
        T entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "엔티티를 찾을 수 없습니다."));
        
        repository.delete(entity);
        
        log.info("Hard deleted entity with id: {}", id);
    }
    
    /**
     * 엔티티 존재 여부를 확인합니다.
     * 
     * @param id 엔티티 ID
     * @return 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return repository.findByIdAndNotDeleted(id).isPresent();
    }
    
    /**
     * 엔티티 개수를 조회합니다.
     * 
     * @return 엔티티 개수
     */
    @Transactional(readOnly = true)
    public long count() {
        return repository.countNotDeleted();
    }
    
    /**
     * 삭제된 엔티티 개수를 조회합니다.
     * 
     * @return 삭제된 엔티티 개수
     */
    @Transactional(readOnly = true)
    public long countDeleted() {
        return repository.countDeleted();
    }
    
    /**
     * 삭제된 엔티티 목록을 조회합니다.
     * 
     * @return 삭제된 엔티티 목록
     */
    @Transactional(readOnly = true)
    public List<T> findAllDeleted() {
        return repository.findAllDeleted();
    }
    
    /**
     * 엔티티를 업데이트합니다.
     * 
     * @param entity 업데이트할 엔티티
     * @return 업데이트된 엔티티
     */
    @Transactional
    public T update(T entity) {
        log.debug("Updating entity: {}", entity.getClass().getSimpleName());
        return repository.save(entity);
    }
}
