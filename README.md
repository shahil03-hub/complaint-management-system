# Complaint Management System

Java 17 · Spring Boot 3 · Servlets · JDBC · PostgreSQL · HTML

## Features
- Submit a complaint (index.html)
- Track a complaint by ID (track.html)
- Admin: list, filter by status, update status, delete (admin.html)

## Structure
- `model/Complaint`            - entity
- `dao/ComplaintDao`           - plain JDBC (PreparedStatement) data access
- `servlet/*`                  - 4 servlets (@WebServlet) exposing JSON endpoints
- `static/*.html, style.css`   - front end
- `resources/schema.sql`       - table created automatically on startup

## Setup
1. Install JDK 17+, Maven and PostgreSQL.
2. Create the database:
   psql -U postgres -c "CREATE DATABASE complaint_db;"
3. Edit `src/main/resources/application.properties` (url / username / password).
4. Run:
   mvn spring-boot:run
5. Open http://localhost:8080

## Endpoints
| Method | URL                         | Params                                       |
|--------|-----------------------------|----------------------------------------------|
| POST   | /api/complaints/submit      | name, email, category, subject, description  |
| GET    | /api/complaints             | optional `status` or `id`                    |
| POST   | /api/complaints/status      | id, status                                   |
| POST   | /api/complaints/delete      | id                                           |

Statuses: PENDING, IN_PROGRESS, RESOLVED, REJECTED

Note: the admin page has no login. Add Spring Security or a session check before real use.
