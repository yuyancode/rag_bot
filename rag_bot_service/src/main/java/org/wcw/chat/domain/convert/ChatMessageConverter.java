package org.wcw.chat.domain.convert;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.wcw.chat.domain.entity.ChatMessageDO;
import org.wcw.chat.domain.vo.response.ChatMessageResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMessageConverter {
    @Mapping(source = "role", target = "role")
    @Mapping(source = "content", target = "content")
    ChatMessageResponse toDto(ChatMessageDO chatMessageDO);

    List<ChatMessageResponse> toDtoList(List<ChatMessageDO> chatMessageDOList);
}
