package org.wcw.chat.ai.config;


import cn.hutool.extra.spring.SpringUtil;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.wcw.utils.ThreadLocalUtils;

import java.util.stream.Stream;

@Configuration
@ConfigurationProperties(prefix = "llm")
@Data
public class AiConfig {
    private String apiKey;
    private String model;
    private String baseUrl;

    /**
     * 自主配置大模型 - 目前配置为DeepSeek R1
     * @return
     */
    StreamingChatLanguageModel streamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(model)
                .build();
    }

    // 这里有问题，应该使用工厂模式，因为初始化的时候ThreadLocalUtils还没有memoryId和knowledgeLibId，应该根据用户实际信息传入这两个值，生成对应的embeddingStoreIngestor
    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(300, 20);

        return EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .documentSplitter(documentSplitter)
                .documentTransformer(dc -> {
                    String memoryId = (String) ThreadLocalUtils.get("memoryId");
                    String knowledgeLibId = (String) ThreadLocalUtils.get("knowledgeLibId");

                    if (StringUtils.hasText(memoryId)) {
                        dc.metadata().put("memoryId", memoryId);
                    }
                    if (StringUtils.hasText(knowledgeLibId)) {
                        dc.metadata().put("knowledgeLibId", knowledgeLibId);
                    }
                    return dc;
                })
                .build();
    }

}
