package com.embabel.agent.rag.oracle.sample.cli;

import com.embabel.agent.rag.oracle.OracleVectorStore;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.embabel.agent.rag.oracle.sample")
public class OracleSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OracleSampleApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(OracleVectorStore store) {
        return args -> {
            String query = "How does Oracle vector search work?";
            float[] queryEmbedding = new float[]{0.85f, 0.10f, 0.05f};
            List<OracleVectorStore.VectorMatch> matches = store.vectorSearch(queryEmbedding, 5, 0.0);

            System.out.println("Query: " + query);
            if (matches.isEmpty()) {
                System.out.println("No matches found. Insert sample data using src/main/resources/sql/oracle_rag_demo_setup.sql first.");
                return;
            }

            System.out.println("Top matches:");
            for (OracleVectorStore.VectorMatch match : matches) {
                System.out.printf("- id=%s score=%.4f text=%s%n", match.getId(), match.getScore(), match.getText());
            }
        };
    }
}
