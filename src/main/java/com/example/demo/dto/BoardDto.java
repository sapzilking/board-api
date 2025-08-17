package com.example.demo.dto;

import com.example.demo.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BoardDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자 이하여야 합니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자 이하여야 합니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private Integer viewCount;
        private String authorName;
        private String authorNickname;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Integer commentCount;
        private List<CommentDto.Response> comments;

        public static Response from(Board board) {
            return Response.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .viewCount(board.getViewCount())
                    .authorName(board.getAuthor().getUsername())
                    .authorNickname(board.getAuthor().getNickname())
                    .createdAt(board.getCreatedAt())
                    .updatedAt(board.getUpdatedAt())
                    .commentCount(board.getCommentCount())
                    .build();
        }

        public static Response fromWithComments(Board board, List<CommentDto.Response> comments) {
            Response response = from(board);
            response.comments = comments;
            return response;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private String title;
        private Integer viewCount;
        private String authorNickname;
        private LocalDateTime createdAt;
        private Integer commentCount;

        public static ListResponse from(Board board) {
            return ListResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .viewCount(board.getViewCount())
                    .authorNickname(board.getAuthor().getNickname())
                    .createdAt(board.getCreatedAt())
                    .commentCount(board.getCommentCount())
                    .build();
        }
    }
}