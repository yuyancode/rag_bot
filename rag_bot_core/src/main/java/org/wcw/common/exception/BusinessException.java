package org.wcw.common.exception;

import org.wcw.common.enums.ErrorEnum;

public class BusinessException extends RuntimeException{


    /**
     * 错误码
     */
    protected Integer errorCode;

    /**
     * 错误信息
     */
    protected String errorMsg;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum.getDesc());
        this.errorCode = errorEnum.getCode();
        this.errorMsg = errorEnum.getDesc();
    }
}
