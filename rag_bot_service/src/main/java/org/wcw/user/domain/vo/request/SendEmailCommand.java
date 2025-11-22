package org.wcw.user.domain.vo.request;


import lombok.Data;

@Data
public class SendEmailCommand {
    private Long userId;
    private String to;
}
