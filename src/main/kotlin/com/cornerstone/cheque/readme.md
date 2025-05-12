# 💳 Cheque – Spring Boot Kotlin Banking Service

## 📌 Project Description

**Cheque** is a Spring Boot Kotlin backend application that simulates a digital banking platform. It supports:

Secure user registration & login
- One-to-one KYC submission
- Account creation with spending limits
- Transactions between user accounts
- Role-based access (`USER` / `ADMIN`)
- Currency conversion logic (with KWD as base)
- Clean architecture for easy extension and testing

---

## 🚀 How to Run the App

### 1. Clone the repository
git clone https://github.com/your-org/cheque.git
cd cheque

### 2. Configure application properties
````properties
spring.application.name=cheque
spring.datasource.username=postgres
spring.datasource.password=12345678
spring.datasource.url=jdbc:postgresql://localhost:8080/chequeBase
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
server.port=8081
spring.jpa.hibernate.ddl-auto=update
logging.level.org.springframework=INFO
````
### 3. Run the application, and the app will be availabe at http://localhost:8080!


### ✅ Prerequisites

- Java 21+
- Kotlin 1.9+
- Maven
- PostgreSQL running with a database named `chequedb`

### ⚙️ Api endpoint list

| Method | Endpoint                 | Description                         |
|------| ------------------------ | ----------------------------------- |
| POST | `/auth/login`            | Login and receive JWT token         |
| POST | `/api/users`             | Register a new user                 |
| GET  | `/api/users`             | List all users (admin only)         |
| GET  | `/api/users/{id}`        | Get user by ID                      |
| POST | `/api/kyc/{userId}`      | Submit KYC info                     |
| GET  | `/api/kyc`               | View all KYC records                |
| GET  | `/api/kyc/{id}`          | View single KYC record              |
| POST | `/api/accounts/create`   | Create a new account                |
| GET  | `/api/accounts/getAll`   | List all accounts                   |
| GET  | `/api/accounts/{id}`     | Get account by ID                   |
| POST | `/api/transactions`      | Make a transaction (money transfer) |
| GET  | `/api/transactions`      | View all transactions               |
| GET  | `/api/transactions/{id}` | View single transaction             |



