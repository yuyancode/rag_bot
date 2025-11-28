package org.wcw.chat.domain.vo.request;


import lombok.Data;

@Data
public class DeleteConversationCommand {
    private String memoryId;
}
