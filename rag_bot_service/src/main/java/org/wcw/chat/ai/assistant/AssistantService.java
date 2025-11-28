package org.wcw.chat.ai.assistant;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wcw.chat.ai.config.ContentRetrieverFactory;
import org.wcw.chat.ai.config.PersistentChatMemoryStore;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssistantService {
    private final ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    private final PersistentChatMemoryStore persistentChatMemory;
    private final ContentRetrieverFactory contentRetrieverFactory;
}
