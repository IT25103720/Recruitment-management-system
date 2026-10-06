# LankaJobsHub

LankaJobsHub is a Spring Boot recruitment management web application with JSP views, user management, job postings, applications, interviews, notifications, analytics, and company profiles.

## Requirements

- Java 17
- Maven
- MySQL

## Configuration

The application reads database and JWT settings from environment variables:

```bash
export DB_URL="jdbc:mysql://localhost:3306/LankaJobsHub.com?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true"
export DB_USERNAME="root"
export DB_PASSWORD="your_mysql_password"
export JWT_SECRET="replace-with-a-strong-secret"
```

## Run

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080/lankajobshub
```
