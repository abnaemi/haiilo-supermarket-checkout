Task:Implement a simplified supermarket checkout system. The cart can contain any number and combination of available items, in any order. Additionally the checkout system should support weekly offers, where an offer defines a number of items that are discounted when bought together. For example: one apple costs 0.30€, but 2 apples are offered at 0.45€. These offers should be applied automatically during checkout.



---

## 🚀 How to Run (Docker Compose)

Der schnellste Weg, die gesamte Anwendung (Frontend, Backend & Datenbank) zu starten, ist über Docker Compose.

### Voraussetzungen
- Docker und Docker Compose müssen installiert sein.
- Die Artefakte müssen gebaut sein:
    - **Backend**: `mvn clean package -DskipTests` im Ordner `/backend` ausführen.
    - **Frontend**: `ng build` im Ordner `/frontend` ausführen.

### Starten
Führe im Hauptverzeichnis des Projekts den folgenden Befehl aus:

```bash
docker-compose up --build
### Database Details
- **Type**: PostgreSQL (Running in Docker)
- **Port**: `5433` (mapped from 5432 to avoid local conflicts)
- **Initial Data**: Initial products and weekly offers are seeded via `import.sql` on startup.

## 🛠 Tech Stack
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Docker Compose**
- **Lombok**
- **JUnit 5 & Mockito**
  🔌 API Endpoints
  Product Catalog
  GET /api/v1/products

  Swagger: http://localhost:8080/swagger-ui/index.html