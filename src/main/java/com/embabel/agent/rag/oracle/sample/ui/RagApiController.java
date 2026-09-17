package com.embabel.agent.rag.oracle.sample.ui;

import com.embabel.agent.rag.oracle.sample.shared.RagComparisonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RagApiController {

    private final RagComparisonService ragComparisonService;

    public RagApiController(RagComparisonService ragComparisonService) {
        this.ragComparisonService = ragComparisonService;
    }

    @GetMapping("/search")
    public RagComparisonService.SearchResponse search(
        @RequestParam("q") String question,
        @RequestParam(value = "topK", defaultValue = "5") int topK
    ) {
        return ragComparisonService.search(question, topK);
    }

    @GetMapping("/ask/plain")
    public RagComparisonService.AnswerResponse askPlain(@RequestParam("q") String question) {
        return ragComparisonService.answerWithoutRag(question);
    }

    @GetMapping("/ask/rag")
    public RagComparisonService.AnswerResponse askRag(
        @RequestParam("q") String question,
        @RequestParam(value = "topK", defaultValue = "5") int topK
    ) {
        return ragComparisonService.answerWithRag(question, topK);
    }
}
