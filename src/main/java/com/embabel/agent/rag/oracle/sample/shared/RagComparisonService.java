package com.embabel.agent.rag.oracle.sample.shared;

import com.embabel.agent.rag.oracle.OracleVectorStore;
import com.embabel.agent.rag.model.Chunk;
import com.embabel.common.ai.model.EmbeddingService;
import com.embabel.common.core.types.SimilarityResult;
import com.embabel.common.core.types.TextSimilaritySearchRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RagComparisonService {

    private final OracleVectorStore store;
    private final EmbeddingService embeddingService;
    private final LlmAnswerService llmAnswerService;

    public RagComparisonService(
        OracleVectorStore store,
        EmbeddingService embeddingService,
        LlmAnswerService llmAnswerService
    ) {
        this.store = store;
        this.embeddingService = embeddingService;
        this.llmAnswerService = llmAnswerService;
    }

    public SearchResponse search(String question, int topK) {
        float[] queryEmbedding = embeddingService.embed(question);
        TextSimilaritySearchRequest request = TextSimilaritySearchRequest.create(question, 0.1, topK);
        List<SimilarityResult<Chunk>> matches = store.vectorSearch(request, Chunk.class);
        List<SearchHit> hits = matches.stream()
            .map(result -> new SearchHit(
                result.getMatch().getId(),
                result.getScore(),
                result.getMatch().getText()
            ))
            .collect(Collectors.toList());
        return new SearchResponse(question, queryEmbedding, hits);
    }

    public AnswerResponse answerWithoutRag(String question) {
        String prompt = "Answer clearly and briefly. Question: " + question;
        String answer = llmAnswerService.generate(prompt);
        return new AnswerResponse(question, answer, List.of(), "no_rag");
    }

    public AnswerResponse answerWithRag(String question, int topK) {
        SearchResponse response = search(question, topK);
        String context = response.hits().stream()
            .map(h -> "[" + h.id() + "] " + h.text())
            .collect(Collectors.joining("\n"));

        String prompt = "Use the context to answer the question. " +
            "If context is insufficient, say so.\n\n" +
            "Question:\n" + question + "\n\n" +
            "Context:\n" + (context.isBlank() ? "(no context found)" : context);

        String answer = llmAnswerService.generate(prompt);
        return new AnswerResponse(question, answer, response.hits(), "rag");
    }

    public record SearchHit(String id, double score, String text) {}

    public record SearchResponse(String question, float[] embedding, List<SearchHit> hits) {}

    public record AnswerResponse(String question, String answer, List<SearchHit> hits, String mode) {}
}
