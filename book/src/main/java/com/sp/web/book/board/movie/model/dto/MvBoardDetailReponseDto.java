package com.sp.web.book.board.movie.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MvBoardDetailReponseDto {

    private int bno;             // 게시글 번호
    private String title;         // 제목
    private String content;       // 내용
    private String userId;        // 작성자 ID
    private String regdate;     // 작성일
    private String updatedate;  // 수정일
    private String category;      // 카테고리
    private String movieTitle;    // 영화 제목
    private double rating;       // 평점
    private int isDeleted;    // 삭제 여부
    private int views;        // 조회수
    private String nickname;

}
