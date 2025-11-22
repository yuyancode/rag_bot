package org.wcw.user.domain.vo.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private Long userId;
    private String userName;
    private String nickName;
    private String avatarUrl;
}
