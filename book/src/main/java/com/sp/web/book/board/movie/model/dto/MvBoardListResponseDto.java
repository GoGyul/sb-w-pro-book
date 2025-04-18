package com.sp.web.book.board.movie.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MvBoardListResponseDto {

    private int bno;
    private String title;
    private String content;
    private String userId;
    private String category;
    private String movieTitle;
    private int rating;
    private int views;
    private String regdate;
    private String updatedate;
    private int isDeleted;
    private String nickname;

}
