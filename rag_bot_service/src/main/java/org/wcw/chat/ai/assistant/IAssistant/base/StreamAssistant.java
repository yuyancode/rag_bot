package org.wcw.chat.ai.assistant.IAssistant.base;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface StreamAssistant {
    String chat(String userMessage);

    TokenStream chat(@MemoryId String memoryId, @UserMessage String userMessage);

    Flux<String> Fchat(@MemoryId String memoryId, @UserMessage String userMessage);
}
