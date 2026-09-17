package com.embabel.agent.rag.oracle.sample.shared;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class LlmAnswerService {

    private final ChatClient chatClient;

    public LlmAnswerService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generate(String prompt) {
        String response = chatClient.prompt()
            .user(prompt)
            .call()
            .content();
        return response == null ? "" : response;
    }
}
