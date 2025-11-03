# 💸 Simulated Digital Banking System for Secure Transactions

## **Created for Charles Schwab Interview (NOT AFFILIATED WITH CHARLES SCHWAB (YET) - JUST A PROJECT TO SHOWCASE)**

A full-stack banking simulation platform where users can create accounts, perform secure transactions, and sign high-value envelopes (> $10,000) for verification.
Built with Spring Boot, Angular, and MySQL, the project demonstrates JWT-based authentication, role-based access control, and a modern responsive UI — containerized using Docker.  

![Sign In Page](/images/signIn.png)
![Accounts Dashboard Page](/images/accountsDashboard.png)
![Accounts Settings Modal](/images/accountSettings.png)
![Messages Page](/images/messages.png)
![Settings Page](/images/settings.png)

---

### 🚀 Features

🔐 Authentication
- Secure JWT-based login / signup
- Spring Security protecting all sensitive endpoints
- Role-based authorization (USER / ADMIN)

💳 Accounts & Transactions
- Create, delete, and manage user accounts
- Send and receive transactions between users
- Flag and route high-value transactions (> $10,000) to an approval system
- Deposit and withdraw funds safely

✉️ Envelopes & Signers
- Automatically creates an envelope for flagged transactions
- Both sender and receiver must digitally sign to approve
- SHA-256 based digital signature simulation

🖥️ Frontend (Angular)
- Clean dashboard UI with:
- Account balance overview
- Sent / Received transactions
- Interactive modals for transfers and deposits
- Responsive design using Montserrat typography

---

## 🧩 Tech Stack

| Layer             | Technology |
| :----------------: | :-------: |
| Frontend          |   Angular 17, TypeScript, HTML, CSS   |
| Backend           |   Spring Boot 3, Spring Security, JPA, Hibernate   |
| Database          |  MySQL   |
| Auth              |  JWT (Bearer Token)   |
| Deployment        |  Docker Compose   |
| Build Tools       |  Maven, Node.js   |

---

## ⚙️ Installation & Setup

Prerequisites
- Java 21+
- Node.js 18+
- MySQL 8.0+
- (optional) Docker Desktop

---

## Usage

#### Build using Docker
> docker compose up --build

To Stop:
> docker compose down

Go To: http://localhost:4200

If you want to just test locally instead, just follow the following directions 👇

---

#### Set Up Backend

1. Build backend
> cd transaction_validator

and set your database credentials in:
> src/main/resources/application.properties

Example:
```
spring.datasource.url=jdbc:mysql://localhost:3306/transaction_validator
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

2. Start backend  
> mvn spring-boot:run

Now the app will begin to run on http://localhost:8080 or if you want to use Swagger, try http://localhost:8080/swagger-ui.html

---

#### Set Up Frontend

1. Build and Start Frontend
```sh
cd frontend
npm install
ng serve
```

2. Test
Now the frontend will begin to run on http://localhost:4200

---

## 🧾 Example Use Flow
1. Sign up → automatically creates user
2. Log in → retrieves JWT token (saved in localStorage)
3. Create account → initial deposit
4. Send a transaction:
    - If ≤ $10,000 → auto-approved
    - If > $10,000 → generates envelope requiring signatures
5. Both users sign → funds transferred + envelope marked “COMPLETED”

---

👨‍💻 Author
Saman Sathenjeri  
📧 https://www.linkedin.com/in/saman-sathenjeri/
