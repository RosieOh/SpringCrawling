package com.crawling.core.jpa.aggregate;

import com.tspoon.domain.badge.entity.UserBadge;
import com.tspoon.domain.study.entity.StudyMember;
import com.tspoon.domain.user.entity.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 Aggregate
 * 사용자와 관련된 모든 엔티티를 하나의 단위로 관리합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Getter
public class UserAggregate {
    
    private final User user;
    private final List<UserBadge> badges;
    private final List<StudyMember> studyMemberships;
    
    public UserAggregate(User user) {
        this.user = user;
        this.badges = new ArrayList<>();
        this.studyMemberships = new ArrayList<>();
    }
    
    public UserAggregate(User user, List<UserBadge> badges, List<StudyMember> studyMemberships) {
        this.user = user;
        this.badges = badges != null ? badges : new ArrayList<>();
        this.studyMemberships = studyMemberships != null ? studyMemberships : new ArrayList<>();
    }
    
    /**
     * 사용자 정보를 업데이트합니다.
     * 
     * @param nickname 닉네임
     * @param email 이메일
     * @param phoneNumber 전화번호
     */
    public void updateUserInfo(String nickname, String email, String phoneNumber) {
        if (nickname != null && !nickname.trim().isEmpty()) {
            this.user.updateNickname(nickname);
        }
        if (email != null && !email.trim().isEmpty()) {
            this.user.updateEmail(email);
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            this.user.updatePhoneNumber(phoneNumber);
        }
    }
    
    /**
     * 사용자 프로필을 업데이트합니다.
     * 
     * @param profileImage 프로필 이미지
     * @param introduction 자기소개
     * @param region 지역
     * @param grade 학년
     */
    public void updateProfile(String profileImage, String introduction, String region, String grade) {
        if (profileImage != null) {
            this.user.updateProfileImage(profileImage);
        }
        if (introduction != null) {
            this.user.updateIntroduction(introduction);
        }
        if (grade != null) {
            this.user.updateGrade(grade);
        }
    }
    
    /**
     * 사용자 상태를 변경합니다.
     * 
     * @param isActive 활성 상태
     */
    public void changeStatus(boolean isActive) {
        if (isActive) {
            this.user.activate();
        } else {
            this.user.deactivate();
        }
    }
    
    /**
     * 사용자를 삭제합니다.
     * 
     * @param deletedBy 삭제자
     */
    public void deleteUser(String deletedBy) {
        this.user.delete(deletedBy);
        
        // 관련된 모든 엔티티도 삭제 처리
        this.badges.forEach(badge -> badge.delete(deletedBy));
        this.studyMemberships.forEach(member -> member.leave(deletedBy));
    }
    
    /**
     * 사용자 통계를 계산합니다.
     * 
     * @return 사용자 통계
     */
    public UserStats calculateStats() {
        return UserStats.builder()
                .totalBadges(badges.size())
                .activeStudyGroups(studyMemberships.stream()
                        .filter(StudyMember::isActive)
                        .count())
                .totalStudyGroups(studyMemberships.size())
                .build();
    }
    
    /**
     * 사용자 통계 클래스
     */
    @Getter
    @lombok.Builder
    public static class UserStats {
        private final long totalBadges;
        private final long activeStudyGroups;
        private final long totalStudyGroups;
    }
}
