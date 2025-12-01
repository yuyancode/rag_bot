package org.wcw.chat.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.wcw.chat.domain.vo.request.ChatRequest;
import org.wcw.chat.domain.vo.request.CreateConversationCommand;
import org.wcw.chat.domain.vo.request.DeleteConversationCommand;
import org.wcw.chat.domain.vo.request.UpdateConversationTitleCommand;
import org.wcw.chat.domain.vo.response.ChatConversationItemResponse;
import org.wcw.chat.domain.vo.response.ChatMessageResponse;
import org.wcw.chat.domain.vo.response.ChatSessionResponse;
import org.wcw.chat.domain.vo.response.FileUploadResponse;
import org.wcw.chat.service.IChatService;
import org.wcw.chat.service.IConversationSideBarService;
import org.wcw.common.Result;
import org.wcw.common.annotation.MdcDot;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final IConversationSideBarService iConversationSideBarService;
    private final IChatService iChatService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件上传响应
     */
    @PostMapping("/upload")
    public Result<FileUploadResponse> uploadFile4Chat(@RequestParam("file")MultipartFile file) {
        return Result.success(iChatService.uploadFile4Chat(file));
    }

    /**
     * 流式对话
     *
     * @param memoryId 会话ID
     * @param request  聊天请求
     * @return 聊天响应
     */
    @MdcDot
    @GetMapping(value = "/{memoryId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@PathVariable String memoryId, ChatRequest request) {
        return iChatService.chat(memoryId, request);
    }

    /**
     * 查询会话历史
     *
     * @param userId 用户ID
     * @return 会话历史列表
     */
    @GetMapping("/conversation-history")
    public Result<List<ChatSessionResponse>> queryChatConversationHistory(Long userId) {
        return iConversationSideBarService.queryChatConversation(userId);
    }

    /**
     * 创建会话
     *
     * @param command 创建会话命令
     * @return 创建结果
     */
    @PostMapping("/conversation-create")
    public Result<ChatConversationItemResponse> createChatConversation(@RequestBody CreateConversationCommand command) {
        return iConversationSideBarService.createChatConversation(command);
    }

    /**
     * 删除会话
     *
     * @param command 删除会话命令
     * @return 删除结果
     */
    @PostMapping("/conversation-delete")
    public Result<Void> deleteChatConversation(@RequestBody DeleteConversationCommand command) {
        return iConversationSideBarService.deleteChatConversation(command);
    }

    /**
     * 修改会话标题
     *
     * @param command 修改会话标题命令
     * @return 修改结果
     */
    @PostMapping("/conversation-title-update")
    public Result<Void> updateChatConversationTitle(@RequestBody UpdateConversationTitleCommand command) {
        return iConversationSideBarService.updateChatConversationTitle(command);
    }

    /**
     * 查询会话历史消息
     *
     * @param memoryId 会话ID
     * @return 历史消息列表
     */
    @GetMapping("/message")
    public Result<List<ChatMessageResponse>> queryHistoryMessages(String memoryId) {
        return Result.success(iChatService.queryHistoryMessages(memoryId));
    }
}
