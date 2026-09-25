package com.embabel.agent.rag.oracle.sample.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.embabel.agent.rag.oracle.sample.ui",
    "com.embabel.agent.rag.oracle.sample.shared"
})
public class OracleUiSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OracleUiSampleApplication.class, args);
    }
}
