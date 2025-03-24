package com.sp.web.book.common.error.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {

    private final boolean success = false;
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<ValidationError> errors;

    public static ErrorResponse of(HttpStatus httpStatus, int code, String message ){
        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }

    public static ErrorResponse of(HttpStatus httpStatus,int code, String message, BindingResult bindingResult){
        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .errors(ValidationError.of(bindingResult))
                .build();
    }

    /*\
    @Valid를 사용했을 때 에러가 발생한 경우 어느 필드에서 에러가 발생했는지 응답을 하기 위한
    ValidationError를 내부 정적 클래스로 추가
    * */
    @Getter
    public static class ValidationError{
        private final String field;
        private final String value;
        private final String message;

        private ValidationError(FieldError fieldError){
            this.field = fieldError.getField();
            this.value = fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString();
            this.message = fieldError.getDefaultMessage();

        }

        public static List<ValidationError> of(final BindingResult bindingResult){
            return bindingResult.getFieldErrors().stream()
                    .map(ValidationError :: new)
                    .toList();
        }

    }

}
