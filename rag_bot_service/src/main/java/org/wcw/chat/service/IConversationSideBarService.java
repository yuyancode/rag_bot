package org.wcw.chat.service;

import org.wcw.chat.domain.vo.request.CreateConversationCommand;
import org.wcw.chat.domain.vo.request.DeleteConversationCommand;
import org.wcw.chat.domain.vo.request.UpdateConversationTitleCommand;
import org.wcw.chat.domain.vo.response.ChatConversationItemResponse;
import org.wcw.chat.domain.vo.response.ChatSessionResponse;
import org.wcw.common.Result;

import java.util.List;

public interface IConversationSideBarService {
    Result<List<ChatSessionResponse>> queryChatConversation(long userId);

    Result<ChatConversationItemResponse> createChatConversation(CreateConversationCommand userId);

    Result<Void> deleteChatConversation(DeleteConversationCommand request);

    Result<Void> updateChatConversationTitle(UpdateConversationTitleCommand command);
}
