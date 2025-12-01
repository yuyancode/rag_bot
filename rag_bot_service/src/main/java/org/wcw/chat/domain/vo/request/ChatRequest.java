package org.wcw.chat.domain.vo.request;


import lombok.Data;

@Data
public class ChatRequest {
    private String konwledgeLidId;
    private String userId;
    private String userMessage;
    private String fileId;
    private Boolean isWebSearchRequest;
}
