package org.wcw.chat.domain.vo.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponse {
    private String role;
    private String content;
}
