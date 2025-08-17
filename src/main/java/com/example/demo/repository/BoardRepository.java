package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 제목으로 검색 (페이징)
    Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // 작성자별 게시글 조회
    List<Board> findByAuthor(User author);

    // 최신 게시글 조회 (페이징)
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 인기 게시글 조회 (조회수 기준)
    @Query("SELECT b FROM Board b ORDER BY b.viewCount DESC, b.createdAt DESC")
    Page<Board> findPopularBoards(Pageable pageable);

    // 제목 + 내용 검색
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
    Page<Board> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
}