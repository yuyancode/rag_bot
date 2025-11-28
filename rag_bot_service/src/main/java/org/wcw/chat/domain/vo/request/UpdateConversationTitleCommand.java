package org.wcw.chat.domain.vo.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateConversationTitleCommand {
    private String memoryId;
    private String newTitle;
}
