# Haiilo Supermarket Checkout KATA

A simplified supermarket checkout system built with Spring Boot, Java 21, and PostgreSQL.

## 🏗 Architecture
- **DDD-inspired Structure**: Separation of Concerns between API, Domain, and Infrastructure.
- **Persistence**: PostgreSQL with UUIDs for enhanced security and scalability.
- **Logic**: Automatic application of "Weekly Offers" (Bundle Pricing) during checkout.

## 🚀 Getting Started

### Prerequisites
- **Java 21**
- **Docker Desktop** (must be running)

### How to Run
1. **Clone the repository.**
2. **Start the application**:
    - Open the project in IntelliJ.
    - Run `HaiiloSupermarketCheckoutApplication`.
    - *Note: Spring Boot Docker Compose will automatically start the database.*

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