package com.sp.web.book.board.movie.mapper;

import com.sp.web.book.board.movie.model.entity.MvBoardEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MvBoardMapper {

    List<MvBoardEntity> selectMvBoardList();

}
