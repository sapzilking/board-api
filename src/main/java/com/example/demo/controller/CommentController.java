package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/boards/{boardId}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentDto.Response> createComment(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentDto.CreateRequest request,
            Authentication authentication) {
        CommentDto.Response response = commentService.createComment(boardId, request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<List<CommentDto.Response>> getCommentsByBoard(@PathVariable Long boardId) {
        List<CommentDto.Response> response = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDto.UpdateRequest request,
            Authentication authentication) {
        try {
            CommentDto.Response response = commentService.updateComment(commentId, request, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication) {
        try {
            commentService.deleteComment(commentId, authentication.getName());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}