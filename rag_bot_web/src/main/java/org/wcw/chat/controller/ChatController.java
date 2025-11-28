package org.wcw.chat.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wcw.chat.service.IChatService;
import org.wcw.chat.service.IConversationSideBarService;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final IConversationSideBarService iConversationSideBarService;
    private final IChatService iChatService;
}
