package com.sp.web.book.board.movie.service;

import com.sp.web.book.board.movie.mapper.MvBoardMapper;
import com.sp.web.book.board.movie.model.dto.MvBoardDto;
import com.sp.web.book.board.movie.model.dto.MvBoardListResponseDto;
import com.sp.web.book.board.movie.model.entity.MvBoardEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MvBoardService {

    private final MvBoardMapper mvBoardMapper;
    private final ModelMapper modelMapper;

    public List<MvBoardListResponseDto> getMvBoardList() {

        List<MvBoardEntity> entities = mvBoardMapper.selectMvBoardList();
        // ModelMapper를 사용한 변환
        List<MvBoardListResponseDto> dtoList = entities.stream()
                .map(entity -> modelMapper.map(entity, MvBoardListResponseDto.class))
                .collect(Collectors.toList());

        return dtoList;

    }

    public Boolean postMvBoard(MvBoardDto dto) {

       return mvBoardMapper.insertMvBoard(dto);

    }
}
