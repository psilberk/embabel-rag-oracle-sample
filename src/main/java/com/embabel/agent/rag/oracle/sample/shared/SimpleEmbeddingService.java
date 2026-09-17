package com.embabel.agent.rag.oracle.sample.shared;

import org.springframework.stereotype.Service;

@Service
public class SimpleEmbeddingService {

    public float[] embed(String text) {
        int hash = Math.abs(text.toLowerCase().hashCode());
        float x = (hash % 1000) / 1000.0f;
        float y = ((hash / 1000) % 1000) / 1000.0f;
        float z = 1.0f - Math.min(1.0f, (x + y) / 2.0f);
        return new float[]{x, y, z};
    }
}
