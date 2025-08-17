package com.example.demo.controller;

import com.example.demo.dto.BoardDto;
import com.example.demo.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BoardDto.Response> createBoard(
            @Valid @RequestBody BoardDto.CreateRequest request,
            Authentication authentication) {
        BoardDto.Response response = boardService.createBoard(request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BoardDto.ListResponse>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BoardDto.ListResponse> response = boardService.getAllBoards(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto.Response> getBoardById(@PathVariable Long id) {
        BoardDto.Response response = boardService.getBoardById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateBoard(
            @PathVariable Long id,
            @Valid @RequestBody BoardDto.UpdateRequest request,
            Authentication authentication) {
        try {
            BoardDto.Response response = boardService.updateBoard(id, request, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteBoard(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            boardService.deleteBoard(id, authentication.getName());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BoardDto.ListResponse>> searchBoards(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BoardDto.ListResponse> response = boardService.searchBoards(keyword, page, size);
        return ResponseEntity.ok(response);
    }
}