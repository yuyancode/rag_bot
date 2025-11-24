package org.wcw.common.enums;

import lombok.Getter;

public enum ErrorEnum {
    USER_NOT_EXIST(505, "用户不存在"),
    ACCESS_TOKEN_INVALID(401, "access_token无效"),
    REFRESH_TOKEN_INVALID(400, "refresh_token无效"),
    USER_EXIST(506, "用户名已存在"),
    FILE_UPLOAD_FAILED(507, "图片文件异常"),
    EMAIL_CODE_INVALID(508, "邮箱验证码无效");

    @Getter
    private int code;

    @Getter
    private String desc;

    ErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
