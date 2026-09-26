package com.embabel.agent.rag.oracle.sample.shared;

import com.embabel.common.ai.model.EmbeddingService;
import com.embabel.common.ai.model.PricingModel;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Deliberately simple embeddings for this three-dimensional demo dataset only.
 */
@Service
public class DemoEmbeddingService implements EmbeddingService {

    @Override
    public float[] embed(String text) {
        String value = text.toLowerCase();
        if (containsAny(value, "oracle", "index", "indexing")) {
            return new float[]{1.0f, 0.0f, 0.0f};
        }
        if (containsAny(value, "rag", "context", "grounding")) {
            return new float[]{0.0f, 1.0f, 0.0f};
        }
        return new float[]{0.0f, 0.0f, 1.0f};
    }

    @Override
    public List<float[]> embed(List<String> texts) {
        return texts.stream().map(this::embed).toList();
    }

    @Override
    public int getDimensions() {
        return 3;
    }

    @Override
    public String getName() {
        return "demo-embeddings";
    }

    @Override
    public String getProvider() {
        return "sample-only";
    }

    @Override
    public PricingModel getPricingModel() {
        return PricingModel.getALL_YOU_CAN_EAT();
    }

    private boolean containsAny(String text, String... terms) {
        return Arrays.stream(terms).anyMatch(text::contains);
    }
}
