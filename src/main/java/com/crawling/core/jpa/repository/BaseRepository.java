package com.crawling.core.jpa.repository;

import com.tspoon.core.jpa.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 모든 Repository의 기본 인터페이스
 * 공통적인 쿼리 메서드들을 제공합니다.
 * 
 * @param <T> 엔티티 타입
 * @param <ID> ID 타입
 * @author tspoon
 * @version 1.0
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {
    
    /**
     * 삭제되지 않은 엔티티를 ID로 조회합니다.
     * 
     * @param id 엔티티 ID
     * @return 삭제되지 않은 엔티티
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.isDeleted = false")
    Optional<T> findByIdAndNotDeleted(@Param("id") ID id);
    
    /**
     * 삭제되지 않은 모든 엔티티를 조회합니다.
     * 
     * @return 삭제되지 않은 엔티티 목록
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.isDeleted = false")
    List<T> findAllNotDeleted();
    
    /**
     * 삭제된 엔티티를 ID로 조회합니다.
     * 
     * @param id 엔티티 ID
     * @return 삭제된 엔티티
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.isDeleted = true")
    Optional<T> findByIdAndDeleted(@Param("id") ID id);
    
    /**
     * 삭제된 모든 엔티티를 조회합니다.
     * 
     * @return 삭제된 엔티티 목록
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.isDeleted = true")
    List<T> findAllDeleted();
    
    /**
     * 엔티티를 논리 삭제합니다.
     * 
     * @param id 엔티티 ID
     * @param deletedBy 삭제자
     * @return 삭제된 행 수
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = true, e.deletedAt = :deletedAt, e.deletedBy = :deletedBy WHERE e.id = :id")
    int softDeleteById(@Param("id") ID id, @Param("deletedAt") LocalDateTime deletedAt, @Param("deletedBy") String deletedBy);
    
    /**
     * 엔티티를 복구합니다.
     * 
     * @param id 엔티티 ID
     * @return 복구된 행 수
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = false, e.deletedAt = null, e.deletedBy = null WHERE e.id = :id")
    int restoreById(@Param("id") ID id);
    
    /**
     * 삭제된 엔티티를 영구 삭제합니다.
     * 
     * @param id 엔티티 ID
     * @return 삭제된 행 수
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id = :id AND e.isDeleted = true")
    int hardDeleteById(@Param("id") ID id);
    
    /**
     * 삭제 여부를 확인합니다.
     * 
     * @param id 엔티티 ID
     * @return 삭제 여부
     */
    @Query("SELECT e.isDeleted FROM #{#entityName} e WHERE e.id = :id")
    Optional<Boolean> isDeletedById(@Param("id") ID id);
    
    /**
     * 삭제되지 않은 엔티티의 개수를 조회합니다.
     * 
     * @return 삭제되지 않은 엔티티 개수
     */
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.isDeleted = false")
    long countNotDeleted();
    
    /**
     * 삭제된 엔티티의 개수를 조회합니다.
     * 
     * @return 삭제된 엔티티 개수
     */
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.isDeleted = true")
    long countDeleted();
}
