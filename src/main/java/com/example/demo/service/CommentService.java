package com.example.demo.service;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public CommentDto.Response createComment(Long boardId, CommentDto.CreateRequest request, String username) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .board(board)
                .author(author)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.Response.from(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto.Response> getCommentsByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        return commentRepository.findByBoardOrderByCreatedAtAsc(board)
                .stream()
                .map(CommentDto.Response::from)
                .collect(Collectors.toList());
    }

    public CommentDto.Response updateComment(Long commentId, CommentDto.UpdateRequest request, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 작성자 또는 관리자만 수정 가능
        if (!comment.isAuthor(user) && !user.isAdmin()) {
            throw new RuntimeException("Access denied");
        }

        comment.update(request.getContent());
        return CommentDto.Response.from(comment);
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 작성자 또는 관리자만 삭제 가능
        if (!comment.isAuthor(user) && !user.isAdmin()) {
            throw new RuntimeException("Access denied");
        }

        commentRepository.delete(comment);
    }
}