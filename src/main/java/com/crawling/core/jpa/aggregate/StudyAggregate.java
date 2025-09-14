package com.crawling.core.jpa.aggregate;

import com.tspoon.domain.study.entity.StudyGroup;
import com.tspoon.domain.study.entity.StudyMember;
import com.tspoon.domain.study.enums.MemberRole;
import com.tspoon.domain.study.enums.StudyGroupStatus;
import com.tspoon.domain.user.entity.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 스터디 Aggregate
 * 스터디 그룹과 멤버를 하나의 단위로 관리합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Getter
public class StudyAggregate {
    
    private final StudyGroup studyGroup;
    private final List<StudyMember> members;
    
    public StudyAggregate(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
        this.members = new ArrayList<>();
    }
    
    public StudyAggregate(StudyGroup studyGroup, List<StudyMember> members) {
        this.studyGroup = studyGroup;
        this.members = members != null ? members : new ArrayList<>();
    }
    
    /**
     * 스터디 그룹 정보를 업데이트합니다.
     * 
     * @param name 스터디명
     * @param description 설명
     * @param maxMembers 최대 멤버 수
     */
    public void updateStudyGroup(String name, String description, Integer maxMembers) {
        if (name != null && !name.trim().isEmpty()) {
            this.studyGroup.updateName(name);
        }
        if (description != null) {
            this.studyGroup.updateDescription(description);
        }
        if (maxMembers != null && maxMembers > 0) {
            this.studyGroup.updateMaxMembers(maxMembers);
        }
    }
    
    /**
     * 멤버를 추가합니다.
     * 
     * @param user 사용자
     * @param role 역할
     */
    public void addMember(User user, String role) {
        if (isFull()) {
            throw new IllegalStateException("스터디 그룹이 가득 찼습니다.");
        }
        
        if (isMember(user.getId())) {
            throw new IllegalStateException("이미 가입된 멤버입니다.");
        }
        
        // String을 MemberRole로 변환
        MemberRole memberRole;
        try {
            memberRole = MemberRole.fromCode(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            memberRole = MemberRole.MEMBER; // 기본값
        }
        
        StudyMember member = StudyMember.builder()
                .studyGroup(this.studyGroup)
                .user(user)
                .role(memberRole)
                .build();
        
        this.members.add(member);
        this.studyGroup.incrementMemberCount();
    }
    
    /**
     * 멤버를 제거합니다.
     * 
     * @param userId 사용자 ID
     * @param leftBy 제거자
     */
    public void removeMember(Long userId, String leftBy) {
        StudyMember member = this.members.stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
        
        member.leave(leftBy);
        this.studyGroup.decrementMemberCount();
    }
    
    /**
     * 멤버 역할을 변경합니다.
     * 
     * @param userId 사용자 ID
     * @param newRole 새로운 역할
     */
    public void changeMemberRole(Long userId, String newRole) {
        StudyMember member = this.members.stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
        
        member.updateRole(newRole);
    }
    
    /**
     * 스터디 상태를 변경합니다.
     * 
     * @param status 새로운 상태
     */
    public void changeStatus(StudyGroupStatus status) {
        this.studyGroup.changeStatus(status);
    }
    
    /**
     * 스터디를 시작합니다.
     */
    public void startStudy() {
        this.studyGroup.start();
    }
    
    /**
     * 스터디를 종료합니다.
     */
    public void endStudy() {
        this.studyGroup.end();
    }
    
    /**
     * 스터디를 삭제합니다.
     * 
     * @param deletedBy 삭제자
     */
    public void deleteStudy(String deletedBy) {
        this.studyGroup.delete(deletedBy);
        
        // 모든 멤버도 탈퇴 처리
        this.members.forEach(member -> member.leave(deletedBy));
    }
    
    /**
     * 멤버 여부를 확인합니다.
     * 
     * @param userId 사용자 ID
     * @return 멤버 여부
     */
    public boolean isMember(Long userId) {
        return this.members.stream()
                .anyMatch(member -> member.getUser().getId().equals(userId) && !member.isDeleted());
    }
    
    /**
     * 스터디가 가득 찼는지 확인합니다.
     * 
     * @return 가득 참 여부
     */
    public boolean isFull() {
        return this.studyGroup.getMemberCount() >= this.studyGroup.getMaxMembers();
    }
    
    /**
     * 활성 멤버 수를 조회합니다.
     * 
     * @return 활성 멤버 수
     */
    public long getActiveMemberCount() {
        return this.members.stream()
                .filter(member -> !member.isDeleted())
                .count();
    }
    
    /**
     * 스터디 통계를 계산합니다.
     * 
     * @return 스터디 통계
     */
    public StudyStats calculateStats() {
        return StudyStats.builder()
                .totalMembers(members.size())
                .activeMembers(getActiveMemberCount())
                .maxMembers(studyGroup.getMaxMembers())
                .isFull(isFull())
                .build();
    }
    
    /**
     * 스터디 통계 클래스
     */
    @Getter
    @lombok.Builder
    public static class StudyStats {
        private final long totalMembers;
        private final long activeMembers;
        private final int maxMembers;
        private final boolean isFull;
    }
}
