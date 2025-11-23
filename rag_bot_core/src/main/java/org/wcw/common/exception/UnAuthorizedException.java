package org.wcw.common.exception;

import org.wcw.common.enums.ErrorEnum;

public class UnAuthorizedException extends RuntimeException {
    /**
	 * 错误码
	 */
    protected Integer errorCode;

    /**
     * 错误信息
     */
    protected String errorMsg;

    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException(ErrorEnum errorEnum) {
        super(errorEnum.getDesc());
        this.errorCode = errorEnum.getCode();
        this.errorMsg = errorEnum.getDesc();
    }
}
