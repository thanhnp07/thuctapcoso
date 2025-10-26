# API Documentation - Student Management System

## Base URL
```
http://localhost:8080/api
```

## Authentication

Hệ thống sử dụng JWT (JSON Web Token) để xác thực. Token có hiệu lực 24 giờ.

### Headers cho các API cần xác thực
```
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

---

## 🔐 Authentication Endpoints

### 1. Đăng nhập
```http
POST /auth/login
```

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đăng nhập thành công",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "admin",
    "vaiTro": "ADMIN",
    "maSV": null,
    "hoTen": null
  },
  "timestamp": "2024-10-26T10:30:00"
}
```

### 2. Đăng ký
```http
POST /auth/register
```

**Request Body:**
```json
{
  "username": "newuser",
  "passwordHash": "password123",
  "vaiTro": "USER"
}
```

---

## 👨‍🎓 Sinh Viên Endpoints

### 1. Lấy tất cả sinh viên
```http
GET /sinhvien
```
**Quyền:** ADMIN, USER

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "maSV": "SV001",
      "hoTen": "Nguyễn Văn An",
      "ngaySinh": "2002-01-15",
      "gioiTinh": "Nam",
      "email": "nguyenvanan@gmail.com",
      "soDienThoai": "0123456789",
      "diaChi": "Hà Nội",
      "gpa": 3.5,
      "trangThai": "DANG_HOC",
      "maLop": "CNTT2020A",
      "tenLop": "Công nghệ thông tin 2020A",
      "maKhoa": "CNTT",
      "tenKhoa": "Công nghệ thông tin"
    }
  ]
}
```

### 2. Lấy thông tin sinh viên theo mã
```http
GET /sinhvien/{maSV}
```
**Quyền:** ADMIN, USER

**Example:** `GET /sinhvien/SV001`

### 3. Thêm sinh viên mới
```http
POST /sinhvien
```
**Quyền:** ADMIN only

**Request Body:**
```json
{
  "maSV": "SV001",
  "hoTen": "Nguyễn Văn An",
  "ngaySinh": "2002-01-15",
  "gioiTinh": "Nam",
  "email": "nguyenvanan@gmail.com",
  "soDienThoai": "0123456789",
  "diaChi": "Hà Nội",
  "gpa": 3.5,
  "trangThai": "DANG_HOC",
  "maLop": "CNTT2020A",
  "maKhoa": "CNTT"
}
```

**Validation:**
- `maSV`: Required, max 20 ký tự
- `hoTen`: Required, max 100 ký tự
- `ngaySinh`: Required, phải là ngày trong quá khứ
- `gioiTinh`: "Nam", "Nữ", hoặc "Khác"
- `email`: Format email hợp lệ
- `soDienThoai`: 10-11 chữ số
- `gpa`: 0.0 - 4.0
- `maLop`: Required
- `maKhoa`: Required

### 4. Cập nhật sinh viên
```http
PUT /sinhvien/{maSV}
```
**Quyền:** ADMIN only

**Request Body:** (giống POST, các trường không bắt buộc)

### 5. Xóa sinh viên
```http
DELETE /sinhvien/{maSV}
```
**Quyền:** ADMIN only

### 6. Tìm kiếm sinh viên (phân trang)
```http
GET /sinhvien/search?hoTen=Nguyen&maKhoa=CNTT&page=0&size=10&sortBy=maSV&direction=ASC
```
**Quyền:** ADMIN, USER

**Query Parameters:**
- `maSV` (optional): Tìm chính xác theo mã SV
- `hoTen` (optional): Tìm gần đúng theo họ tên
- `maLop` (optional): Lọc theo lớp
- `maKhoa` (optional): Lọc theo khoa
- `gioiTinh` (optional): Lọc theo giới tính
- `trangThai` (optional): Lọc theo trạng thái
- `page` (default: 0): Trang số
- `size` (default: 10): Số lượng/trang
- `sortBy` (default: maSV): Trường sắp xếp
- `direction` (default: ASC): ASC hoặc DESC

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [...],
    "totalElements": 50,
    "totalPages": 5,
    "size": 10,
    "number": 0
  }
}
```

### 7. Lấy sinh viên theo lớp
```http
GET /sinhvien/lop/{maLop}
```
**Quyền:** ADMIN, USER

**Example:** `GET /sinhvien/lop/CNTT2020A`

### 8. Lấy sinh viên theo khoa
```http
GET /sinhvien/khoa/{maKhoa}
```
**Quyền:** ADMIN, USER

**Example:** `GET /sinhvien/khoa/CNTT`

---

## 📊 Export Endpoints

### Excel Export

#### 1. Xuất tất cả sinh viên
```http
GET /export/excel/all
```
**Quyền:** ADMIN, USER
**Response:** File Excel (.xlsx)

#### 2. Xuất sinh viên theo khoa
```http
GET /export/excel/khoa/{maKhoa}
```
**Example:** `GET /export/excel/khoa/CNTT`

#### 3. Xuất sinh viên theo lớp
```http
GET /export/excel/lop/{maLop}
```
**Example:** `GET /export/excel/lop/CNTT2020A`

### PDF Export

#### 1. Xuất tất cả sinh viên
```http
GET /export/pdf/all
```
**Quyền:** ADMIN, USER
**Response:** File PDF

#### 2. Xuất sinh viên theo khoa
```http
GET /export/pdf/khoa/{maKhoa}
```

#### 3. Xuất sinh viên theo lớp
```http
GET /export/pdf/lop/{maLop}
```

---

## 📝 Response Format

### Success Response
```json
{
  "success": true,
  "message": "Thao tác thành công",
  "data": {...},
  "timestamp": "2024-10-26T10:30:00"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Mô tả lỗi",
  "data": null,
  "timestamp": "2024-10-26T10:30:00"
}
```

### Validation Error Response
```json
{
  "success": false,
  "message": "Lỗi xác thực dữ liệu",
  "data": {
    "hoTen": "Họ tên không được để trống",
    "email": "Email không hợp lệ"
  },
  "timestamp": "2024-10-26T10:30:00"
}
```

---

## 🔒 Authorization Matrix

| Endpoint | ADMIN | USER |
|----------|-------|------|
| POST /sinhvien | ✅ | ❌ |
| PUT /sinhvien/{id} | ✅ | ❌ |
| DELETE /sinhvien/{id} | ✅ | ❌ |
| GET /sinhvien/** | ✅ | ✅ |
| GET /export/** | ✅ | ✅ |

---

## 🧪 Testing với PowerShell

### Setup
```powershell
# Đăng nhập và lưu token
$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"admin123"}'

$token = $loginResponse.data.token

$headers = @{
  "Authorization" = "Bearer $token"
  "Content-Type" = "application/json"
}
```

### Test tìm kiếm
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/search?hoTen=Nguyen" `
  -Method GET `
  -Headers $headers
```

### Test thêm sinh viên
```powershell
$sinhVien = @{
  maSV = "SV999"
  hoTen = "Test Student"
  ngaySinh = "2002-01-01"
  gioiTinh = "Nam"
  maLop = "CNTT2020A"
  maKhoa = "CNTT"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" `
  -Method POST `
  -Headers $headers `
  -Body $sinhVien
```

### Test export Excel
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/export/excel/all" `
  -Method GET `
  -Headers $headers `
  -OutFile "DanhSachSinhVien.xlsx"
```

---

## 📌 Notes

- Tất cả timestamps sử dụng định dạng ISO 8601
- Ngày sinh sử dụng định dạng: `yyyy-MM-dd`
- Token JWT có hiệu lực 24 giờ
- File export tự động đặt tên theo timestamp
- CORS được cấu hình cho `http://localhost:3000` và `http://localhost:5173`
