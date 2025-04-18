package com.sp.web.book.board.movie.mapper;

import com.sp.web.book.board.movie.model.dto.MvBoardDto;
import com.sp.web.book.board.movie.model.entity.MvBoardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MvBoardMapper {

    List<MvBoardEntity> selectMvBoardList(@Param("offset") int offset, @Param("size") int size);

    boolean insertMvBoard(MvBoardDto dto);

    long selectTotalCount();
}
