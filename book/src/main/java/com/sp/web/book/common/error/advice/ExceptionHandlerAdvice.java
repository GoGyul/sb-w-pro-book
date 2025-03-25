package com.sp.web.book.common.error.advice;

import com.sp.web.book.common.error.CommonErrorCode;
import com.sp.web.book.common.error.ErrorCode;
import com.sp.web.book.common.error.response.ErrorResponse;
import com.sp.web.book.common.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    //모든 에러 -> 하위 에러에서 못받을때
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        log.error("[Exception] : {} , message : {}" , NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(),errorCode.getCode(),errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity handleSystemException(RestApiException e){
        log.error("[SystemException] cause: {}, message: {}",NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(),errorCode.getCode(),  errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    //메소드가 잘못되었거나 부적합한 인수를 전달했을 경우 -> 필수 파라미터 없을때
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity handleIllegalAccessException(IllegalAccessException e){
        log.error("[IlleagalArgumentException] cause: {} , message: {}",NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
        ErrorCode errorCode = CommonErrorCode.ILLEGAL_ARGUMENT_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus() , errorCode.getCode(),
                String.format("%s %s",errorCode.getMessage(), NestedExceptionUtils.getMostSpecificCause(e).getMessage()));
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);

    }

    //@Valid 유효성 검사에서 예외가 발생했을 때 -> requestbody에 잘못 들어왔을 때.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("[MethodArgumentNotValidException] cause: {}, message: {}",NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
        ErrorCode errorCode = CommonErrorCode.INVALID_ARGUMENT_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(),
                errorCode.getCode(),
                errorCode.getMessage(),
                e.getBindingResult());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

}
