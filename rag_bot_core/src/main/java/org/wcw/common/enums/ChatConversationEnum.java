package org.wcw.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ChatConversationEnum {
    NORMAL(0, "正常 "),
    DELETED(1, "删除");

    @Getter
    private int status;
    private String desc;
}
