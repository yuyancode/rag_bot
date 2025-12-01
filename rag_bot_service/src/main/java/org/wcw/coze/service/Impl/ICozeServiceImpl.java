package org.wcw.coze.service.Impl;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wcw.chat.domain.dto.WeatherDataDTO;
import org.wcw.chat.domain.vo.request.DayWhetherRequest;
import org.wcw.coze.CozeClient;
import org.wcw.coze.domain.vo.request.CozeWorkFlowRequest;
import org.wcw.coze.domain.vo.response.CozeWorkFlowResponse;
import org.wcw.coze.service.ICozeService;

@Service
@RequiredArgsConstructor
public class ICozeServiceImpl implements ICozeService {
    final CozeClient cozeClient;
    @Override
    public WeatherDataDTO getWeatherData(DayWhetherRequest request) {
        CozeWorkFlowRequest<DayWhetherRequest> workFlowRequest = new CozeWorkFlowRequest<>();
        workFlowRequest.setWorkflow_id("7547187910912065575");
        workFlowRequest.setParameters(request);

        CozeWorkFlowResponse workFlowResponse = cozeClient.reqWorkFlow(workFlowRequest);
        JSONObject jsonObject = JSONObject.parseObject(workFlowResponse.getData());
        WeatherDataDTO data = JSONObject.parseObject(jsonObject.getString("data"), WeatherDataDTO.class);

        return data;
    }
}
