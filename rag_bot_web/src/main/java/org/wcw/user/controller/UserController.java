package org.wcw.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.wcw.common.Result;
import org.wcw.common.constant.Constants;
import org.wcw.user.domain.vo.request.*;
import org.wcw.user.domain.vo.response.RefreshTokenResponse;
import org.wcw.user.domain.vo.response.UserInfoResponse;
import org.wcw.user.service.IUserInfoService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserInfoService userInfoService;

    @PostMapping("/login")
    public Result<UserInfoResponse> login(HttpServletRequest req, HttpServletResponse resp, LoginCommand request) {
        UserInfoResponse userInfoResponse = userInfoService.login(req, resp, request);
        if (userInfoResponse == null) {
            return Result.success("账号密码错误");
        }
        return Result.success(userInfoResponse);
    }

    @PostMapping("/refreshToken")
    public Result<RefreshTokenResponse> refresh(@CookieValue(value = Constants.REFRESH_TOKEN_COOKIE_NAME) String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
        return Result.success(
                RefreshTokenResponse.builder()
                        .accessToken(userInfoService.refresh(req, resp, refreshToken))
                        .build());
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest req, HttpServletResponse resp) {
        userInfoService.logout(req, resp);
        return Result.success("登出成功");
    }

    @PostMapping("/registry")
    public Result<Void> registry(@RequestBody RegistryCommand request) {
        userInfoService.registry(request);
        return Result.success("注册成功");
    }

    @PostMapping("/sendEmail")
    public Result<Void> sendEmail(@RequestBody SendEmailCommand sendEmailCommand) {
        userInfoService.sendEmailCode(sendEmailCommand);
        return Result.success("发送成功");
    }

    @PostMapping("/modifyInfo")
    public Result<Void> modify(@RequestBody ModifyUserInfoCommand request) {
        userInfoService.updateUserInfo(request);
        return Result.success("修改成功");
    }

    @PostMapping("/bindEmail")
    public Result<Void> bindEmail(@RequestBody BindEmailCommand bindEmailCommand) {
        userInfoService.bindEmail(bindEmailCommand);
        return Result.success("绑定成功");
    }
}
