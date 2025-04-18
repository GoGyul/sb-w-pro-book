package com.sp.web.book.common.pagenation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequestDto {

    private int page = 1;
    private int size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }

}
