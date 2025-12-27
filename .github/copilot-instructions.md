<!--
  AI coding agent guidance for the Student Management System repository.
  Last updated: 2025-12-27
-->

# Copilot / AI agent instructions — Student Management System (Quản lý sinh viên)

A full-stack student management system built with **Spring Boot 3.2**, **Java 17**, **PostgreSQL**, **Thymeleaf** (server-side templating), and **Docker**.

## Quick facts

### Backend (`be/`)
- **Tech stack:** Spring Boot 3.2, Java 17, PostgreSQL 15, Thymeleaf, Apache POI, iText, Docker
- **Architecture:** Layered (Web/REST Controller → Service → Repository → Entity)
- **Security:** Session-based (Spring Security) + form login; role-based access (ADMIN/USER)
- **UI:** Server-side rendered Thymeleaf templates (fully functional web UI, not just REST API)
- **Key features:**
  - Complete CRUD for students (Sinh Viên), departments (Khoa), classes (Lớp)
  - Advanced search & pagination via Thymeleaf UI and REST API
  - Excel/PDF export with Apache POI and iText
  - Role-based authorization (ADMIN can edit, USER is read-only)
  - Docker multi-container (app + PostgreSQL)

### Frontend (`fe/`)
- Not yet implemented (placeholder for future standalone frontend)

## Key architectural decisions

### Hybrid UI approach (Thymeleaf + REST)
- **Web controllers** (`*WebController.java`): Serve Thymeleaf-rendered HTML pages with forms for CRUD operations
- **REST controllers** (`*Controller.java`): Provide JSON APIs for programmatic access
- **Sessions**: Uses session-based security (HttpSession), not JWT tokens
- Example: `SinhVienWebController` renders UI pages; `SinhVienController` provides REST endpoints for both

### Data flow: Database → Entity → DTO → View/JSON
1. **Entity layer** (`com.qlsv.entity.*`): JPA entities with relationships (Khoa→Lop→SinhVien)
2. **Repository layer**: Spring Data JPA with custom `@Query` methods
3. **Service layer**: Business logic and validation (e.g., `SinhVienService.searchSinhVien()`)
4. **Controller layer**: Convert to DTOs, return Thymeleaf models or JSON responses via `ApiResponse<T>`
5. **Templates** (`src/main/resources/templates/`): Thymeleaf HTML for web UI

### Security model
- **Session-based** (not JWT): Standard Spring Security with form login
- **Authentication**: `CustomUserDetailsService` loads users from database (role = ADMIN or USER)
- **Authorization**: `@PreAuthorize("hasRole('ADMIN')")` on REST endpoints; Thymeleaf `sec:authorize` in templates
- **Password hashing**: BCrypt via `PasswordEncoder` bean
- **Default credentials** (from DataLoader): `admin/admin123` (ADMIN), `user1/user123` (USER)

## Running the backend

```powershell
# Windows PowerShell with Docker (recommended)
cd be
docker-compose up -d --build
docker-compose logs -f backend  # Monitor logs
docker-compose down  # Stop containers
docker-compose down -v  # Stop and reset database

# Access points:
# - Web UI: http://localhost:8080 (login page)
# - REST API: http://localhost:8080/api/sinhvien, /api/khoa, /api/lop, etc.
# - Database: localhost:5432 (postgres/postgres123)
```

## Project structure essentials

**Core packages (follow this layering pattern):**
- `com.qlsv.entity.*` — JPA entities: `Khoa`, `Lop`, `SinhVien`, `TaiKhoan`, `BaoCao`
- `com.qlsv.repository.*` — Spring Data JPA repositories with `@Query` custom finders
- `com.qlsv.service.*` — Business logic: `SinhVienService`, `ExcelExportService`, `PdfExportService`
- `com.qlsv.controller.*` — REST controllers (REST endpoints)
- `com.qlsv.controller.*WebController` — Thymeleaf web controllers (HTML page rendering)
- `com.qlsv.security.*` — Spring Security config, custom user details service
- `com.qlsv.dto.*` — Transfer objects: `ApiResponse<T>`, `SinhVienDTO`, `KhoaDTO`, `LopDTO`
- `com.qlsv.exception.*` — Global exception handler and custom exceptions

**Template structure:**
- `templates/login.html` — Login form
- `templates/dashboard.html` — Dashboard (requires authentication)
- `templates/sinhvien/list.html` — List all students with search/filter
- `templates/sinhvien/form.html` — Add/Edit student
- `templates/sinhvien/view.html` — View student details
- `templates/khoa/list.html` — Departments
- `templates/lop/list.html` — Classes
- `templates/layout/main.html` — Master layout with navbar

## Critical developer workflows

### Build and run locally
```powershell
cd be
# Maven build (skip tests for faster iteration)
./mvnw clean package -DskipTests
./mvnw spring-boot:run

# Or run with Docker Compose
docker-compose up -d --build
docker-compose logs -f backend
```

### Reset database (development)
```powershell
cd be
docker-compose down -v  # Removes volumes, next startup re-initializes
docker-compose up -d --build
```

### Testing
- Check logs for errors: `docker-compose logs -f backend`
- Test REST endpoints via browser at `http://localhost:8080` (web UI) or with curl/Postman for API
- Database queries can be verified via PostgreSQL client at `localhost:5432`

### Deployment
- Docker is mandatory for production
- Environment variables are set in `docker-compose.yml` and injected into Spring config
- No hardcoded credentials — all config via `application.yml` placeholders

## When adding features

### Adding a new REST API endpoint

**Pattern:** Create entity → repository → service → REST controller (+ optional web controller for UI)

1. **Entity** (`com.qlsv.entity/MyEntity.java`):
```java
@Entity @Table(name = "my_entity") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MyEntity {
    @Id private String id;
    @NotBlank private String name;
    // Add validation annotations, foreign keys (@ManyToOne), relationships
}
```

2. **Repository** (`com.qlsv.repository/MyEntityRepository.java`):
```java
public interface MyEntityRepository extends JpaRepository<MyEntity, String> {
    Optional<MyEntity> findByName(String name);
    @Query("SELECT m FROM MyEntity m WHERE m.name LIKE %:keyword%")
    Page<MyEntity> search(String keyword, Pageable p);
}
```

3. **DTO** (`com.qlsv.dto/MyEntityDTO.java`):
```java
@Data @Builder public class MyEntityDTO {
    @NotBlank private String name;
    // Fields for API responses
}
```

4. **Service** (`com.qlsv.service/MyEntityService.java`):
```java
@Service @RequiredArgsConstructor public class MyEntityService {
    private final MyEntityRepository repo;
    public MyEntityDTO create(MyEntityDTO dto) { /* validation + save */ }
    public Page<MyEntityDTO> search(String keyword, Pageable p) { /* call repo */ }
}
```

5. **REST Controller** (`com.qlsv.controller/MyEntityController.java`):
```java
@RestController @RequestMapping("/api/myentity") @RequiredArgsConstructor
public class MyEntityController {
    private final MyEntityService service;
    @GetMapping public ApiResponse<List<MyEntityDTO>> getAll() { }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public ApiResponse<MyEntityDTO> create(@Valid @RequestBody MyEntityDTO dto) { }
}
```

6. **Optional: Web Controller** for HTML UI (same pattern with `@Controller`, return template names)

### Adding search filters

Edit the repository `@Query` and update service signature:
```java
// Repository
@Query("SELECT s FROM SinhVien s WHERE (:keyword IS NULL OR s.hoTen LIKE %:keyword%) AND (:maKhoa IS NULL OR s.lop.khoa.maKhoa = :maKhoa)")
Page<SinhVien> searchSinhVien(@Param("keyword") String kw, @Param("maKhoa") String kh, Pageable p);

// Service
public Page<SinhVienDTO> searchSinhVien(String kw, String kh, Pageable p) { return repo.searchSinhVien(kw, kh, p).map(mapper); }
```

### Adding export functionality

Extend `ExcelExportService` or `PdfExportService` pattern:
```java
// Service method
public byte[] exportToExcel(List<SinhVienDTO> data) {
    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet("Sinh Viên");
    // Create headers, populate rows, style
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    wb.write(out);
    wb.close();
    return out.toByteArray();
}

// Controller endpoint
@GetMapping("/excel/all") @PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<byte[]> exportExcel() {
    byte[] data = exportService.exportToExcel(sinhVienService.getAll());
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=export.xlsx")
        .body(data);
}
```

## Code style and conventions

**Naming:**
- Entity field names: snake_case in database (`ma_sv`, `ho_ten`, `ngay_sinh`), camelCase in Java code
- Always use Lombok: `@Data`, `@Builder`, `@RequiredArgsConstructor`, `@NoArgsConstructor`, `@AllArgsConstructor`
- All log messages and user-facing text in **Vietnamese** for consistency

**Validation:**
- Add `@NotBlank`, `@NotNull`, `@Valid` to DTO fields
- Custom validation in Service layer (e.g., check unique `maSV` before insert)
- Return validation errors via `GlobalExceptionHandler` → `ApiResponse<T>` with `success=false`

**API Responses:**
Always wrap responses in `ApiResponse<T>`:
```java
ApiResponse.success("Thêm thành công", sinhVienDTO)
ApiResponse.error("Mã sinh viên đã tồn tại")
```

**Pagination:**
- Use `Pageable` parameter in controller: `@RequestParam(defaultValue = "0") int page`
- Return `Page<DTO>` from service, Thymeleaf templates handle pagination UI

**Database:**
- Use `@CreatedDate`, `@LastModifiedDate` for audit fields (enable `@EnableJpaAuditing` in config)
- Add `@Index` on frequently searched columns: `@Index(name = "idx_ho_ten", columnList = "ho_ten")`
- Foreign key naming: `ma_khoa`, `ma_lop`, `ma_sv` (consistent with business domain)

## What NOT to assume or change

- **Do not** change database schema without updating both entity annotations and any related repositories/services
- **Do not** remove or modify existing security configurations without understanding session-based flow
- **Do not** change the `ApiResponse<T>` wrapper structure (it's used consistently across all endpoints)
- **Do not** modify Docker environment variables in `docker-compose.yml` without updating `application.yml` accordingly
- **Do not** assume frontend is implemented — it's a placeholder for future work
- **Do not** mix English and Vietnamese in user-facing messages (stick to Vietnamese for consistency)
- **Do not** use JWT — this project uses session-based authentication (Spring Security HttpSession)
- **Do not** remove Thymeleaf web controllers — they serve the actual HTML UI, not just APIs

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
- **Session/Auth errors:** Verify user credentials in DataLoader, check SecurityConfig session settings
- **Validation errors:** Check `GlobalExceptionHandler` logs and ensure DTO fields match validation rules
- **Export errors:** Verify Apache POI and iText dependencies in `pom.xml`, check logs for font/encoding issues
- **Thymeleaf template errors:** Check logs for missing templates or incorrect variable binding in controller models

---

**Last updated:** 2025-12-27 after verifying actual implementation (session-based auth, Thymeleaf UI, hybrid REST/Web controllers)
