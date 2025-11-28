package org.wcw.chat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wcw.chat.ai.assistant.AssistantService;
import org.wcw.chat.domain.convert.ChatMessageConverter;
import org.wcw.chat.mapper.ChatMessageMapper;
import org.wcw.chat.service.IChatService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {
    private final ChatMessageMapper chatMessageMapper;
    private final ChatMessageConverter chatMessageConverter;
    private final AssistantService assistantService;
}
