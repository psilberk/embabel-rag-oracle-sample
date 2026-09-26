# embabel-rag-oracle-sample

This project contains two independently runnable Java applications:

- CLI: `com.embabel.agent.rag.oracle.sample.cli.OracleSampleApplication`
- UI: `com.embabel.agent.rag.oracle.sample.ui.OracleUiSampleApplication`

Both use `embabel-rag-oracle` auto-configuration from `application.yml`. The UI
compares an ordinary LLM answer with a RAG answer grounded by Oracle vector
search. The CLI runs one vector search and prints the matching chunks.

The sample registers a deliberately simple three-dimensional `EmbeddingService`.
It is demo code, not a general embedding model: it maps a few topics to fixed
vectors matching the SQL seed data. The library infers the table dimension from
this service, so `embedding-dimension` is not duplicated in configuration.

## Prerequisites

- Java 21
- Oracle Database 23ai or newer
- Ollama at `http://localhost:11434`
- The `llama3.2:latest` Ollama model

Install the sibling library first:

```bash
cd ../embabel-rag-oracle
mvn install
```

Install the model:

```bash
ollama pull llama3.2
```

Connection settings have local defaults and can be overridden without editing
the YAML:

```bash
export ORACLE_JDBC_URL='jdbc:oracle:thin:@//localhost:1521/FREEPDB1'
export ORACLE_USERNAME='scott'
export ORACLE_PASSWORD='tiger'
export OLLAMA_BASE_URL='http://localhost:11434'
export OLLAMA_MODEL='llama3.2:latest'
```

## Oracle Setup

Run `src/main/resources/sql/oracle_rag_demo_setup.sql`. It contains commented
sections for table creation, optional vector-index creation, truncation, seed
inserts, and teardown.

The application also initializes a missing table. It does not create the HNSW
index by default, which avoids `ORA-51962` on small local databases. Search is
still correct without the index; only performance differs as data grows.

## Run CLI

```bash
mvn spring-boot:run -Pcli
```

The CLI is explicitly non-web and exits after printing its results. It can also
be launched directly from IntelliJ using `OracleSampleApplication`.

## Run UI

```bash
mvn spring-boot:run
```

Open `http://localhost:8080`. The UI application scans only its UI and shared
components, so it does not execute the CLI runner.

The default packaged application is the UI. Package the CLI instead with:

```bash
mvn package -Pcli
```

## Tests

```bash
mvn test
```

The sample tests validate topic embeddings, RAG prompt construction, REST output,
and the important application configuration without requiring Oracle or Ollama.
