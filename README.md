<div align="center">

<br/>

# 🍼 InfantMilkCare

**A precision health monitoring platform for infant nutrition, feeding schedules, and growth tracking.**

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![License](https://img.shields.io/badge/License-MIT-pink?style=for-the-badge)](LICENSE)

<br/>

> _"Because every drop matters."_ — A smart companion for parents tracking their infant's milk intake, health stats, and feeding alarms in one beautiful dashboard.

<br/>

</div>

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [🖥️ Screenshots](#️-screenshots)
- [🏗️ Architecture](#️-architecture)
- [🗄️ Database Schema](#️-database-schema)
- [🔐 Security](#-security)
- [🚀 Getting Started](#-getting-started)
- [☁️ Deploy to Render](#️-deploy-to-render)
- [⚙️ Configuration](#️-configuration)
- [📡 API Endpoints](#-api-endpoints)
- [📁 Project Structure](#-project-structure)
- [🛠️ Tech Stack](#️-tech-stack)
- [🤝 Contributing](#-contributing)

---

## ✨ Features

### 🏠 Smart Dashboard

- Real-time overview of your baby's **hemoglobin levels**, **weight**, and **age**
- Color-coded health status badges (`HEALTHY` / `ANEMIC` / `OVERWEIGHT` / `UNDERWEIGHT`)
- Quick-access forms to **log a new feed** and **record health stats** without leaving the dashboard

### 🍼 Feeding Log

- Track every feed with **amount (mL)**, **type** (Formula / Breast Milk / Water), **timestamp**, and **notes**
- Chronological feed history with delete support
- Per-child feed isolation — switching children instantly filters data

### 🔔 Feeding Alarms

- Schedule precise feeding reminders with a **custom label** and **time (HH:mm)**
- View all active alarms in a clean card list
- Delete individual alarms with one click

### 📊 Health Records

- Record **hemoglobin levels (g/dL)** and **weight (kg)** over time
- Automated status determination:
  - Hemoglobin: `< 11.0` → ANEMIC · `11–14` → HEALTHY · `> 14.0` → HIGH
  - Weight (0–12 months): `< 7.0 kg` → UNDERWEIGHT · `> 12.0 kg` → OVERWEIGHT
- Full history view sorted by most recent

### 🔬 Formula Scanner

- Simulate scanning an infant formula product for allergens and BIS compliance
- Instant **Safe / Allergen Detected** result with detailed message
- 70% safe / 30% alert probability simulation (ready for real barcode integration)

### 👤 Profile Management

- Update **phone number** and **password** with confirmation validation
- Edit **child profile**: name, age, weight, gender, region
- **Privacy Shield** — masks child's name with initials across the UI

### 👶 Multi-Child Support

- Register **multiple child profiles** under one account
- Switch active child via a persistent dropdown in the navbar — all pages update instantly
- Session-based child selection with ownership validation

---

## 🖥️ Screenshots

> Pages included: Landing · Login · Register · Child Setup · Dashboard · History · Alarms · Profile · Scanner

| Dashboard                                     | Alarms                              | Health History                        |
| --------------------------------------------- | ----------------------------------- | ------------------------------------- |
| Real-time stats, feed logger, health recorder | Schedule & manage feeding reminders | Full feeding & health record timeline |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Browser (Thymeleaf SSR)               │
│         HTML + CSS (Glassmorphism) + Font Awesome        │
└──────────────────────┬──────────────────────────────────┘
                       │  HTTP
┌──────────────────────▼──────────────────────────────────┐
│                Spring Boot MVC Layer                     │
│                                                          │
│  WebController   FeedingController   OnboardingCtrl      │
│  HealthCtrl      ProfileController   DashboardCtrl       │
└──────────────┬───────────────────────────────────────────┘
               │
┌──────────────▼───────────────────────────────────────────┐
│                    Service Layer                          │
│                                                          │
│  SessionService     DashboardService     ScannerService  │
│  OnboardingService  CustomUserDetailsService             │
└──────────────┬───────────────────────────────────────────┘
               │
┌──────────────▼───────────────────────────────────────────┐
│               Repository Layer (Spring Data JPA)         │
│                                                          │
│  UserRepository          FeedingLogRepository            │
│  ChildProfileRepository  FeedingAlarmRepository          │
│  HealthRecordRepository                                  │
└──────────────┬───────────────────────────────────────────┘
               │
┌──────────────▼───────────────────────────────────────────┐
│                      MySQL 8.x                           │
│   users · child_profiles · feeding_logs                  │
│   feeding_alarms · health_records                        │
└──────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Schema

```sql
-- Core user account
users
  id            BIGINT PK AUTO_INCREMENT
  email         VARCHAR UNIQUE NOT NULL
  password      VARCHAR NOT NULL          -- BCrypt hashed
  phone_number  VARCHAR

-- One user → many children
child_profiles
  id                    BIGINT PK
  user_id               BIGINT FK → users.id
  name                  VARCHAR
  age_months            INT
  weight_kg             DOUBLE
  gender                VARCHAR
  country_region        VARCHAR
  privacy_shield_enabled BOOLEAN

-- Feeding events
feeding_logs
  id          BIGINT PK
  child_id    BIGINT FK → child_profiles.id
  amount_ml   DOUBLE
  type        VARCHAR          -- Formula / Breast Milk / Water
  timestamp   DATETIME
  note        VARCHAR

-- Scheduled reminders
feeding_alarms
  id          BIGINT PK
  child_id    BIGINT FK → child_profiles.id
  alarm_time  TIME
  label       VARCHAR
  enabled     BOOLEAN

-- Health measurements
health_records
  id                 BIGINT PK
  child_id           BIGINT FK → child_profiles.id
  hemoglobin_level   DOUBLE
  hemoglobin_status  VARCHAR    -- HEALTHY / ANEMIC / HIGH
  weight_kg          DOUBLE
  allergens          VARCHAR
  recorded_at        DATETIME
```

---

## 🔐 Security

- **Spring Security 6** with form-based login
- **BCrypt** password hashing (strength 10)
- **Session-scoped child selection** with ownership validation — users can only access their own children's data
- **CSRF disabled** (suitable for internal/demo use; enable for production)
- Public routes: `/`, `/login`, `/register`, `/style.css`, `/hero.png`
- All `/api/**`, `/dashboard`, `/alarms`, `/history`, `/profile`, `/scanner` routes require authentication

---

## 🚀 Getting Started

### Prerequisites

| Tool     | Version |
| -------- | ------- |
| Java JDK | 17+     |
| Maven    | 3.8+    |
| MySQL    | 8.x     |

### 1. Clone the repository

```bash
git clone https://github.com/your-username/InfantMilkCare.git
cd InfantMilkCare
```

### 2. Create the MySQL database

```sql
CREATE DATABASE infant_milk_care;
```

### 3. Configure credentials

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/infant_milk_care
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

Or on Windows:

```cmd
mvnw.cmd spring-boot:run
```

### 5. Open in browser

```
http://localhost:8081
```

> Hibernate will auto-create all tables on first run (`ddl-auto=update`).

---

## ☁️ Deploy to Render

> **Why PostgreSQL on Render?** Render's managed databases only support PostgreSQL (not MySQL). The app ships with both drivers — PostgreSQL is used in production, MySQL is used locally.

### Step 1 — Push to GitHub

```bash
git init            # if not already a git repo
git add .
git commit -m "Initial commit — ready for Render deployment"
git remote add origin https://github.com/YOUR_USERNAME/InfantMilkCare.git
git push -u origin main
```

### Step 2 — Create a Render account

Sign up at [render.com](https://render.com) (free tier available).

### Step 3 — New Web Service (Docker)

1. Click **New → Web Service**
2. Connect your GitHub account and select the **InfantMilkCare** repository
3. Render will detect the `Dockerfile` automatically
4. Set **Name** → `infantmilkcare`, **Plan** → `Free`

### Step 4 — Create a PostgreSQL Database

1. Click **New → PostgreSQL**
2. Set **Name** → `infantmilkcare-db`, **Plan** → `Free`
3. After creation, open the database and copy the **Internal Database URL**
   - It looks like: `postgres://user:pass@host:5432/dbname`

### Step 5 — Set Environment Variables

In your web service → **Environment** tab, add these variables:

| Key                 | Value                                                                        |
| ------------------- | ---------------------------------------------------------------------------- |
| `DB_HOST`           | Internal hostname from Render DB (e.g. `dpg-xxx.oregon-postgres.render.com`) |
| `DB_PORT`           | `5432`                                                                       |
| `DB_NAME`           | Your database name (shown in Render DB dashboard)                            |
| `DB_USERNAME`       | Your database user (shown in Render DB dashboard)                            |
| `DB_PASSWORD`       | Your database password (shown in Render DB dashboard)                        |
| `DB_DRIVER`         | `org.postgresql.Driver`                                                      |
| `HIBERNATE_DIALECT` | `org.hibernate.dialect.PostgreSQLDialect`                                    |
| `SHOW_SQL`          | `false`                                                                      |

> 💡 All these values are available in the Render PostgreSQL dashboard under **Connections**.

### Step 6 — Deploy

Click **Manual Deploy → Deploy latest commit**. Render will:

1. Pull your code from GitHub
2. Build the Docker image (Maven → JAR → Alpine JRE)
3. Run `start.sh` which constructs `DB_URL` and starts Spring Boot
4. Auto-create all tables via `ddl-auto=update`

Your app will be live at `https://infantmilkcare.onrender.com` 🎉

> ⚠️ **Free tier cold starts** — free Render services spin down after 15 minutes of inactivity. The first request after sleep takes ~30–60 seconds. Upgrade to **Starter** ($7/month) for always-on.

---

## ⚙️ Configuration

All configuration lives in `src/main/resources/application.properties`:

```properties
# Server
server.port=8081

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/infant_milk_care
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update       # use 'validate' in production
spring.jpa.show-sql=true                   # set false in production
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.open-in-view=true
```

---

## 📡 API Endpoints

### 🌐 Web Pages (GET)

| Route                | Description           | Auth |
| -------------------- | --------------------- | ---- |
| `GET /`              | Landing page          | ❌   |
| `GET /login`         | Login page            | ❌   |
| `GET /register`      | Registration page     | ❌   |
| `GET /dashboard`     | Main dashboard        | ✅   |
| `GET /alarms`        | Feeding alarms        | ✅   |
| `GET /history`       | Feed & health history | ✅   |
| `GET /scanner`       | Formula scanner       | ✅   |
| `GET /profile`       | User & child profile  | ✅   |
| `GET /child-details` | Add child onboarding  | ✅   |

### 🔧 Action Endpoints (POST)

| Route                                  | Description            | Auth |
| -------------------------------------- | ---------------------- | ---- |
| `POST /api/onboarding/register`        | Register new user      | ❌   |
| `POST /api/onboarding/child-details`   | Save child profile     | ✅   |
| `POST /api/feeding/logs`               | Log a new feed         | ✅   |
| `POST /api/feeding/logs/{id}/delete`   | Delete a feed log      | ✅   |
| `POST /api/feeding/alarms`             | Add a feeding alarm    | ✅   |
| `POST /api/feeding/alarms/{id}/delete` | Delete a feeding alarm | ✅   |
| `POST /api/health/record`              | Record health stats    | ✅   |
| `POST /api/profile/update-user`        | Update account info    | ✅   |
| `POST /api/profile/update-child`       | Update child profile   | ✅   |
| `POST /scanner/scan`                   | Run formula scan       | ✅   |
| `POST /switch-child`                   | Switch active child    | ✅   |
| `POST /login`                          | Authenticate user      | ❌   |
| `GET /logout`                          | Log out                | ✅   |

---

## 📁 Project Structure

```
InfantMilkCare/
├── src/
│   └── main/
│       ├── java/com/InfantMilk/Care/
│       │   ├── InfantMilkCareApplication.java   # Entry point
│       │   ├── config/
│       │   │   └── SecurityConfig.java           # Spring Security setup
│       │   ├── controller/
│       │   │   ├── WebController.java            # Page routing + model population
│       │   │   ├── FeedingController.java        # Feed logs & alarms CRUD
│       │   │   ├── HealthController.java         # Health record submission
│       │   │   ├── OnboardingController.java     # Registration & child setup
│       │   │   ├── ProfileController.java        # Profile update actions
│       │   │   └── DashboardController.java      # Dashboard data
│       │   ├── dto/
│       │   │   ├── RegistrationRequest.java
│       │   │   └── ChildDetailsRequest.java
│       │   ├── model/
│       │   │   ├── User.java
│       │   │   ├── ChildProfile.java
│       │   │   ├── FeedingLog.java
│       │   │   ├── FeedingAlarm.java
│       │   │   └── HealthRecord.java
│       │   ├── repository/
│       │   │   ├── UserRepository.java
│       │   │   ├── ChildProfileRepository.java
│       │   │   ├── FeedingLogRepository.java
│       │   │   ├── FeedingAlarmRepository.java
│       │   │   └── HealthRecordRepository.java
│       │   └── service/
│       │       ├── SessionService.java           # Auth context + child session
│       │       ├── DashboardService.java         # Weight & Hb status logic
│       │       ├── ScannerService.java           # Formula scan simulation
│       │       ├── OnboardingService.java        # User & child registration
│       │       └── CustomUserDetailsService.java # Spring Security integration
│       └── resources/
│           ├── application.properties
│           ├── templates/                        # Thymeleaf HTML templates
│           │   ├── index.html
│           │   ├── login.html
│           │   ├── register.html
│           │   ├── child-details.html
│           │   ├── dashboard.html
│           │   ├── alarms.html
│           │   ├── history.html
│           │   ├── scanner.html
│           │   └── profile.html
│           └── static/
│               ├── style.css                    # Global design system
│               └── hero.png                     # Dashboard hero image
└── pom.xml
```

---

## 🛠️ Tech Stack

| Layer           | Technology                                                            |
| --------------- | --------------------------------------------------------------------- |
| **Language**    | Java 17                                                               |
| **Framework**   | Spring Boot 4.0.6                                                     |
| **Web MVC**     | Spring Web MVC + Thymeleaf                                            |
| **Security**    | Spring Security 6 (BCrypt, Form Login)                                |
| **ORM**         | Spring Data JPA + Hibernate                                           |
| **Database**    | MySQL 8.x                                                             |
| **Build**       | Maven (Maven Wrapper included)                                        |
| **Boilerplate** | Lombok (`@Getter`, `@Setter`, `@Builder`, `@RequiredArgsConstructor`) |
| **UI**          | Vanilla CSS, Glassmorphism, Font Awesome 6, Google Fonts              |
| **Templating**  | Thymeleaf 3 with `#temporals` (Java 8 Time API)                       |

---

## 🩺 Health Status Logic

### Hemoglobin (g/dL)

| Range         | Status     |
| ------------- | ---------- |
| `< 11.0`      | 🔴 ANEMIC  |
| `11.0 – 14.0` | 🟢 HEALTHY |
| `> 14.0`      | 🟡 HIGH    |

### Weight (kg) — Age 0–12 months

| Range           | Status         |
| --------------- | -------------- |
| `< 7.0 kg`      | 🟡 UNDERWEIGHT |
| `7.0 – 12.0 kg` | 🟢 OPTIMAL     |
| `> 12.0 kg`     | 🔴 OVERWEIGHT  |

---

## 🔭 Roadmap

- [ ] Real barcode/QR scanner integration (ZXing or ML Kit)
- [ ] Push notifications for feeding alarms (Web Push API)
- [ ] PDF export for health reports
- [ ] Multi-language / i18n support
- [ ] Growth percentile charts (WHO standard curves)
- [ ] Doctor/caregiver shared access role
- [ ] Progressive Web App (PWA) support

---

## 🤝 Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

Please ensure your code compiles (`mvn compile`) before submitting.

---

## 📜 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

Made with ❤️ for parents everywhere.

**InfantMilkCare** — _Precision care for your little one._

</div>
