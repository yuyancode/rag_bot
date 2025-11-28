package org.wcw.chat.domain.vo.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSessionResponse {
    private String memoryId;
    private String userId;
    private String title;
}
