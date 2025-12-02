package org.wcw.home.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wcw.chat.domain.dto.WeatherDataDTO;
import org.wcw.chat.domain.vo.request.DayWhetherRequest;
import org.wcw.chat.domain.vo.request.SubmitIssueCommand;
import org.wcw.common.Result;
import org.wcw.coze.service.ICozeService;
import org.wcw.utils.EmailUtil;

@Slf4j
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final ICozeService iCozeService;
    private final EmailUtil emailUtil;

    @PostMapping("/submitIssue")
    public Result<Void> submitIssue(@RequestBody SubmitIssueCommand issue) {
        emailUtil.sendFeedbackEmail(issue.getTitle(), issue.getIssueDescription());
        return Result.success(null);
    }

    @PostMapping("/getWeatherData")
    public Result<WeatherDataDTO> getWeatherData(@RequestBody DayWhetherRequest request) {
        return Result.success(iCozeService.getWeatherData(request));
    }
}
