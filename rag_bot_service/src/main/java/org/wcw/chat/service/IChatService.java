package org.wcw.chat.service;


import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.wcw.chat.domain.vo.request.ChatRequest;
import org.wcw.chat.domain.vo.response.ChatMessageResponse;
import org.wcw.chat.domain.vo.response.FileUploadResponse;

import java.util.List;

public interface IChatService {
    /**
     * 流式对话
     *
     * @param memoryId 会话ID
     * @param request  聊天请求
     * @return 聊天响应
     */
    SseEmitter chat(String memoryId, ChatRequest request);

    /**
     * 查询会话历史消息
     *
     * @param memoryId 会话ID
     * @return 历史消息列表
     */
    List<ChatMessageResponse> queryHistoryMessages(String memoryId);

    /**
     * 对话中文件上传
     *
     * @param file 文件
     * @return 文件上传响应
     */
    FileUploadResponse uploadFile4Chat(MultipartFile file);

}
