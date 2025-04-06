# 🛠️ db-initializer

This is a lightweight **Spring Boot + Flyway** service that initializes a PostgreSQL database with:

- Secure `users` table
- Role-based access via `roles` and `user_roles`
- OAuth user support
- Session handling
- Login attempts tracking
- Password history auditing

It is containerized using **Docker** and runs as a one-time job inside a `docker-compose` environment alongside PostgreSQL.

---

## 📦 Technologies Used

- Java 21
- Spring Boot 3.4.x
- Flyway for DB migration
- PostgreSQL 17
- Docker & Docker Compose

---

## 🚀 Quick Start (Using Docker Desktop)

### 1. Clone the Repository

```bash
git clone https://github.com/SergeyZabolotskikh/user-onboarding-system/db-initializer.git
cd db-initializer
```

### 2. Build the Spring Boot JAR

```bash
mvn clean package
```

### 3. Build & Start with Docker Compose

```bash
docker compose down -v     # Ensures a fresh DB
docker compose up --build --force-recreate
```

The `db-initializer` container will automatically:
- Connect to PostgreSQL
- Run Flyway migrations (create schema + tables)
- Insert initial test data

---

## 🔐 Default Credentials

| Property     | Value              |
|--------------|--------------------|
| Database     | `user_db`          |
| Schema       | `users_schema`     |
| Username     | `users_admin`      |
| Password     | `users_admin_pass` |
| Port         | `5432`             |

---

## 🗂️ Database Structure (Main Tables)

- `users`: Base user info (UUID, email, password hash)
- `roles`: Available roles (e.g., `ADMIN`, `USER`)
- `user_roles`: Many-to-many mapping of users to roles
- `sessions`: Session tokens for authenticated users
- `oauth_users`: Links to external OAuth providers
- `login_attempts`: Security audit for logins
- `password_history`: History of user passwords

---

## 🧪 Inspect the Database

To connect via **psql** from your host:

```bash
psql -h localhost -U users_admin -d user_db
# password: users_admin_pass
```

Or use **DBeaver** with:

- Host: `localhost`
- Port: `5432`
- DB: `user_db`
- Username: `users_admin`
- Password: `users_admin_pass`

Then explore the `users_schema`.

---

## 🧼 Cleaning Up

```bash
docker compose down -v
```

This removes containers and deletes DB data (volume).

---

## 📁 Folder Structure

```
db-initializer/
├── src/
│   └── main/
│       └── resources/
│           └── db/
│               └── migration/
│                   └── V0__create_schema.sql
│                   └── V1__create_roles_table.sql
│                   └── ...
├── dockerfile
├── docker-compose.yaml
├── application.properties
└── README.md
```

---

## 📌 Notes

- Flyway migration scripts are run in version order (V0, V1, ..., V10)
- Schema is created as `users_schema` and all tables are created inside it
- Data is truncated before re-insertion to ensure clean setup on each run

---