# Student Management System - Backend

Hệ thống quản lý sinh viên được xây dựng bằng Spring Boot 3.2, PostgreSQL, Thymeleaf và Docker.

## 📋 Tính năng chính

### Giao diện Web (Thymeleaf)
- ✅ Giao diện web đầy đủ với Bootstrap 5
- ✅ Đăng nhập/Đăng xuất (form-based authentication)
- ✅ Dashboard với thống kê và biểu đồ
- ✅ Quản lý sinh viên qua giao diện web
- ✅ Tìm kiếm, thêm, sửa, xóa sinh viên
- ✅ Phân quyền hiển thị (ADMIN/USER)
- ✅ Responsive design (mobile/tablet/desktop)

### REST API (JWT Authentication)
- ✅ RESTful API đầy đủ cho client khác
- ✅ Xác thực JWT (JSON Web Token)
- ✅ CRUD operations qua API
- ✅ Swagger/OpenAPI documentation

### Quản lý sinh viên
- ✅ CRUD sinh viên (thêm, sửa, xóa, xem)
- ✅ Tìm kiếm và lọc sinh viên theo nhiều tiêu chí
- ✅ Phân trang và sắp xếp
- ✅ Quản lý khoa/lớp

### Bảo mật
- ✅ Dual authentication: Form-based (web) + JWT (API)
- ✅ Phân quyền: ADMIN (CRUD) và USER (Read-only)
- ✅ Mã hóa mật khẩu (BCrypt)
- ✅ Session management cho web UI

### Xuất báo cáo
- ✅ Xuất danh sách sinh viên ra Excel (Apache POI)
- ✅ Xuất danh sách sinh viên ra PDF (iText)
- ✅ Lọc theo khoa, lớp

## 🛠️ Công nghệ sử dụng

- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - **Thymeleaf** (Server-side template engine)
  - **Thymeleaf Spring Security 6** (Security integration)
- **PostgreSQL 15**
- **JWT (io.jsonwebtoken)** - API authentication
- **Lombok**
- **Apache POI** (Excel export)
- **iText** (PDF export)
- **Bootstrap 5** (UI framework)
- **Chart.js** (Dashboard charts)
- **Docker & Docker Compose**

## 📦 Cấu trúc project

```
be/
├── src/
│   └── main/
│       ├── java/com/qlsv/
│       │   ├── controller/      # REST API Controllers & Web Controllers
│       │   │   ├── AuthController.java      # API: /api/auth/*
│       │   │   ├── SinhVienController.java  # API: /api/sinhvien/*
│       │   │   ├── ExportController.java    # API: /api/export/*
│       │   │   ├── HomeController.java      # Web: /, /login, /dashboard
│       │   │   ├── SinhVienWebController.java # Web: /sinhvien/*
│       │   │   ├── KhoaController.java      # API: /api/khoa/*
│       │   │   └── LopController.java       # API: /api/lop/*
│       │   ├── dto/             # Data Transfer Objects
│       │   ├── entity/          # JPA Entities
│       │   ├── exception/       # Exception Handlers
│       │   ├── repository/      # JPA Repositories
│       │   ├── security/        # Security Config, JWT
│       │   ├── service/         # Business Logic
│       │   └── StudentManagementApplication.java
│       └── resources/
│           ├── application.yml  # Configuration
│           └── templates/       # Thymeleaf Templates
│               ├── layout/
│               │   └── main.html       # Base layout
│               ├── login.html          # Login page
│               ├── dashboard.html      # Dashboard with charts
│               └── sinhvien/
│                   ├── list.html       # Student list
│                   ├── form.html       # Add/Edit form
│                   └── view.html       # Detail view
├── Dockerfile
├── docker-compose.yml
├── init-db.sql
├── pom.xml
├── README.md
├── API_DOCUMENTATION.md
├── QUICK_TEST.md
└── THYMELEAF_TESTING.md    # Hướng dẫn test Thymeleaf
```

## 🚀 Cách chạy project

### Yêu cầu
- Docker Desktop
- JDK 17+ (nếu chạy local không dùng Docker)
- Maven 3.8+ (nếu chạy local)

### Chạy với Docker Compose (Khuyến nghị)

```powershell
# 1. Di chuyển vào thư mục be
cd d:\WorkSpace\Lab\thuctapcoso\be

# 2. Build và chạy containers
docker-compose up -d --build

# 3. Kiểm tra logs
docker-compose logs -f backend

# 4. Dừng containers
docker-compose down

# 5. Dừng và xóa volumes (reset database)
docker-compose down -v
```

**Services sau khi chạy:**
- **Web UI**: http://localhost:8080 (Thymeleaf interface)
- **Backend API**: http://localhost:8080/api (RESTful API)
- **PostgreSQL**: localhost:5432
- **pgAdmin**: http://localhost:5050 (email: admin@qlsv.com, password: admin123)

### Chạy local (không Docker)

```powershell
# 1. Cài PostgreSQL và tạo database
createdb qlsv_db

# 2. Cập nhật application.yml với thông tin database local

# 3. Build project
./mvnw clean package -DskipTests

# 4. Chạy application
./mvnw spring-boot:run

# Hoặc chạy jar file
java -jar target/student-management-1.0.0.jar
```

## 🌐 Sử dụng Web UI (Thymeleaf)

### 1. Truy cập ứng dụng
Sau khi chạy project, mở trình duyệt và truy cập: **http://localhost:8080**

### 2. Đăng nhập
Hệ thống sẽ redirect đến trang đăng nhập. Sử dụng tài khoản mặc định:

**Tài khoản Admin (có quyền CRUD):**
- Username: `admin`
- Password: `admin123`

**Tài khoản User (chỉ xem):**
- Username: `user1`
- Password: `user123`

### 3. Dashboard
Sau khi đăng nhập, bạn sẽ thấy:
- **Thống kê tổng quan:** Số sinh viên, khoa, lớp, GPA trung bình
- **Biểu đồ:** Sinh viên theo khoa, theo giới tính (Chart.js)
- **Danh sách gần đây:** 5 sinh viên mới nhất
- **Menu sidebar:** Dashboard, Sinh viên, Khoa, Lớp, Xuất báo cáo

### 4. Quản lý sinh viên

#### Xem danh sách
- Click **"Sinh viên"** trên sidebar
- Hỗ trợ tìm kiếm theo: Từ khóa (mã SV, họ tên, email), Khoa, Lớp, Giới tính
- Phân trang tự động

#### Thêm sinh viên (Admin only)
1. Click nút **"Thêm sinh viên"**
2. Điền form với các trường bắt buộc:
   - Mã SV, Họ tên, Ngày sinh, Giới tính, Email
   - Khoa → Lớp (danh sách lớp tự động load theo khoa)
   - GPA (0.00-4.00), Trạng thái
3. Click **"Thêm mới"**

#### Sửa sinh viên (Admin only)
- Click icon ✏️ **"Sửa"** trên bảng danh sách
- Form tương tự thêm mới, nhưng mã SV không sửa được
- Click **"Cập nhật"**

#### Xem chi tiết
- Click icon 👁️ **"Xem"** để xem thông tin đầy đủ
- Hiển thị 2 cột: Thông tin cá nhân & Thông tin học tập

#### Xóa sinh viên (Admin only)
- Click icon 🗑️ **"Xóa"**
- Confirm dialog → Xóa vĩnh viễn

### 5. Xuất báo cáo
- Click **"Xuất Excel"** hoặc **"Xuất PDF"** trên sidebar/dashboard
- File tải về tự động: `DanhSachSinhVien_YYYYMMDD_HHMMSS.xlsx/pdf`

### 6. Đăng xuất
- Click nút **"Đăng xuất"** ở góc trên bên phải
- Session sẽ bị clear và redirect về trang đăng nhập

**Xem hướng dẫn test chi tiết:** [THYMELEAF_TESTING.md](./THYMELEAF_TESTING.md)

## 📡 API Endpoints

### Authentication
```
POST   /api/auth/login       # Đăng nhập
POST   /api/auth/register    # Đăng ký
```

### Sinh viên (cần JWT token)
```
GET    /api/sinhvien                    # Lấy tất cả sinh viên
GET    /api/sinhvien/{maSV}             # Lấy thông tin sinh viên
POST   /api/sinhvien                    # Thêm sinh viên (ADMIN)
PUT    /api/sinhvien/{maSV}             # Cập nhật sinh viên (ADMIN)
DELETE /api/sinhvien/{maSV}             # Xóa sinh viên (ADMIN)
GET    /api/sinhvien/search             # Tìm kiếm sinh viên
GET    /api/sinhvien/lop/{maLop}        # Lấy sinh viên theo lớp
GET    /api/sinhvien/khoa/{maKhoa}      # Lấy sinh viên theo khoa
```

### Xuất báo cáo
```
GET    /api/export/excel/all            # Xuất tất cả sinh viên ra Excel
GET    /api/export/excel/khoa/{maKhoa}  # Xuất sinh viên theo khoa ra Excel
GET    /api/export/excel/lop/{maLop}    # Xuất sinh viên theo lớp ra Excel
GET    /api/export/pdf/all              # Xuất tất cả sinh viên ra PDF
GET    /api/export/pdf/khoa/{maKhoa}    # Xuất sinh viên theo khoa ra PDF
GET    /api/export/pdf/lop/{maLop}      # Xuất sinh viên theo lớp ra PDF
```

## 🧪 Test API với curl/PowerShell

### 1. Đăng nhập (lấy JWT token)

```powershell
$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"admin123"}'

$token = $loginResponse.data.token
Write-Host "Token: $token"
```

### 2. Thêm sinh viên

```powershell
$headers = @{
  "Authorization" = "Bearer $token"
  "Content-Type" = "application/json"
}

$sinhVien = @{
  maSV = "SV001"
  hoTen = "Nguyen Van A"
  ngaySinh = "2002-01-15"
  gioiTinh = "Nam"
  email = "nguyenvana@gmail.com"
  soDienThoai = "0123456789"
  diaChi = "Ha Noi"
  gpa = 3.5
  trangThai = "DANG_HOC"
  maLop = "CNTT2020A"
  maKhoa = "CNTT"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" `
  -Method POST `
  -Headers $headers `
  -Body $sinhVien
```

### 3. Tìm kiếm sinh viên

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/search?hoTen=Nguyen&page=0&size=10" `
  -Method GET `
  -Headers $headers
```

### 4. Xuất Excel

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/export/excel/all" `
  -Method GET `
  -Headers $headers `
  -OutFile "DanhSachSinhVien.xlsx"
```

## 🔐 Tài khoản mặc định

```
Admin:
- Username: admin
- Password: admin123
- Vai trò: ADMIN

User:
- Username: user1  
- Password: user123
- Vai trò: USER
```

## 📊 Database Schema

```
KHOA (ma_khoa, ten_khoa, mo_ta)
  └── LOP (ma_lop, ten_lop, khoa_hoc, si_so, ma_khoa FK)
      └── SINH_VIEN (ma_sv, ho_ten, ngay_sinh, gioi_tinh, email, 
                      so_dien_thoai, dia_chi, gpa, trang_thai,
                      ma_lop FK, ma_khoa FK)
          └── TAI_KHOAN (username, password_hash, vai_tro, ma_sv FK)
          └── BAO_CAO (ma_bc, loai, ngay_lap, dinh_dang, ma_sv FK)
```

## 🐛 Troubleshooting

### Lỗi kết nối database
```powershell
# Kiểm tra PostgreSQL đang chạy
docker-compose ps

# Xem logs database
docker-compose logs postgres

# Restart services
docker-compose restart
```

### Lỗi port đã được sử dụng
```powershell
# Thay đổi port trong docker-compose.yml
# Ví dụ: "8081:8080" thay vì "8080:8080"
```

### Reset database
```powershell
docker-compose down -v
docker-compose up -d
```

## 📝 TODO / Mở rộng

- [ ] Thêm Swagger/OpenAPI documentation
- [ ] Thêm unit tests và integration tests
- [ ] Thêm caching với Redis
- [ ] Thêm file upload cho ảnh sinh viên
- [ ] Thêm email notifications
- [ ] Thêm audit logging
- [ ] CI/CD pipeline

## 📄 License

MIT License

## 👥 Liên hệ

Dự án thực tập cơ sở - Hệ thống quản lý sinh viên
