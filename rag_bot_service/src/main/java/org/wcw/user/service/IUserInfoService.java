package org.wcw.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wcw.user.domain.entity.UserInfoDO;
import org.wcw.user.domain.vo.request.*;
import org.wcw.user.domain.vo.response.UserInfoResponse;

import java.util.List;

/**
 * 用户信息服务接口
 * @author wcw
 */
public interface IUserInfoService {
    String TOKEN = "token";

    /**
     *刷新token
     *
     * @param req
     * @param resp
     * @param refreshToken
     * @return
     */
    String refresh(HttpServletRequest req, HttpServletResponse resp, String refreshToken);

    /**
     * 创建用户
     * @return 用户ID
     */
    Long registry(RegistryCommand registryCommand);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserInfoDO getUserByUsername(String username);

    /**
     * 根据邮箱获取用户信息
     * @param email 邮箱
     * @return 用户信息
     */
    UserInfoDO getUserByEmail(String email);

    /**
     * 更新用户信息
     * @param modifyUserInfoCommand 用户信息
     */
    void modifyUserInfo(ModifyUserInfoCommand modifyUserInfoCommand);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 获取用户列表
     * @return 用户列表
     */
    List<UserInfoDO> listUsers();

    /**
     * 用户登录
     * @return 用户信息，登录失败返回null
     */
    UserInfoResponse login(HttpServletRequest req, HttpServletResponse resp, LoginCommand request);

    /**
     * 登出
     * @param req
     * @param resp
     */
    void logout(HttpServletRequest req, HttpServletResponse resp);

    /**
     * 查询用户详情信息
     * @param request
     * @return
     */
    UserInfoResponse queryUserDetailInfo(QueryUserDetailInfoRequest request);

    /**
     * 绑定/换绑 邮箱
     * @param bindEmailCommand
     */
    void bindEmail(BindEmailCommand bindEmailCommand);

    /**
     * 发送邮箱验证码
     * @param sendEmailCommand
     */
    void sendEmailCode(SendEmailCommand sendEmailCommand);
}
