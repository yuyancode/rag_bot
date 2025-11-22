package org.wcw.user.domain.vo.request;


import lombok.Data;

@Data
public class BindEmailCommand {
    private long userId;
    private String newEmail;
    private String code;
}
