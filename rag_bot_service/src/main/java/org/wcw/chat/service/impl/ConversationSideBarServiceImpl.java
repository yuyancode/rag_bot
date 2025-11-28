package org.wcw.chat.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wcw.chat.domain.convert.ChatConversationConverter;
import org.wcw.chat.domain.entity.ChatConversationDO;
import org.wcw.chat.domain.vo.request.CreateConversationCommand;
import org.wcw.chat.domain.vo.request.DeleteConversationCommand;
import org.wcw.chat.domain.vo.request.UpdateConversationTitleCommand;
import org.wcw.chat.domain.vo.response.ChatConversationItemResponse;
import org.wcw.chat.domain.vo.response.ChatSessionResponse;
import org.wcw.chat.mapper.ChatConversationMapper;
import org.wcw.chat.mapper.ChatMessageMapper;
import org.wcw.chat.service.IConversationSideBarService;
import org.wcw.common.Result;

import java.util.List;
import java.util.UUID;

import static org.wcw.chat.domain.common.constant.ChatConstant.NEW_SESSION_TITLE;

@Service
@RequiredArgsConstructor
public class ConversationSideBarServiceImpl implements IConversationSideBarService {
    private final ChatConversationMapper chatConversationMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatConversationConverter chatConversationConverter;

    /**
     * 查询会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    @Override
    public Result<List<ChatSessionResponse>> queryChatConversation(long userId) {
        List<ChatConversationDO> chatConversationDOList = chatConversationMapper.selectByUserId(userId);
        return Result.success(chatConversationConverter.toDtoList(chatConversationDOList));
    }

    /**
     * 新建会话
     *
     * @param request 新建会话请求
     * @return 新建会话响应
     */
    @Override
    public Result<ChatConversationItemResponse> createChatConversation(CreateConversationCommand request) {
        String memoryId = UUID.randomUUID().toString();
        Long userId = request.getUserId();
        ChatConversationDO chatConversationDO = ChatConversationDO.builder()
                .memoryId(memoryId)
                .userId(userId)
                .build();
        chatConversationMapper.insert(chatConversationDO);
        return Result.success(
                ChatConversationItemResponse.builder()
                        .memoryId(memoryId)
                        .title(NEW_SESSION_TITLE)
                        .build()
        );
    }

    /**
     * 删除会话
     *
     * @param request 删除会话请求
     * @return 删除会话响应
     */
    @Override
    public Result<Void> deleteChatConversation(DeleteConversationCommand request) {
        chatConversationMapper.updateStatus(request.getMemoryId(), 2);
        return Result.success(null);
    }

    @Override
    public Result<Void> updateChatConversationTitle(UpdateConversationTitleCommand command) {
        chatConversationMapper.updateTitle(command.getMemoryId(), command.getNewTitle());
        return Result.success(null);
    }
}
