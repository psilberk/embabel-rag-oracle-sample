package com.embabel.agent.rag.oracle.sample.ui;

import com.embabel.agent.rag.oracle.sample.shared.RagComparisonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RagApiController.class)
class RagApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RagComparisonService ragComparisonService;

    @Test
    void ragEndpointReturnsAnswerPayload() throws Exception {
        var response = new RagComparisonService.AnswerResponse(
            "What is RAG?",
            "RAG answer: based on retrieved context",
            List.of(new RagComparisonService.SearchHit("doc-1", 0.88, "chunk")),
            "rag"
        );

        when(ragComparisonService.answerWithRag("What is RAG?", 5)).thenReturn(response);

        mockMvc.perform(get("/api/ask/rag").param("q", "What is RAG?"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mode").value("rag"))
            .andExpect(jsonPath("$.hits[0].id").value("doc-1"));
    }
}
