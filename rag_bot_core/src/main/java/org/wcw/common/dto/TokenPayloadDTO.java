package org.wcw.common.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPayloadDTO {
    private long userId;
    private String username;
    private String avatarUrl;
}
