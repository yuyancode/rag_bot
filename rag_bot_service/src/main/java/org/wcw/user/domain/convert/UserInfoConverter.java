package org.wcw.user.domain.convert;


import org.mapstruct.Mapper;
import org.wcw.user.domain.entity.UserInfoDO;
import org.wcw.user.domain.vo.response.UserInfoResponse;

/**
 * @Description: 用户信息转换
 * @Author: wcw
 *
 */
@Mapper(componentModel = "spring")
public interface UserInfoConverter {
    UserInfoResponse toDto(UserInfoDO userInfoDo);
}
