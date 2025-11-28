package org.wcw.chat.domain.convert;

import org.mapstruct.Mapper;
import org.wcw.chat.domain.entity.ChatConversationDO;
import org.wcw.chat.domain.vo.response.ChatSessionResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatConversationConverter {
    ChatSessionResponse toDto(ChatConversationDO chatConversationDO);

    List<ChatSessionResponse> toDtoList(List<ChatConversationDO> chatConversationDOList);
}
