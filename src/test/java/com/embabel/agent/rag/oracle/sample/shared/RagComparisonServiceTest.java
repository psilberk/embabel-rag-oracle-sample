package com.embabel.agent.rag.oracle.sample.shared;

import com.embabel.agent.rag.oracle.OracleVectorStore;
import com.embabel.agent.rag.model.Chunk;
import com.embabel.common.core.types.SimilarityResult;
import com.embabel.common.core.types.TextSimilaritySearchRequest;
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
        DemoEmbeddingService embeddingService = new DemoEmbeddingService();
        LlmAnswerService llmAnswerService = mock(LlmAnswerService.class);

        List<SimilarityResult<Chunk>> matches = List.of(
            SimilarityResult.create(
                Chunk.create("oracle vector index note", "root", Map.of(), "doc-1", "oracle vector index note"),
                0.91
            ),
            SimilarityResult.create(
                Chunk.create("rag pipeline note", "root", Map.of(), "doc-2", "rag pipeline note"),
                0.77
            )
        );

        when(store.vectorSearch(ArgumentMatchers.any(TextSimilaritySearchRequest.class), ArgumentMatchers.eq(Chunk.class)))
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

        RagComparisonService service = new RagComparisonService(store, new DemoEmbeddingService(), llmAnswerService);

        RagComparisonService.AnswerResponse response = service.answerWithoutRag("hello");

        assertThat(response.mode()).isEqualTo("no_rag");
        assertThat(response.hits()).isEmpty();
        assertThat(response.answer()).isEqualTo("LLM plain answer");
    }
}
