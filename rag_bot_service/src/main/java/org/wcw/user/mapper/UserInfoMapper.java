package org.wcw.user.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wcw.user.domain.entity.UserInfoDO;

import java.util.List;

/**
 * @author wcw
 * @date 2021/7/20
 * @Description: 用户信息Mapper接口
 */
@Mapper
public interface UserInfoMapper {
    /**
     * 插入用户信息
     * @param record 用户信息
     * @return 影响行数
     */
    int insert(@Param("record") UserInfoDO record);

    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoDO selectById(@Param("userId") Long userId);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    UserInfoDO selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    UserInfoDO selectByEmail(@Param("email") String email);


    /**
     * 更新用户信息
     * @param record 用户信息
     * @return 影响行数
     */
    int updateById(@Param("record") UserInfoDO record);

    /**
     * 删除用户信息
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("userId") Long userId);

    /**
     * 查询用户列表
     * @return 用户列表
     */
    List<UserInfoDO> selectList();

}
