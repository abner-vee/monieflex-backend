package com.Java020.MonieFlex.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> {
    private String message;
    private String status;
    private HttpStatus httpStatus;
    private T data;
    private HttpStatus statusCode;

    public ApiResponse(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
