package com.sp.web.book.board.movie.service;

import com.sp.web.book.board.movie.mapper.MvBoardMapper;
import com.sp.web.book.board.movie.model.dto.MvBoardDetailReponseDto;
import com.sp.web.book.board.movie.model.dto.MvBoardDto;
import com.sp.web.book.board.movie.model.dto.MvBoardListResponseDto;
import com.sp.web.book.common.pagenation.dto.PageResponseDto;
import com.sp.web.book.board.movie.model.entity.MvBoardEntity;
import com.sp.web.book.common.pagenation.dto.PageRequestDto;
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

    public PageResponseDto<MvBoardListResponseDto> getMvBoardList(PageRequestDto pageRequestDto) {

        int offset = pageRequestDto.getOffset();
        int size = pageRequestDto.getSize();
        long totalCount = mvBoardMapper.selectTotalCount();

        List<MvBoardEntity> entities = mvBoardMapper.selectMvBoardList(offset, size);
        // ModelMapper를 사용한 변환
        List<MvBoardListResponseDto> dtoList = entities.stream()
                .map(entity -> modelMapper.map(entity, MvBoardListResponseDto.class))
                .collect(Collectors.toList());

        return new PageResponseDto<>(dtoList, totalCount);

    }

    public Boolean postMvBoard(MvBoardDto dto) {

        if(dto.getCategory().equals("ANYTINING")){
            dto.setRating(null);
        }

       return mvBoardMapper.insertMvBoard(dto);

    }

    public MvBoardDetailReponseDto getMvBoardDetail(int bno) {

        mvBoardMapper.increaseViews(bno);

        return mvBoardMapper.selectMvBoardDetail(bno);

    }
}
