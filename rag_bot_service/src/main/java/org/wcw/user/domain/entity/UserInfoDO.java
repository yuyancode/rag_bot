package org.wcw.user.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 用户信息
 *
 * @author wcw
 * @date 2023/05/05
 * @description: 用户信息实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDO {
    /**
     * 主键ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 个性签名
     */
    private String description;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
