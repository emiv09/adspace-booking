Ad Space Booking – Mono Repo
================================

This repository contains both the backend (Spring Boot) and frontend (React + TypeScript).
Each project has its own README with details, commands, and folder structure.
The mono-repo exists only for convenience so everything sits in one place while sharing the same challenge domain.

Structure
---------
ad-space-booking/
│
├── backend/           # Spring Boot app
│   └── adspace/       # actual server code + .env.example
│
└── frontend/          # React + TypeScript client

Backend – Quick Start
---------------------

cd backend/adspace

Copy env file:
cp .env.example .env
Update DB settings inside .env.

Start PostgreSQL (Docker):
docker run --name postgres-challenge -e POSTGRES_PASSWORD=<your-password> -p 5432:5432 -d postgres:15

Run the server:
./mvnw spring-boot:run

Server runs at: http://localhost:8080

Backend Tests
-------------

Run the full test suite:

./mvnw test

The backend is built with Spring Boot 3.5.8, Java 17, and enforces business rules such as seven-day minimum bookings and future start dates. Full API details, validation rules, and sample payloads live in backend/adspace/README.md.

Frontend – Quick Start
----------------------

cd frontend
npm install
npm start

Runs at: http://localhost:3000

The frontend expects the backend running locally on port 8080.
It is a Create React App project that uses Material UI, @mui/x-date-pickers, Zustand, Day.js, and Axios to deliver the booking UI described in frontend/README.md.

Environment Files
-----------------

Backend includes an .env.example inside:

backend/adspace/.env.example

Copy it → .env → update DB password.
Frontend does not require an env unless added later; configuration currently relies on the hard-coded API base URL in the client.
