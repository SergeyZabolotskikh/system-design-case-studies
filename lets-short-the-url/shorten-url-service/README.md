
# 🔗 URL Shortener Microservice

This project implements a distributed and scalable **URL Shortener Microservice** written in **Java 21** using **Spring Boot 3**, with support for **PostgreSQL** and **Redis caching**.  
The service converts long URLs into short ones using caching and persistence mechanisms if already mapped.

---

## 🧩 System Design

1. Long URL is received via the REST endpoint (`/shorten`).
2. A **SHA-256 hash** of the long URL is computed.
3. The service first checks for an existing mapping in **Redis** using the hash.
4. If not found, it queries the local **PostgreSQL** DB (or **DynamoDB** for global setups).
5. If still not found, a **globally unique token** is generated and shortened.
6. The result is saved in Redis and DB, and returned as a JSON response.

---

## ⚙️ Technologies

- Java 21 + Spring Boot 3
- PostgreSQL (local dev)
- Redis 7 (TTL + LRU)
- Docker & Docker Compose

---

## 📦 Persistence Layer

### ✅ Local Mode:
- **PostgreSQL** stores:
    - `url_map`: Maps short tokens to original URLs
    - `token_allocations`: Tracks generated token usage

### 🌐 Global Mode (Optional):
- **DynamoDB** can replace `url_map` for write-side replication across regions.

---

## 🚀 Redis Caching

- Redis stores mappings with **12-hour TTL** and **LRU eviction** (via `maxmemory-policy`).
- Two key formats:
    - `long:<sha256>` → `shortToken`
    - `short:<token>` → `longUrl`
- Greatly reduces DB load and latency on repeated requests.

---

## 📬 API Endpoint

### ➕ POST `/shorten`

**Request:**
```json
{
  "longUrl": "https://example.com/ssssome-page2"
}
```

**Response:**
```json
{
  "shortUrl": "http://localhost:8080/u/abc123"
}
```

> 🔹 Note: The actual redirection endpoint (`GET /{shortCode}`) is handled by a **separate service** to follow clean architecture and separation of responsibilities.

---

## 🐳 Docker & Deployment

### Compose Services:
- `shorten-url-service`: The main Spring Boot app.
- `postgres`: PostgreSQL DB container.
- `redis`: Redis 7 container with LRU config.
- `db-init-job`: Initializes schema/tables using Flyway.

### Debugging:
- Remote debug port: **5005**
- Live reload supported via mapped volumes (optional)

---

## ✅ Status

- [x] Redis cache with TTL and LRU
- [x] Shorten URL via SHA-256 and fallback token
- [x] Dual persistence layers: PostgreSQL or DynamoDB
- [x] Dockerized for local development
- [ ] GET redirect service (handled externally)

---

## 🛠 Possible Future Improvements

- Admin dashboard for analytics
- Custom alias support
- Expiry date per URL
- Role-based access
- Rate limiting & abuse protection

---


---

## 🧪 Test Evidence (Docker Desktop)

The following screenshots show working evidence of the system running via Docker Desktop:

- `local_test.png` – PostgreSQL verification
- `test_with_redis.png` – Redis cache validation

### 🔍 Example Commands

Run the following commands in Docker to inspect Redis cache values:

```bash
docker exec -it redis redis-cli GET "7dec3059f59ae95943e899f330e896ee41508056d68fe84eeb8c2d2382b97425"
docker exec -it redis redis-cli GET "20DWH1zNtON"
```

```curl to call the API:
curl -X POST http://localhost:8080/shorten -H "Content-Type: application/json" -d "{\"longUrl\": \"https://example.com/ssssome-page2\"}"
```

These commands confirm the presence of both:
- `long:<sha256>` key
- `short:<token>` key

