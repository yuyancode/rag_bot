package org.wcw.chat.ai.assistant;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.DefaultContentAggregator;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.wcw.chat.ai.assistant.IAssistant.RAGAssistant;
import org.wcw.chat.ai.assistant.IAssistant.SummarizeAssistant;
import org.wcw.chat.ai.assistant.IAssistant.WebSearchAssistant;
import org.wcw.chat.ai.config.ContentRetrieverFactory;
import org.wcw.chat.ai.config.PersistentChatMemoryStore;
import org.wcw.chat.ai.tool.SendEmailTool;
import org.wcw.config.properties.WebSearchProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssistantService {
    private final ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    private final PersistentChatMemoryStore chatMemoryStore;
    private final ContentRetrieverFactory contentRetrieverFactory;
    final WebSearchProperties webSearchProperties;
    final SendEmailTool emailTool;

    // 缓存已经创建的RAG助手实例
    // 感觉是有问题的，这里只增加不释放，会导致内存被占用；然后分布式环境下会导致缓存失效（在不同的服务器上有各自的cache）
    private final Map<String, RAGAssistant> ragAssistantCache = new ConcurrentHashMap<>();

    public RAGAssistant getRAGAssistant( String memoryId, String knowledgeId) {
        if (ragAssistantCache.containsKey(memoryId)) {
            return ragAssistantCache.get(memoryId);
        }
        RAGAssistant ragAssistant = createRagAssistant(memoryId, knowledgeId);
//        ragAssistantCache.put(memoryId, ragAssistant);
        return ragAssistant;
    }

    private RAGAssistant createRagAssistant(String memoryId, String knowledgeId) {
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetrieverFactory.createRetriever(memoryId, knowledgeId))
                .contentInjector(DefaultContentInjector.builder()
                        .promptTemplate(PromptTemplate.from("{{userMessage}}\n文档/文件/附件的内容如下，你可以基于下面的内容回答:\n{{contents}}"))
                        .build())
                .build();

        return AiServices.builder(RAGAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemoryProvider(id -> MessageWindowChatMemory.builder()
                        .id(id)
                        .maxMessages(20)
                        .chatMemoryStore(chatMemoryStore)
                        .build())
                .build();
    }

    @Bean
    public WebSearchAssistant getWebSearchAssistant() {
        WebSearchEngine webSearchEngine = SearchApiWebSearchEngine.builder()
                .engine(webSearchProperties.getEngine())
                .apiKey(webSearchProperties.getApiKey())
                .build();
        EmbeddingStoreContentRetriever embeddingStoreContentRetriever = contentRetrieverFactory.createRetriever(null, null);
        WebSearchContentRetriever webSearchContentRetriever = WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(3)
                .build();
        QueryRouter queryRouter = new DefaultQueryRouter(embeddingStoreContentRetriever, webSearchContentRetriever);

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .contentAggregator(new DefaultContentAggregator())
                .contentInjector(DefaultContentInjector.builder()
                        .promptTemplate(PromptTemplate.from("{{userMessage}}\n文档/文件/附件的内容如下，你可以基于下面的内容回答:\n{{contents}}"))
                        .build())
                .build();

        return AiServices.builder(WebSearchAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemoryProvider(id -> MessageWindowChatMemory.builder()
                        .id(id)
                        .maxMessages(20)
                        .chatMemoryStore(chatMemoryStore)
                        .build())
                .build();
    }

    @Bean
    public SummarizeAssistant summarizeAssistant() {
        return AiServices.builder(SummarizeAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .build();
    }
}
