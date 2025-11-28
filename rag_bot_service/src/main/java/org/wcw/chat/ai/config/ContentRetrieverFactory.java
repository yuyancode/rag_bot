package org.wcw.chat.ai.config;


import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Component
@RequiredArgsConstructor
public class ContentRetrieverFactory {
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public EmbeddingStoreContentRetriever createRetriever(String memoryId, String knowledgeId) {
        if (!StringUtils.hasText(memoryId)) { // 没有memoryId(是联网搜索)
            return EmbeddingStoreContentRetriever.builder()
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .maxResults(5)
                    .minScore(0.8)
                    .build();
        }

        if (!StringUtils.hasText(knowledgeId)) {// 没有knowledgeId，则返回memoryId
            return EmbeddingStoreContentRetriever.builder()
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .maxResults(5)
                    .minScore(0.8)
                    .filter(
                            metadataKey("memoryId").isEqualTo(memoryId)
                    )
                    .build();
        }
        return EmbeddingStoreContentRetriever.builder() // 返回memoryId和knowledgeId
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(5)
                .minScore(0.8)
                .filter(
                        metadataKey("memoryId").isEqualTo(memoryId)
                                .and(metadataKey("knowledgeLibId").isEqualTo(knowledgeId))
                )
                .build();
    }
}
