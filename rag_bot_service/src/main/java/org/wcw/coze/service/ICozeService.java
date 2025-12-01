package org.wcw.coze.service;

import org.wcw.chat.domain.dto.WeatherDataDTO;
import org.wcw.chat.domain.vo.request.DayWhetherRequest;

public interface ICozeService {
    WeatherDataDTO getWeatherData(DayWhetherRequest request);
}
