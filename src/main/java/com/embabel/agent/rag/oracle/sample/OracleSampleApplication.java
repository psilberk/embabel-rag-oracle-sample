package com.embabel.agent.rag.oracle.sample;

import com.embabel.agent.rag.oracle.OracleVectorStore;
import java.util.List;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OracleSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OracleSampleApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(OracleVectorStore store) {
        return args -> {
            store.upsertChunk(
                "doc-1",
                "urn:sample:1",
                "Oracle vector search sample document one",
                new float[]{0.91f, 0.08f, 0.01f},
                Map.of("source", "sample")
            );
            store.upsertChunk(
                "doc-2",
                "urn:sample:2",
                "Another document with different embedding",
                new float[]{0.20f, 0.70f, 0.10f},
                Map.of("source", "sample")
            );

            List<OracleVectorStore.VectorMatch> matches = store.vectorSearch(
                new float[]{0.95f, 0.03f, 0.02f},
                5,
                0.0
            );

            System.out.println("Top matches:");
            for (OracleVectorStore.VectorMatch match : matches) {
                System.out.printf("- id=%s score=%.4f text=%s%n", match.getId(), match.getScore(), match.getText());
            }
        };
    }
}
