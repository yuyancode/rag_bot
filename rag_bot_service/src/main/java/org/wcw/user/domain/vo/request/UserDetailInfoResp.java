package org.wcw.user.domain.vo.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailInfoResp {
    private String username;
    private String nickname;
    private String avatarUrl;
    private String description;
    private long joinDays;
    private String email;
}
