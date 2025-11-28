# Ad Space Booking – Mono Repo

This repository contains both the backend (Spring Boot) and frontend (React + TypeScript).  
Each project has its own README with details and instructions.

This mono-repo exists only for convenience.

## Structure

```
ad-space-booking/
│
├── backend/
│   └── adspace/        # Spring Boot app + .env.example
│
└── frontend/           # React + TypeScript client
```

---

## Backend – Quick Start

```bash
cd backend/adspace
```

1. Copy env file:
```bash
cp .env.example .env
```

2. Start PostgreSQL (Docker):
```bash
docker run --name postgres-challenge -e POSTGRES_PASSWORD=<your-password> -p 5432:5432 -d postgres:15
```

3. Run the server:
```bash
./mvnw spring-boot:run
```

Server runs at: **http://localhost:8080**

### Backend Tests

```bash
./mvnw test
```

---

## Frontend – Quick Start

```bash
cd frontend
npm install
npm start
```

Runs at: **http://localhost:3000**

The frontend expects the backend running on **port 8080**.

---

## Environment Files

Backend includes an example environment file:

```
backend/adspace/.env.example
```

Copy it to `.env` and update DB credentials.

Frontend does not require an env file unless added later.

---

## Notes

- Each part of the project has its own README for full details.
- The root README only explains structure + basic startup.
