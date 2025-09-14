package com.crawling.core.jpa.aggregate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Aggregate Service
 * Aggregate 객체의 생명주기를 관리하는 서비스입니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateService {
    
    private final AggregateFactory aggregateFactory;
    
    /**
     * 사용자 Aggregate를 저장합니다.
     * 
     * @param aggregate 사용자 Aggregate
     * @return 저장된 사용자 Aggregate
     */
    @Transactional
    public UserAggregate saveUserAggregate(UserAggregate aggregate) {
        log.debug("Saving UserAggregate for user: {}", aggregate.getUser().getId());
        
        // 사용자 정보 저장
        // TODO: 실제 저장 로직 구현
        
        return aggregate;
    }
    
    /**
     * 게시판 Aggregate를 저장합니다.
     * 
     * @param aggregate 게시판 Aggregate
     * @return 저장된 게시판 Aggregate
     */
    @Transactional
    public BoardAggregate saveBoardAggregate(BoardAggregate aggregate) {
        log.debug("Saving BoardAggregate for post: {}", aggregate.getPost().getId());
        
        // 게시글과 댓글 저장
        // TODO: 실제 저장 로직 구현
        
        return aggregate;
    }
    
    /**
     * 스터디 Aggregate를 저장합니다.
     * 
     * @param aggregate 스터디 Aggregate
     * @return 저장된 스터디 Aggregate
     */
    @Transactional
    public StudyAggregate saveStudyAggregate(StudyAggregate aggregate) {
        log.debug("Saving StudyAggregate for study group: {}", aggregate.getStudyGroup().getId());
        
        // 스터디 그룹과 멤버 저장
        // TODO: 실제 저장 로직 구현
        
        return aggregate;
    }
    
    /**
     * Aggregate를 삭제합니다.
     * 
     * @param aggregate 삭제할 Aggregate
     * @param deletedBy 삭제자
     */
    @Transactional
    public void deleteAggregate(Object aggregate, String deletedBy) {
        AggregateFactory.AggregateType type = aggregateFactory.getAggregateType(aggregate);
        
        switch (type) {
            case USER -> {
                UserAggregate userAggregate = (UserAggregate) aggregate;
                userAggregate.deleteUser(deletedBy);
                log.info("Deleted UserAggregate for user: {}", userAggregate.getUser().getId());
            }
            case BOARD -> {
                BoardAggregate boardAggregate = (BoardAggregate) aggregate;
                boardAggregate.deletePost(deletedBy);
                log.info("Deleted BoardAggregate for post: {}", boardAggregate.getPost().getId());
            }
            case STUDY -> {
                StudyAggregate studyAggregate = (StudyAggregate) aggregate;
                studyAggregate.deleteStudy(deletedBy);
                log.info("Deleted StudyAggregate for study group: {}", studyAggregate.getStudyGroup().getId());
            }
            default -> {
                log.warn("Unknown aggregate type: {}", type);
                throw new IllegalArgumentException("알 수 없는 Aggregate 타입입니다.");
            }
        }
    }
    
    /**
     * Aggregate의 일관성을 검증합니다.
     * 
     * @param aggregate 검증할 Aggregate
     * @return 일관성 검증 결과
     */
    public boolean validateAggregate(Object aggregate) {
        AggregateFactory.AggregateType type = aggregateFactory.getAggregateType(aggregate);
        
        try {
            switch (type) {
                case USER -> {
                    UserAggregate userAggregate = (UserAggregate) aggregate;
                    return validateUserAggregate(userAggregate);
                }
                case BOARD -> {
                    BoardAggregate boardAggregate = (BoardAggregate) aggregate;
                    return validateBoardAggregate(boardAggregate);
                }
                case STUDY -> {
                    StudyAggregate studyAggregate = (StudyAggregate) aggregate;
                    return validateStudyAggregate(studyAggregate);
                }
                default -> {
                    log.warn("Unknown aggregate type: {}", type);
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("Error validating aggregate: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 사용자 Aggregate 일관성을 검증합니다.
     */
    private boolean validateUserAggregate(UserAggregate aggregate) {
        // 사용자 정보 검증
        if (aggregate.getUser() == null) {
            return false;
        }
        
        // 뱃지 정보 검증
        if (aggregate.getBadges() != null) {
            for (var badge : aggregate.getBadges()) {
                if (badge.getUser() == null || !badge.getUser().getId().equals(aggregate.getUser().getId())) {
                    return false;
                }
            }
        }
        
        // 스터디 멤버십 검증
        if (aggregate.getStudyMemberships() != null) {
            for (var membership : aggregate.getStudyMemberships()) {
                if (membership.getUser() == null || !membership.getUser().getId().equals(aggregate.getUser().getId())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 게시판 Aggregate 일관성을 검증합니다.
     */
    private boolean validateBoardAggregate(BoardAggregate aggregate) {
        // 게시글 정보 검증
        if (aggregate.getPost() == null) {
            return false;
        }
        
        // 댓글 정보 검증
        if (aggregate.getComments() != null) {
            for (var comment : aggregate.getComments()) {
                if (comment.getPost() == null || !comment.getPost().getId().equals(aggregate.getPost().getId())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 스터디 Aggregate 일관성을 검증합니다.
     */
    private boolean validateStudyAggregate(StudyAggregate aggregate) {
        // 스터디 그룹 정보 검증
        if (aggregate.getStudyGroup() == null) {
            return false;
        }
        
        // 멤버 정보 검증
        if (aggregate.getMembers() != null) {
            for (var member : aggregate.getMembers()) {
                if (member.getStudyGroup() == null || !member.getStudyGroup().getId().equals(aggregate.getStudyGroup().getId())) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
