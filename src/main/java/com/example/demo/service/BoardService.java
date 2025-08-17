package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.User;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public BoardDto.Response createBoard(BoardDto.CreateRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        Board savedBoard = boardRepository.save(board);
        return BoardDto.Response.from(savedBoard);
    }

    @Transactional(readOnly = true)
    public Page<BoardDto.ListResponse> getAllBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);

        return boards.map(BoardDto.ListResponse::from);
    }

    @Transactional(readOnly = true)
    public BoardDto.Response getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 조회수 증가
        board.increaseViewCount();

        // 댓글 조회
        List<CommentDto.Response> comments = commentRepository.findByBoardOrderByCreatedAtAsc(board)
                .stream()
                .map(CommentDto.Response::from)
                .collect(Collectors.toList());

        return BoardDto.Response.fromWithComments(board, comments);
    }

    public BoardDto.Response updateBoard(Long id, BoardDto.UpdateRequest request, String username) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 작성자 또는 관리자만 수정 가능
        if (!board.isAuthor(user) && !user.isAdmin()) {
            throw new RuntimeException("Access denied");
        }

        board.update(request.getTitle(), request.getContent());
        return BoardDto.Response.from(board);
    }

    public void deleteBoard(Long id, String username) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 작성자 또는 관리자만 삭제 가능
        if (!board.isAuthor(user) && !user.isAdmin()) {
            throw new RuntimeException("Access denied");
        }

        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardDto.ListResponse> searchBoards(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Board> boards = boardRepository.findByTitleOrContentContaining(keyword, pageable);

        return boards.map(BoardDto.ListResponse::from);
    }
}