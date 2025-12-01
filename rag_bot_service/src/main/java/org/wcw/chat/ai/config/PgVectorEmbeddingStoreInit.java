package org.wcw.chat.ai.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PgVectorEmbeddingStoreInit {
    final PgVectorProperties pgVectorProperties;

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .host(pgVectorProperties.getHost())
                .port(pgVectorProperties.getPort())
                .user(pgVectorProperties.getUser())
                .password(pgVectorProperties.getPassword())
                .database(pgVectorProperties.getDatabase())
                .table(pgVectorProperties.getTable())
                .dimension(1024)
                .dropTableFirst(false)
                .createTable(true)
                .build();
    }
}
