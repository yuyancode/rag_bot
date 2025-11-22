package org.wcw.user.domain.vo.request;

import com.drew.lang.annotations.NotNull;
import lombok.Data;

@Data
public class QueryUserDetailInfoRequest {
    @NotNull
    private Long userId;
}
