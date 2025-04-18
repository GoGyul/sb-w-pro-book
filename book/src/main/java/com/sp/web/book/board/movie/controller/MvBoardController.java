package com.sp.web.book.board.movie.controller;

import com.sp.web.book.board.movie.model.dto.MvBoardDto;
import com.sp.web.book.board.movie.model.dto.MvBoardListResponseDto;
import com.sp.web.book.common.pagenation.dto.PageResponseDto;
import com.sp.web.book.board.movie.service.MvBoardService;
import com.sp.web.book.common.pagenation.dto.PageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class MvBoardController {

    private final MvBoardService service;

    @GetMapping("/movie/list")
    private PageResponseDto<MvBoardListResponseDto> getMvBoardList (@ModelAttribute PageRequestDto pageRequestDto){

        return service.getMvBoardList(pageRequestDto);

    }

    @PostMapping("/movie/write")
    private Boolean postMvBoard (@RequestBody MvBoardDto dto){

        return service.postMvBoard(dto);

    }

}
