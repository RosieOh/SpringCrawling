package com.crawling.core.jpa.aggregate;

import com.tspoon.domain.badge.entity.UserBadge;
import com.tspoon.domain.board.entity.Comment;
import com.tspoon.domain.board.entity.Post;
import com.tspoon.domain.study.entity.StudyGroup;
import com.tspoon.domain.study.entity.StudyMember;
import com.tspoon.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Aggregate Factory
 * Aggregate 객체를 생성하고 관리하는 팩토리 클래스입니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregateFactory {
    
    /**
     * 사용자 Aggregate를 생성합니다.
     * 
     * @param user 사용자 엔티티
     * @return 사용자 Aggregate
     */
    public UserAggregate createUserAggregate(User user) {
        log.debug("Creating UserAggregate for user: {}", user.getId());
        return new UserAggregate(user);
    }
    
    /**
     * 사용자 Aggregate를 생성합니다. (관련 엔티티 포함)
     * 
     * @param user 사용자 엔티티
     * @param badges 사용자 뱃지 목록
     * @param studyMemberships 스터디 멤버십 목록
     * @return 사용자 Aggregate
     */
    public UserAggregate createUserAggregate(User user, List<UserBadge> badges, List<StudyMember> studyMemberships) {
        log.debug("Creating UserAggregate for user: {} with {} badges and {} study memberships", 
                user.getId(), badges.size(), studyMemberships.size());
        return new UserAggregate(user, badges, studyMemberships);
    }
    
    /**
     * 게시판 Aggregate를 생성합니다.
     * 
     * @param post 게시글 엔티티
     * @return 게시판 Aggregate
     */
    public BoardAggregate createBoardAggregate(Post post) {
        log.debug("Creating BoardAggregate for post: {}", post.getId());
        return new BoardAggregate(post);
    }
    
    /**
     * 게시판 Aggregate를 생성합니다. (댓글 포함)
     * 
     * @param post 게시글 엔티티
     * @param comments 댓글 목록
     * @return 게시판 Aggregate
     */
    public BoardAggregate createBoardAggregate(Post post, List<Comment> comments) {
        log.debug("Creating BoardAggregate for post: {} with {} comments", post.getId(), comments.size());
        return new BoardAggregate(post, comments);
    }
    
    /**
     * 스터디 Aggregate를 생성합니다.
     * 
     * @param studyGroup 스터디 그룹 엔티티
     * @return 스터디 Aggregate
     */
    public StudyAggregate createStudyAggregate(StudyGroup studyGroup) {
        log.debug("Creating StudyAggregate for study group: {}", studyGroup.getId());
        return new StudyAggregate(studyGroup);
    }
    
    /**
     * 스터디 Aggregate를 생성합니다. (멤버 포함)
     * 
     * @param studyGroup 스터디 그룹 엔티티
     * @param members 멤버 목록
     * @return 스터디 Aggregate
     */
    public StudyAggregate createStudyAggregate(StudyGroup studyGroup, List<StudyMember> members) {
        log.debug("Creating StudyAggregate for study group: {} with {} members", 
                studyGroup.getId(), members.size());
        return new StudyAggregate(studyGroup, members);
    }
    
    /**
     * Aggregate 타입을 확인합니다.
     * 
     * @param aggregate Aggregate 객체
     * @return Aggregate 타입
     */
    public AggregateType getAggregateType(Object aggregate) {
        if (aggregate instanceof UserAggregate) {
            return AggregateType.USER;
        } else if (aggregate instanceof BoardAggregate) {
            return AggregateType.BOARD;
        } else if (aggregate instanceof StudyAggregate) {
            return AggregateType.STUDY;
        } else {
            return AggregateType.UNKNOWN;
        }
    }
    
    /**
     * Aggregate 타입 열거형
     */
    public enum AggregateType {
        USER, BOARD, STUDY, UNKNOWN
    }
}
