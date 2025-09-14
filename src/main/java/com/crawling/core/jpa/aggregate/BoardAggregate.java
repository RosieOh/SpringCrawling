package com.crawling.core.jpa.aggregate;

import com.tspoon.domain.board.entity.Comment;
import com.tspoon.domain.board.entity.Post;
import com.tspoon.domain.board.enums.BoardType;
import com.tspoon.domain.board.enums.PostStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시판 Aggregate
 * 게시글과 댓글을 하나의 단위로 관리합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Getter
public class BoardAggregate {
    
    private final Post post;
    private final List<Comment> comments;
    
    public BoardAggregate(Post post) {
        this.post = post;
        this.comments = new ArrayList<>();
    }
    
    public BoardAggregate(Post post, List<Comment> comments) {
        this.post = post;
        this.comments = comments != null ? comments : new ArrayList<>();
    }
    
    /**
     * 게시글을 업데이트합니다.
     * 
     * @param title 제목
     * @param content 내용
     * @param status 상태
     */
    public void updatePost(String title, String content, PostStatus status) {
        if (title != null && !title.trim().isEmpty()) {
            this.post.updateTitle(title);
        }
        if (content != null) {
            this.post.updateContent(content);
        }
        if (status != null) {
            this.post.changeStatus(status);
        }
    }
    
    /**
     * 댓글을 추가합니다.
     * 
     * @param comment 댓글
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.post.incrementCommentCount();
    }
    
    /**
     * 댓글을 삭제합니다.
     * 
     * @param commentId 댓글 ID
     * @param deletedBy 삭제자
     */
    public void deleteComment(Long commentId, String deletedBy) {
        Comment comment = this.comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        
        comment.delete(deletedBy);
        this.post.decrementCommentCount();
    }
    
    /**
     * 댓글을 수정합니다.
     * 
     * @param commentId 댓글 ID
     * @param content 새로운 내용
     */
    public void updateComment(Long commentId, String content) {
        Comment comment = this.comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        
        comment.updateContent(content);
    }
    
    /**
     * 게시글을 삭제합니다.
     * 
     * @param deletedBy 삭제자
     */
    public void deletePost(String deletedBy) {
        this.post.delete(deletedBy);
        
        // 모든 댓글도 삭제 처리
        this.comments.forEach(comment -> comment.delete(deletedBy));
    }
    
    /**
     * 게시글을 공지로 설정합니다.
     */
    public void setNotice() {
        this.post.setNotice();
    }
    
    /**
     * 게시글을 고정합니다.
     */
    public void setPinned() {
        this.post.setPinned();
    }
    
    /**
     * Q&A 게시글에서 답변을 채택합니다.
     * 
     * @param commentId 채택할 댓글 ID
     */
    public void acceptAnswer(Long commentId) {
        if (this.post.getBoardType() != BoardType.QNA) {
            throw new IllegalStateException("Q&A 게시글이 아닙니다.");
        }
        
        Comment comment = this.comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        
        comment.accept();
    }
    
    /**
     * 게시글 통계를 계산합니다.
     * 
     * @return 게시글 통계
     */
    public PostStats calculateStats() {
        return PostStats.builder()
                .totalComments(comments.size())
                .activeComments(comments.stream()
                        .filter(comment -> !comment.isDeleted())
                        .count())
                .acceptedAnswer(comments.stream()
                        .anyMatch(Comment::getIsAccepted))
                .build();
    }
    
    /**
     * 게시글 통계 클래스
     */
    @Getter
    @lombok.Builder
    public static class PostStats {
        private final long totalComments;
        private final long activeComments;
        private final boolean acceptedAnswer;
    }
}
