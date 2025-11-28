package org.wcw.chat.domain.vo.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatConversationItemResponse {
    private String memoryId;
    private String title;
}
