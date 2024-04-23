package com.example.cookietest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestException extends RuntimeException{

    private final ErrorCode errorCode;

}
