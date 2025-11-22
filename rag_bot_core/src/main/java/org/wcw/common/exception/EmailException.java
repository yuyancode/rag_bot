package org.wcw.common.exception;

/**
 * 邮件发送异常
 */
public class EmailException extends RuntimeException {
    public EmailException(String message) {
        super(message);
    }
}
