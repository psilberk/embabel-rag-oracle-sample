package com.embabel.agent.rag.oracle.sample.cli;

import com.embabel.agent.rag.oracle.OracleVectorStore;
import com.embabel.agent.rag.model.Chunk;
import com.embabel.common.core.types.SimilarityResult;
import com.embabel.common.core.types.TextSimilaritySearchRequest;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {
    "com.embabel.agent.rag.oracle.sample.cli",
    "com.embabel.agent.rag.oracle.sample.shared"
})
public class OracleSampleApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OracleSampleApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Bean
    CommandLineRunner demo(OracleVectorStore store) {
        return args -> {
            String query = "How does Oracle vector search work?";
            TextSimilaritySearchRequest request = TextSimilaritySearchRequest.create(query, 0.1, 5);
            List<SimilarityResult<Chunk>> matches = store.vectorSearch(request, Chunk.class);

            System.out.println("Query: " + query);
            if (matches.isEmpty()) {
                System.out.println("No matches found. Insert sample data using src/main/resources/sql/oracle_rag_demo_setup.sql first.");
                return;
            }

            System.out.println("Top matches:");
            for (SimilarityResult<Chunk> match : matches) {
                System.out.printf(
                    "- id=%s score=%.4f text=%s%n",
                    match.getMatch().getId(),
                    match.getScore(),
                    match.getMatch().getText()
                );
            }
        };
    }
}
