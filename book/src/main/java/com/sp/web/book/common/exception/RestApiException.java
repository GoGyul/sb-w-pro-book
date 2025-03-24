package com.sp.web.book.common.exception;

import com.sp.web.book.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
