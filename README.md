```markdown
# Supermarket Checkout Challenge

**Task:** Implement a simplified supermarket checkout system. The cart can contain any number and combination of available items, in any order. Additionally, the checkout system supports weekly offers, where an offer defines a number of items that are discounted when bought together. For example: one apple costs 0.30€, but 2 apples are offered at 0.45€. These offers are applied automatically during checkout.

---

## 🚀 How to Run (Docker Compose)

The fastest way to start the entire application (Frontend, Backend & Database) is via Docker Compose. This ensures a consistent environment and avoids manual configuration.

### Prerequisites
- **Docker** and **Docker Compose** must be installed.
- **Build Artifacts**:
    - **Backend**: Run `mvn clean package -DskipTests` inside the `/backend` folder to generate the JAR file.
    - **Frontend**: Run `ng build` inside the `/frontend` folder to generate the `dist/` folder.

### Starting the System
Run the following command in the project's root directory:

```bash
docker-compose up --build

```

### Accessing the Application

Once the containers are running, you can access the system at:

* **Frontend (UI)**: [http://localhost:4200]()
* **Backend (API)**: [http://localhost:8080]()
* **Swagger UI**: [http://localhost:8080/swagger-ui/index.html]()

---

## 🛠 Tech Stack

* **Backend**: Java 21 (Eclipse Temurin), Spring Boot 3.x, Spring Data JPA
* **Frontend**: Angular 18, Angular Material, Nginx (as reverse proxy in Docker)
* **Database**: PostgreSQL 16
* **Tools**: Docker Compose, Lombok, JUnit 5 & Mockito

---


### Database Details

* **Type**: PostgreSQL (Running in Docker)
* **Port**: `5433` (mapped from 5432 to avoid local conflicts with existing Postgres instances)
* **Initial Data**: Initial products and weekly offers are seeded via `import.sql` or Hibernate schema generation on startup.

---

## 💡 Implementation Notes

* **Docker Orchestration**: The `docker-compose.yml` includes a health check to ensure the backend only starts once the database is fully ready.
* **Frontend Routing**: An `nginx.conf` is provided within the frontend container to handle Angular's client-side routing correctly.

```

