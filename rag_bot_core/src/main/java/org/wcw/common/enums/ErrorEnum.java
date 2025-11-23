package org.wcw.common.enums;

import lombok.Getter;

public enum ErrorEnum {
    USER_NOT_EXIST(505, "用户不存在"),
    ACCESS_TOKEN_INVALID(401, "access_token无效"),
    REFRESH_TOKEN_INVALID(400, "refresh_token无效"),
    ;

    @Getter
    private int code;

    @Getter
    private String desc;

    ErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
