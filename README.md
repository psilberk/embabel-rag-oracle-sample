# embabel-rag-oracle-sample

This project contains two runnable applications in the same module:

- CLI app: `com.embabel.agent.rag.oracle.sample.cli.OracleSampleApplication`
- UI app: `com.embabel.agent.rag.oracle.sample.ui.OracleUiSampleApplication`

Both rely on `embabel-rag-oracle` auto-configuration from `application.yml`.

## Oracle setup

Run the SQL script at:

- `src/main/resources/sql/oracle_rag_demo_setup.sql`

The script includes:

- table DDL
- optional vector index DDL (with ORA-51962 warning)
- truncate block
- sample inserts
- optional full teardown

## Run CLI

```bash
mvn spring-boot:run -Dspring-boot.run.mainClass=com.embabel.agent.rag.oracle.sample.cli.OracleSampleApplication
```

## Run UI

```bash
mvn spring-boot:run -Dspring-boot.run.mainClass=com.embabel.agent.rag.oracle.sample.ui.OracleUiSampleApplication
```

Then open `http://localhost:8080`.

## Tests

```bash
mvn test
```
