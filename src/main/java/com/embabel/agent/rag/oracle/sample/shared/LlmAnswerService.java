package com.embabel.agent.rag.oracle.sample.shared;

import com.embabel.agent.api.common.Ai;
import org.springframework.stereotype.Service;

@Service
public class LlmAnswerService {

    private final Ai ai;

    public LlmAnswerService(Ai ai) {
        this.ai = ai;
    }

    public String generate(String prompt) {
        return ai.withDefaultLlm().generateText(prompt);
    }
}
