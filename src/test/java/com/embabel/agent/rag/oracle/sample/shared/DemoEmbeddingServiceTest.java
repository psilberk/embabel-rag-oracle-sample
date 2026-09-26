package com.embabel.agent.rag.oracle.sample.shared;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DemoEmbeddingServiceTest {

    private final DemoEmbeddingService embeddingService = new DemoEmbeddingService();

    @Test
    void mapsDemoTopicsToTheSeedDataDimensions() {
        assertThat(embeddingService.embed("How does Oracle vector indexing help search?"))
            .containsExactly(1.0f, 0.0f, 0.0f);
        assertThat(embeddingService.embed("How does RAG add context?"))
            .containsExactly(0.0f, 1.0f, 0.0f);
        assertThat(embeddingService.embed("Explain cosine similarity"))
            .containsExactly(0.0f, 0.0f, 1.0f);
        assertThat(embeddingService.getDimensions()).isEqualTo(3);
    }
}
