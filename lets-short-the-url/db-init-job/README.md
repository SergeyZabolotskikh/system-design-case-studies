# 🛠️ DB Init Job - URL Shortener

This container is responsible for initializing the PostgreSQL database schema used by the URL Shortener system.

---

## 🎯 Purpose

- Create tables, indexes, and sequences needed by the URL Shortener system
- Ensure `url_hash` and `short_token` are unique (to prevent duplicates)
- Track token block allocations per service instance
- Set up a reusable DB schema for distributed token generation

---

## 📦 Components

- `Dockerfile` — Defines the container to run the init script
- `entrypoint.sh` — Waits for PostgreSQL readiness and executes SQL
- `init-db.sql` — Defines schema, sequences, and indexes

---

## 🗃️ Schema Objects Created

> Schema: `url_shortener`

### 🔗 `url_map`
Stores the long ↔ short URL mappings.

```sql
CREATE TABLE url_map (
    id SERIAL PRIMARY KEY,
    url_hash VARCHAR(64) UNIQUE NOT NULL,
    long_url TEXT NOT NULL,
    short_token VARCHAR(16) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 📊 `token_allocations`
Tracks which service instance (pod) was allocated which range of tokens.

```sql
CREATE TABLE token_allocations (
    allocation_id SERIAL PRIMARY KEY,
    instance_id VARCHAR(64) NOT NULL,
    region VARCHAR(32),
    token_start BIGINT NOT NULL,
    token_end BIGINT NOT NULL,
    allocated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 🔢 `token_sequence`
Global numeric token generator used to allocate blocks (incremental, collision-free).

```sql
CREATE SEQUENCE token_sequence
  INCREMENT BY 1
  MINVALUE 1
  START WITH 1000000
  CACHE 100;
```

---

## 🚀 How It Works

1. Waits until the PostgreSQL server is reachable
2. Executes the schema creation SQL via `psql`
3. Exits automatically when done

---

## 🔧 Environment Variables Required

- `DB_HOST`
- `DB_PORT`
- `DB_USER`
- `DB_NAME`
- `PGPASSWORD` (used by `psql` for authentication)

---

## 🔍 How to Check the Database from Inside the Container

You can manually connect to the running PostgreSQL container like this:

```bash
docker exec -it url-shortener-db psql -U postgres -d url_shortener
```

Then inside the `psql` prompt, run:

```sql
\dt url_shortener.*
SELECT * FROM url_shortener.token_allocations;
```

To exit `psql`, type:

```sql
\q
```

---

## 📝 Note

- Redis is **not required** for this job.
- Redis **will be used later** for Bloom filter-based deduplication and fast token caching by application services.

---
