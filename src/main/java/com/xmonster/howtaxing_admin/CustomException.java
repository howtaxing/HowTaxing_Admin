package com.xmonster.howtaxing_admin;

import com.xmonster.howtaxing_admin.type.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    ErrorCode errorCode;
    String errorMessage;
    String errorMessageDetail;

    public CustomException(ErrorCode code){
        errorCode = code;
    }

    public CustomException(ErrorCode code, String msgDtl){
        errorCode = code;
        errorMessageDetail = msgDtl;
    }

    public CustomException(ErrorCode code, String msg, String msgDtl){
        errorCode = code;
        errorMessage = msg;
        errorMessageDetail = msgDtl;
    }
}
