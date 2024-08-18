package com.cotato.squadus.common.error.exception;

import com.cotato.squadus.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException {

    private ErrorCode errorCode;
}
