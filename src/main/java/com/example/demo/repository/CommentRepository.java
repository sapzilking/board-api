package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글별 댓글 조회 (최신순)
    List<Comment> findByBoardOrderByCreatedAtAsc(Board board);

    // 사용자별 댓글 조회
    List<Comment> findByAuthor(User author);

    // 게시글의 댓글 개수
    int countByBoard(Board board);
}
