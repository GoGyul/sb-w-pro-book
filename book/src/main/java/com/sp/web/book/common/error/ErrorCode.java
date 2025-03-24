package com.sp.web.book.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();
    HttpStatus getHttpStatus();
    int getCode();
    String getMessage();

}
