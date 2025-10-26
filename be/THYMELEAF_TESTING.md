# Hướng dẫn Test Thymeleaf Integration

## 1. Build và chạy ứng dụng

### Sử dụng Docker (Khuyến nghị)
```powershell
cd be
docker-compose down
docker-compose up -d --build
```

### Hoặc chạy local
```powershell
cd be
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

## 2. Kiểm tra services

Đợi ~30 giây để các service khởi động, sau đó kiểm tra:

```powershell
# Kiểm tra backend
docker-compose ps

# Xem logs
docker-compose logs -f backend
```

Expected output:
- `backend` - Up
- `postgres` - Up (healthy)
- `pgadmin` - Up (optional)

## 3. Truy cập ứng dụng web

### 3.1. Trang đăng nhập
1. Mở trình duyệt: http://localhost:8080
2. Sẽ tự động redirect đến http://localhost:8080/login
3. Giao diện đẹp với gradient background và form login

### 3.2. Đăng nhập
Sử dụng tài khoản mặc định:
- **Admin**: `admin` / `admin123`
- **User**: `user1` / `user123`

Click **"Đăng nhập"**

### 3.3. Dashboard
Sau khi đăng nhập thành công, sẽ redirect đến http://localhost:8080/dashboard

Kiểm tra:
- ✅ Sidebar bên trái với menu (Dashboard, Sinh viên, Khoa, Lớp)
- ✅ Top navbar với tên user và nút đăng xuất
- ✅ 4 stat cards: Tổng sinh viên, Tổng khoa, Tổng lớp, GPA trung bình
- ✅ 2 charts: Sinh viên theo khoa, Sinh viên theo giới tính
- ✅ Bảng danh sách sinh viên gần đây

### 3.4. Quản lý sinh viên

#### Danh sách sinh viên
1. Click menu **"Sinh viên"** trên sidebar
2. URL: http://localhost:8080/sinhvien

Kiểm tra:
- ✅ Search box với các filter (Từ khóa, Khoa, Lớp, Giới tính)
- ✅ Nút **"Thêm sinh viên"** (chỉ Admin)
- ✅ Bảng danh sách với pagination
- ✅ Các nút thao tác: Xem, Sửa, Xóa (Sửa/Xóa chỉ Admin)

#### Tìm kiếm sinh viên
1. Nhập từ khóa vào ô **"Từ khóa"**: vd. `SV001` hoặc `Nguyễn`
2. Chọn **Khoa**: vd. `Công nghệ thông tin`
3. Chọn **Lớp**: vd. `CNTT1`
4. Chọn **Giới tính**: `Nam` / `Nữ` / `Khác`
5. Click **"Tìm kiếm"**

Kiểm tra:
- ✅ Kết quả lọc đúng
- ✅ Pagination hoạt động
- ✅ Nút **"Làm mới"** reset filter

#### Thêm sinh viên mới (Admin only)
1. Click **"Thêm sinh viên"**
2. URL: http://localhost:8080/sinhvien/add
3. Điền form:
   - Mã SV: `SV100`
   - Họ tên: `Nguyễn Văn Test`
   - Ngày sinh: `01/01/2000`
   - Giới tính: `Nam`
   - Email: `test@example.com`
   - SĐT: `0901234567`
   - Khoa: Chọn khoa → Danh sách lớp tự động load
   - Lớp: Chọn lớp (đã filter theo khoa)
   - Địa chỉ: `Hà Nội`
   - GPA: `3.5`
   - Trạng thái: `Đang học`
4. Click **"Thêm mới"**

Kiểm tra:
- ✅ Validation form (required fields)
- ✅ Email format validation
- ✅ SĐT 10-11 chữ số
- ✅ GPA 0.00-4.00
- ✅ Danh sách lớp thay đổi khi chọn khoa
- ✅ Thành công → redirect về danh sách
- ✅ Alert thông báo thành công

#### Xem chi tiết sinh viên
1. Click icon **"👁 Xem"** trên bảng danh sách
2. URL: http://localhost:8080/sinhvien/view/SV100

Kiểm tra:
- ✅ Profile header đẹp với gradient
- ✅ Avatar icon lớn
- ✅ 2 cột thông tin: Cá nhân & Học tập
- ✅ Hiển thị đầy đủ thông tin
- ✅ Badge màu cho trạng thái
- ✅ Các nút: Quay lại, Sửa, Xóa (Sửa/Xóa chỉ Admin)

#### Sửa sinh viên (Admin only)
1. Click icon **"✏ Sửa"** trên bảng hoặc trang chi tiết
2. URL: http://localhost:8080/sinhvien/edit/SV100
3. Form tương tự thêm mới, nhưng:
   - Mã SV readonly (không sửa được)
   - Các field khác đã điền sẵn dữ liệu
4. Sửa thông tin, click **"Cập nhật"**

Kiểm tra:
- ✅ Form load đúng dữ liệu cũ
- ✅ Validation như thêm mới
- ✅ Cập nhật thành công → redirect về danh sách
- ✅ Alert thông báo cập nhật thành công

#### Xóa sinh viên (Admin only)
1. Click icon **"🗑 Xóa"** trên bảng hoặc trang chi tiết
2. Confirm dialog xuất hiện: "Bạn có chắc chắn muốn xóa sinh viên SV100?"
3. Click **OK**

Kiểm tra:
- ✅ Confirm dialog hiện ra
- ✅ Xóa thành công → reload trang
- ✅ Alert thông báo xóa thành công
- ✅ Sinh viên không còn trong danh sách

### 3.5. Xuất báo cáo

#### Xuất Excel
1. Trên dashboard hoặc sidebar, click **"Xuất Excel"**
2. Hoặc dùng API trực tiếp: http://localhost:8080/api/export/excel/all

Kiểm tra:
- ✅ File Excel tải về: `DanhSachSinhVien_YYYYMMDD_HHMMSS.xlsx`
- ✅ Mở file Excel, kiểm tra dữ liệu đầy đủ
- ✅ Format đẹp, có header, borders

#### Xuất PDF
1. Trên dashboard hoặc sidebar, click **"Xuất PDF"**
2. Hoặc dùng API trực tiếp: http://localhost:8080/api/export/pdf/all

Kiểm tra:
- ✅ File PDF tải về: `DanhSachSinhVien_YYYYMMDD_HHMMSS.pdf`
- ✅ Mở file PDF, kiểm tra dữ liệu đầy đủ
- ✅ Format đẹp, có table, Vietnamese font hỗ trợ

### 3.6. Đăng xuất
1. Click nút **"Đăng xuất"** ở top navbar
2. Redirect về http://localhost:8080/login?logout=true
3. Alert thông báo: "Đăng xuất thành công!"

Kiểm tra:
- ✅ Redirect về login page
- ✅ Session bị clear
- ✅ Không thể truy cập lại /dashboard hoặc /sinhvien khi chưa login

## 4. Test phân quyền (Role-based access)

### 4.1. Test với User role (user1/user123)
1. Đăng nhập với `user1` / `user123`
2. Truy cập dashboard: ✅ OK
3. Truy cập danh sách sinh viên: ✅ OK (chỉ xem)
4. Kiểm tra các nút:
   - ❌ Nút **"Thêm sinh viên"** không hiển thị
   - ❌ Nút **"Sửa"** không hiển thị
   - ❌ Nút **"Xóa"** không hiển thị
   - ✅ Nút **"Xem"** vẫn hiển thị
5. Thử truy cập trực tiếp:
   - http://localhost:8080/sinhvien/add → Redirect hoặc 403 Forbidden
   - http://localhost:8080/sinhvien/edit/SV001 → Redirect hoặc 403 Forbidden

### 4.2. Test với Admin role (admin/admin123)
1. Đăng nhập với `admin` / `admin123`
2. Truy cập dashboard: ✅ OK
3. Truy cập danh sách sinh viên: ✅ OK
4. Kiểm tra các nút:
   - ✅ Nút **"Thêm sinh viên"** hiển thị
   - ✅ Nút **"Sửa"** hiển thị
   - ✅ Nút **"Xóa"** hiển thị
   - ✅ Nút **"Xem"** hiển thị
5. Thử thêm/sửa/xóa: ✅ Tất cả hoạt động bình thường

## 5. Test REST API (vẫn hoạt động với JWT)

### 5.1. Login để lấy JWT token
```powershell
$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body '{"username":"admin","password":"admin123"}'

$token = $loginResponse.data.token
Write-Host "JWT Token: $token"
```

### 5.2. Gọi API với token
```powershell
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# Lấy danh sách sinh viên
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" `
    -Method GET `
    -Headers $headers

Write-Host "Danh sách sinh viên:" 
$response.data | Format-Table
```

Kiểm tra:
- ✅ API login trả về token
- ✅ API với token hoạt động bình thường
- ✅ API không có token → 401 Unauthorized

## 6. Troubleshooting

### Lỗi thường gặp

**1. Không thể truy cập http://localhost:8080**
```powershell
# Kiểm tra containers
docker-compose ps

# Nếu backend không up, xem logs
docker-compose logs backend

# Restart
docker-compose down
docker-compose up -d --build
```

**2. Login không thành công**
- Kiểm tra database đã có dữ liệu chưa:
```powershell
docker-compose exec postgres psql -U postgres -d qlsv_db -c "SELECT * FROM tai_khoan;"
```
- Nếu chưa có, chạy lại init-db.sql:
```powershell
docker-compose down -v
docker-compose up -d --build
```

**3. Charts không hiển thị**
- Mở Developer Tools (F12) → Console
- Kiểm tra lỗi JavaScript
- Đảm bảo API `/api/sinhvien` hoạt động:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" -Headers @{"Authorization"="Bearer <token>"}
```

**4. Form submit lỗi validation**
- Kiểm tra console logs
- Đảm bảo API endpoint `/api/sinhvien` hoạt động
- Kiểm tra format dữ liệu (email, phone, GPA, date)

**5. Thymeleaf template not found**
- Kiểm tra file template có trong `src/main/resources/templates/`
- Rebuild project: `./mvnw clean package`
- Restart application

**6. 403 Forbidden khi truy cập trang**
- Kiểm tra SecurityConfig.java
- Đảm bảo URL pattern đúng
- Clear cookies và login lại

## 7. Kiểm tra hoàn chỉnh

### Checklist toàn bộ tính năng

#### Authentication & Authorization
- [ ] Login với admin/admin123 thành công
- [ ] Login với user1/user123 thành công
- [ ] Login sai mật khẩu hiện lỗi
- [ ] Logout thành công, session cleared
- [ ] Admin có quyền CRUD
- [ ] User chỉ có quyền xem

#### Dashboard
- [ ] Hiển thị số liệu thống kê
- [ ] Charts load dữ liệu từ API
- [ ] Bảng sinh viên gần đây hiển thị
- [ ] Sidebar navigation hoạt động
- [ ] Xuất Excel/PDF từ dashboard

#### Quản lý sinh viên
- [ ] Danh sách sinh viên hiển thị
- [ ] Pagination hoạt động
- [ ] Tìm kiếm theo keyword
- [ ] Filter theo khoa, lớp, giới tính
- [ ] Thêm sinh viên mới (Admin)
- [ ] Sửa sinh viên (Admin)
- [ ] Xóa sinh viên (Admin)
- [ ] Xem chi tiết sinh viên
- [ ] Validation form đầy đủ
- [ ] Danh sách lớp thay đổi theo khoa

#### REST API
- [ ] Login API trả về JWT token
- [ ] API với token hoạt động
- [ ] API không token → 401
- [ ] CRUD API sinh viên
- [ ] Export Excel API
- [ ] Export PDF API

#### UI/UX
- [ ] Giao diện responsive (mobile/tablet/desktop)
- [ ] Màu sắc, font chữ đẹp
- [ ] Icons hiển thị đúng
- [ ] Alert/notification rõ ràng
- [ ] Loading states (nếu có)

## Kết luận

Nếu tất cả các test case trên đều pass ✅, thì Thymeleaf integration đã hoàn thành thành công!

**Hệ thống bây giờ hỗ trợ:**
1. ✅ **Web UI** với Thymeleaf (form-based authentication)
2. ✅ **REST API** với JWT (stateless authentication)
3. ✅ **Dual authentication** trong cùng một ứng dụng
4. ✅ **Role-based access control** (ADMIN vs USER)
5. ✅ **Full CRUD** operations
6. ✅ **Export** Excel/PDF
7. ✅ **Responsive UI** với Bootstrap 5
