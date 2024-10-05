package com.xmonster.howtaxing_admin.exception;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.model.ErrorResponseEntity;
import com.xmonster.howtaxing_admin.type.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e){

        ErrorCode errorCode = e.getErrorCode();
        String errorMessage = e.getErrorMessage();
        String errorMessageDetail = e.getErrorMessageDetail();

        if(errorCode != null){
            if(errorMessageDetail != null){
                if(errorMessage != null){
                    return ErrorResponseEntity.toResponseEntity(errorCode, errorMessage, errorMessageDetail);
                }else{
                    return ErrorResponseEntity.toResponseEntity(errorCode, errorMessageDetail);
                }
            }else{
                return ErrorResponseEntity.toResponseEntity(errorCode);
            }
        }else{
            errorCode = ErrorCode.SYSTEM_UNKNOWN_ERROR;
            return ErrorResponseEntity.toResponseEntity(errorCode);
        }
    }
}