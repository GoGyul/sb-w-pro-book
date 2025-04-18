package com.sp.web.book.common.pagenation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponseDto<T> {

    private List<T> items;
    private long totalCount;

    public PageResponseDto(List<T> items, long totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

}
