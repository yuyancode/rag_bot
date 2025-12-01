package org.wcw.coze.domain.vo.response;


import lombok.Data;
import org.wcw.chat.domain.dto.WeatherDataDTO;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/29 22:55
 * @description:
 */
@Data
public class DayWhetherResponse {
    private Integer code;
    private List<WeatherDataDTO> data;
    private String message;

}
