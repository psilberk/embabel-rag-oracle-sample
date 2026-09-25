package com.embabel.agent.rag.oracle.sample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationConfigurationTest {

    @Test
    void defaultLlmUsesTheRegisteredOllamaModelId() throws IOException {
        StandardEnvironment environment = loadConfiguration();

        assertThat(environment.getProperty("embabel.models.default-llm"))
            .isEqualTo(System.getenv().getOrDefault("OLLAMA_MODEL", "llama3.2:latest"));
    }

    @Test
    void vectorDimensionsComeFromTheDemoEmbeddingService() throws IOException {
        StandardEnvironment environment = loadConfiguration();

        assertThat(environment.getProperty("embabel.rag.oracle.embedding-dimension")).isNull();
        assertThat(environment.getProperty("embabel.rag.oracle.create-vector-index", Boolean.class)).isFalse();
    }

    private StandardEnvironment loadConfiguration() throws IOException {
        var resource = new ClassPathResource("application.yml");
        List<PropertySource<?>> propertySources = new YamlPropertySourceLoader().load("application", resource);
        StandardEnvironment environment = new StandardEnvironment();
        for (int index = propertySources.size() - 1; index >= 0; index--) {
            environment.getPropertySources().addFirst(propertySources.get(index));
        }
        return environment;
    }
}
