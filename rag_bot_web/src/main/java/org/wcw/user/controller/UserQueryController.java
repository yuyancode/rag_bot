package org.wcw.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wcw.common.Result;
import org.wcw.user.domain.vo.request.QueryUserDetailInfoRequest;
import org.wcw.user.domain.vo.request.UserDetailInfoResp;
import org.wcw.user.service.IUserInfoService;

@RestController
@RequestMapping("/user-query")
@RequiredArgsConstructor
public class UserQueryController {
    private final IUserInfoService userInfoService;

    @PostMapping("/queryUserDetailInfo")
    public Result<UserDetailInfoResp> queryUserDetailInfo(@Valid @RequestBody QueryUserDetailInfoRequest request) {
        return Result.success(userInfoService.queryUserDetailInfo(request));
    }
}
