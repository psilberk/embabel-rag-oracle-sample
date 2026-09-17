-- Oracle RAG demo setup script
-- Run blocks manually in SQL Developer / SQLcl as SCOTT (or your configured schema).

-- -----------------------------------------------------------------------------
-- 1) DDL: Create table for demo chunks and vectors.
-- -----------------------------------------------------------------------------
CREATE TABLE scott.CONTENT_ELEMENTS (
    id VARCHAR2(128) PRIMARY KEY,
    uri VARCHAR2(1024),
    text CLOB NOT NULL,
    embedding VECTOR(3, FLOAT32),
    metadata CLOB,
    ingestion_timestamp TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

-- -----------------------------------------------------------------------------
-- 2) DDL: Create vector index (currently disabled for the local demo).
--    Creating an HNSW index requires space in the PDB's vector memory area. On a
--    small local Oracle installation this can fail with ORA-51962. The index is
--    only a performance optimization: VECTOR_DISTANCE searches remain correct
--    without it and use an exact scan, which is sufficient for this small dataset.
--    Uncomment the complete statement after a DBA configures enough vector memory.
-- -----------------------------------------------------------------------------
-- CREATE VECTOR INDEX scott.CONTENT_ELEMENTS_embedding_hnsw_idx
-- ON scott.CONTENT_ELEMENTS (embedding)
-- ORGANIZATION INMEMORY NEIGHBOR GRAPH
-- DISTANCE COSINE
-- WITH TARGET ACCURACY 95;

-- -----------------------------------------------------------------------------
-- 3) Cleanup: Truncate rows while keeping table/index.
-- -----------------------------------------------------------------------------
    TRUNCATE TABLE scott.CONTENT_ELEMENTS;

    -- -----------------------------------------------------------------------------
    -- 4) Sample inserts for RAG comparison.
    --    These chunks are intentionally explanatory so the LLM can produce grounded
    --    answers for questions like "How does Oracle vector indexing help search?".
    -- -----------------------------------------------------------------------------
    INSERT INTO scott.CONTENT_ELEMENTS (id, uri, text, embedding, metadata)
    VALUES (
      'doc-1',
      'urn:oracle:index:overview',
      'Oracle vector indexing improves similarity search by avoiding full table scans. '
      || 'Instead of comparing every stored embedding, the index narrows the candidate set '
      || 'so nearest-neighbor lookups can return top matches with much lower latency.',
      TO_VECTOR('[0.94,0.04,0.02]'),
      '{"topic":"oracle-vector-index","section":"overview","source":"manual"}'
    );

    INSERT INTO scott.CONTENT_ELEMENTS (id, uri, text, embedding, metadata)
    VALUES (
      'doc-2',
      'urn:oracle:index:hnsw',
      'Oracle uses an in-memory neighbor graph for approximate nearest-neighbor search. '
      || 'This HNSW-style structure trades a small amount of recall for faster query time, '
      || 'which is usually a good fit for interactive RAG retrieval workloads.',
      TO_VECTOR('[0.90,0.07,0.03]'),
      '{"topic":"oracle-vector-index","section":"hnsw","source":"manual"}'
    );

    INSERT INTO scott.CONTENT_ELEMENTS (id, uri, text, embedding, metadata)
    VALUES (
      'doc-3',
      'urn:oracle:index:tradeoffs',
      'Vector indexes improve speed but consume memory. If memory is too constrained, '
      || 'index creation can fail with ORA-51962. In that case, retrieval can still run '
      || 'without the index, but query performance will be slower as data volume grows.',
      TO_VECTOR('[0.88,0.09,0.03]'),
      '{"topic":"oracle-vector-index","section":"tradeoffs","source":"manual"}'
    );

    INSERT INTO scott.CONTENT_ELEMENTS (id, uri, text, embedding, metadata)
    VALUES (
      'doc-4',
      'urn:oracle:index:distance',
      'Cosine distance compares direction between vectors rather than raw magnitude. '
      || 'For text embeddings, cosine is commonly used because semantically similar text '
      || 'tends to produce vectors with similar direction.',
      TO_VECTOR('[0.82,0.14,0.04]'),
      '{"topic":"vector-distance","section":"cosine","source":"manual"}'
    );

    INSERT INTO scott.CONTENT_ELEMENTS (id, uri, text, embedding, metadata)
    VALUES (
      'doc-5',
      'urn:oracle:index:rag',
      'In a RAG pipeline, Oracle vector retrieval is the grounding step: it fetches '
      || 'relevant chunks that are injected into the LLM prompt. Better chunk quality and '
      || 'better embeddings usually improve factual quality of generated answers.',
      TO_VECTOR('[0.86,0.11,0.03]'),
      '{"topic":"rag","section":"grounding","source":"manual"}'
    );

    COMMIT;

-- -----------------------------------------------------------------------------
-- 5) Optional full teardown.
-- -----------------------------------------------------------------------------
-- DROP TABLE scott.CONTENT_ELEMENTS PURGE;
