package com.embabel.agent.rag.oracle.sample.shared;

import com.embabel.agent.rag.oracle.OracleVectorStore;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RagComparisonServiceTest {

    @Test
    void answerWithRagIncludesRetrievedContext() {
        OracleVectorStore store = mock(OracleVectorStore.class);
        SimpleEmbeddingService embeddingService = new SimpleEmbeddingService();
        LlmAnswerService llmAnswerService = mock(LlmAnswerService.class);

        List<OracleVectorStore.VectorMatch> matches = List.of(
            new OracleVectorStore.VectorMatch("doc-1", "urn:1", "oracle vector index note", Map.of(), 0.91),
            new OracleVectorStore.VectorMatch("doc-2", "urn:2", "rag pipeline note", Map.of(), 0.77)
        );

        when(store.vectorSearch(ArgumentMatchers.any(float[].class), ArgumentMatchers.eq(3), ArgumentMatchers.eq(0.0)))
            .thenReturn(matches);
        when(llmAnswerService.generate(ArgumentMatchers.contains("Context:")))
            .thenReturn("LLM RAG answer");

        RagComparisonService service = new RagComparisonService(store, embeddingService, llmAnswerService);
        RagComparisonService.AnswerResponse response = service.answerWithRag("question", 3);

        assertThat(response.mode()).isEqualTo("rag");
        assertThat(response.hits()).hasSize(2);
        assertThat(response.answer()).isEqualTo("LLM RAG answer");
    }

    @Test
    void answerWithoutRagReturnsNoRagMode() {
        OracleVectorStore store = mock(OracleVectorStore.class);
        LlmAnswerService llmAnswerService = mock(LlmAnswerService.class);
        when(llmAnswerService.generate(ArgumentMatchers.anyString())).thenReturn("LLM plain answer");

        RagComparisonService service = new RagComparisonService(store, new SimpleEmbeddingService(), llmAnswerService);

        RagComparisonService.AnswerResponse response = service.answerWithoutRag("hello");

        assertThat(response.mode()).isEqualTo("no_rag");
        assertThat(response.hits()).isEmpty();
        assertThat(response.answer()).isEqualTo("LLM plain answer");
    }
}
