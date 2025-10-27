# Student Management System - Backend

Hệ thống quản lý sinh viên được xây dựng bằng Spring Boot 3.2, PostgreSQL, Thymeleaf và Docker.

## 🚀 Cách chạy project

### Yêu cầu
- Docker Desktop
- JDK 17+ (nếu chạy local không dùng Docker)
- Maven 3.8+ (nếu chạy local)

### Chạy với Docker Compose (Khuyến nghị)

```powershell
# Build và chạy containers
docker-compose up -d --build

# Kiểm tra logs
docker-compose logs -f backend

# Dừng containers
docker-compose down

# Dừng và xóa volumes (reset database)
docker-compose down -v
```

**Services sau khi chạy:**
- **Web UI**: http://localhost:8080
- **Database**: localhost:5432

**Tài khoản đăng nhập:**
- Admin: `admin` / `admin123`
- User: `user1` / `user123`

**Dữ liệu mẫu ban đầu:**
- 6 Khoa
- 6 Lớp
- 12 Sinh viên
- 2 Tài khoản

## 📄 License

MIT License
