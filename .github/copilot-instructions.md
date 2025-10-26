<!--
  AI coding agent guidance for the Student Management System repository.
  Last updated: 2024-10-26
-->

# Copilot / AI agent instructions — Student Management System (Quản lý sinh viên)

This is a full-stack student management system with:

- `be/` — Spring Boot 3.2 backend (Java 17, PostgreSQL, JWT auth, Docker)
- `fe/` — frontend (to be implemented)

The backend is **fully functional** with REST APIs, database, authentication, export features, and Docker deployment.

## Quick facts (from repository scan)

### Backend (`be/`)
- **Tech stack:** Spring Boot 3.2.0, Java 17, PostgreSQL 15, Docker
- **Architecture:** Layered (Controller → Service → Repository → Entity)
- **Security:** JWT authentication with role-based access (ADMIN, USER)
- **Key features:**
  - CRUD operations for students, classes, departments
  - Advanced search with pagination
  - Export to Excel (Apache POI) and PDF (iText)
  - Dockerized deployment with docker-compose
- **Main files:**
  - `pom.xml` — Maven dependencies
  - `src/main/resources/application.yml` — Configuration
  - `docker-compose.yml` — Multi-container setup (app + PostgreSQL + pgAdmin)
  - `be/README.md` — Comprehensive setup guide
  - `be/API_DOCUMENTATION.md` — Complete API reference
  - `be/QUICK_TEST.md` — Step-by-step testing guide

### Frontend (`fe/`)
- Not yet implemented (placeholder for React/Vue/Angular frontend)

## What an AI agent should do when working on this project

### For Backend (`be/`) Development

1. **Read first:**
   - `be/README.md` — Setup, architecture, and commands
   - `be/API_DOCUMENTATION.md` — All REST endpoints, request/response formats
   - `be/QUICK_TEST.md` — How to test the system

2. **Running the backend:**
   ```powershell
   cd be
   .\start.ps1  # Windows PowerShell (recommended)
   # OR
   docker-compose up -d --build
   ```
   Services: http://localhost:8080/api (backend), http://localhost:5050 (pgAdmin)

3. **Project structure to understand:**
   - `com.qlsv.entity.*` — JPA entities (Khoa, Lop, SinhVien, TaiKhoan, BaoCao) with full relationships
   - `com.qlsv.repository.*` — Spring Data JPA repos with custom queries
   - `com.qlsv.service.*` — Business logic, including Excel/PDF export services
   - `com.qlsv.controller.*` — REST controllers with role-based security
   - `com.qlsv.security.*` — JWT config, filters, Spring Security setup
   - `com.qlsv.dto.*` — Data transfer objects for API requests/responses

4. **When adding new features:**
   - Follow the existing layered pattern: DTO → Controller → Service → Repository → Entity
   - Use `@PreAuthorize("hasRole('ADMIN')")` for admin-only endpoints
   - Add validation annotations (`@NotBlank`, `@Valid`, etc.) to DTOs
   - Update `API_DOCUMENTATION.md` with new endpoints
   - Add tests to `QUICK_TEST.md` if introducing new workflows

5. **Testing changes:**
   - Use PowerShell scripts in `QUICK_TEST.md` for quick API tests
   - Or use `test-data.http` for REST client testing
   - Check logs: `docker-compose logs -f backend`

### For Frontend (`fe/`) Development (future)

- When implementing, integrate with backend API at `http://localhost:8080/api`
- Use JWT token from `/auth/login` in `Authorization: Bearer <token>` header
- Refer to `be/API_DOCUMENTATION.md` for complete endpoint specs

## Architecture and patterns specific to this codebase

### Backend Architecture (3-tier layered)

```
Client/Frontend
    ↓ HTTP/REST
Controller Layer (AuthController, SinhVienController, ExportController)
    ↓ DTOs
Service Layer (SinhVienService, ExcelExportService, PdfExportService)
    ↓ Entities
Repository Layer (Spring Data JPA - KhoaRepository, LopRepository, SinhVienRepository, etc.)
    ↓ JDBC
PostgreSQL Database
```

**Key architectural decisions:**

1. **Entity relationships (JPA):**
   - `Khoa` (1) → (n) `Lop` → (n) `SinhVien`
   - `SinhVien` (1) ← (1) `TaiKhoan`
   - `SinhVien` (1) → (n) `BaoCao`
   - Use `@ManyToOne(fetch = FetchType.LAZY)` for performance
   - Builder pattern with Lombok for entity construction

2. **Security flow:**
   - JWT token generated on `/auth/login`
   - `JwtAuthenticationFilter` validates token on every request
   - `SecurityConfig` defines role-based access (ADMIN can CRUD, USER can read-only)
   - Passwords hashed with BCrypt

3. **Export pattern:**
   - Separate service classes: `ExcelExportService`, `PdfExportService`
   - Return `byte[]` from service, convert to file download in controller
   - Filename includes timestamp: `DanhSachSinhVien_20241026_103000.xlsx`

4. **Error handling:**
   - `@RestControllerAdvice` with `GlobalExceptionHandler`
   - Custom exceptions: `ResourceNotFoundException`
   - Validation errors auto-formatted from `@Valid` annotations
   - Standard response wrapper: `ApiResponse<T>`

5. **Search and pagination:**
   - Use `@Query` with `Pageable` for complex searches
   - `SinhVienRepository.searchSinhVien(...)` accepts multiple optional filters
   - Return `Page<SinhVienDTO>` for frontend pagination support

## Project-specific conventions and patterns

### Code Style
- Use **Lombok** annotations (`@Data`, `@Builder`, `@RequiredArgsConstructor`) to reduce boilerplate
- Entity field names in **snake_case** for database columns: `ma_sv`, `ho_ten`, `ngay_sinh`
- Java class/method names in **camelCase**: `maSV`, `hoTen`, `ngaySinh`
- All log messages in Vietnamese for consistency with business domain

### Validation Rules
- Mã sinh viên (Student ID): max 20 chars, unique, required
- Email: must be valid format, unique across students
- Phone: 10-11 digits, regex validated
- GPA: 0.0 to 4.0 scale
- Date of birth: must be in the past
- Gender: "Nam", "Nữ", or "Khác"

### Database Conventions
- Use `@CreatedDate` and `@LastModifiedDate` for audit fields (enable with `@EnableJpaAuditing`)
- Foreign keys named consistently: `ma_khoa`, `ma_lop`, `ma_sv`
- Indexes on frequently searched columns: `@Index` on `ho_ten`, `ma_lop`, `ma_khoa`

### API Response Format
Always wrap in `ApiResponse<T>`:
```json
{
  "success": true/false,
  "message": "Vietnamese message",
  "data": <actual data>,
  "timestamp": "ISO-8601 datetime"
}
```

### Docker Setup
- Multi-container: `backend`, `postgres`, `pgadmin` (optional)
- Environment variables in `docker-compose.yml` for config
- Health checks ensure postgres is ready before backend starts
- Volumes persist database data across restarts

## Examples: Common tasks in this codebase

### 1. Adding a new REST endpoint for a new entity

**Step 1:** Create entity in `com.qlsv.entity`:
```java
@Entity
@Table(name = "table_name")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MyEntity {
    @Id
    private String id;
    // fields with validation annotations
}
```

**Step 2:** Create repository in `com.qlsv.repository`:
```java
public interface MyEntityRepository extends JpaRepository<MyEntity, String> {
    Optional<MyEntity> findByField(String field);
}
```

**Step 3:** Create DTO in `com.qlsv.dto`:
```java
@Data @Builder
public class MyEntityDTO {
    @NotBlank private String field;
}
```

**Step 4:** Create service in `com.qlsv.service`:
```java
@Service @RequiredArgsConstructor
public class MyEntityService {
    private final MyEntityRepository repository;
    // CRUD methods
}
```

**Step 5:** Create controller in `com.qlsv.controller`:
```java
@RestController
@RequestMapping("/myentity")
@PreAuthorize("hasRole('ADMIN')")
public class MyEntityController {
    private final MyEntityService service;
    // endpoints
}
```

### 2. Adding a new export format

Create a new service extending the pattern in `ExcelExportService.java` or `PdfExportService.java`, then add endpoint to `ExportController`.

### 3. Running backend after code changes

```powershell
# If using Docker (recommended)
docker-compose down
docker-compose up -d --build

# If running locally
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

### 4. Adding a new search filter

Edit `SinhVienRepository.searchSinhVien()` query to include new parameter, then update controller and service signatures.

## What NOT to assume or change

- **Do not** change database schema without updating both entity annotations and any related repositories/services
- **Do not** remove or modify existing security configurations without understanding JWT flow
- **Do not** change the `ApiResponse<T>` wrapper structure (it's used consistently across all endpoints)
- **Do not** modify Docker environment variables in `docker-compose.yml` without updating `application.yml` accordingly
- **Do not** assume frontend is implemented — it's a placeholder for future work
- **Do not** mix English and Vietnamese in user-facing messages (stick to Vietnamese for consistency)

## Key files to reference

- **Setup & Architecture:** `be/README.md`
- **API Reference:** `be/API_DOCUMENTATION.md`
- **Testing Guide:** `be/QUICK_TEST.md`
- **Dependencies:** `be/pom.xml`
- **Configuration:** `be/src/main/resources/application.yml`
- **Docker:** `be/docker-compose.yml`, `be/Dockerfile`
- **Security:** `be/src/main/java/com/qlsv/security/SecurityConfig.java`
- **Main entities:** `be/src/main/java/com/qlsv/entity/SinhVien.java` (student), `Khoa.java` (department), `Lop.java` (class)

## Debugging tips

- **Backend not starting:** Check `docker-compose logs -f backend` for errors
- **Database connection issues:** Ensure postgres container is healthy: `docker-compose ps`
- **JWT errors:** Verify token hasn't expired (24h validity), re-login if needed
- **Validation errors:** Check `GlobalExceptionHandler` logs and ensure DTO fields match validation rules
- **Export errors:** Verify Apache POI and iText dependencies in `pom.xml`, check logs for font/encoding issues

---

**Last updated:** 2024-10-26 after full backend implementation with Docker, JWT auth, CRUD APIs, search, and export features.
