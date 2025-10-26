# Quick Test Guide - Hướng dẫn test nhanh

## 🚀 Bước 1: Khởi động hệ thống

### Với Windows PowerShell (Khuyến nghị):
```powershell
cd d:\WorkSpace\Lab\thuctapcoso\be
.\start.ps1
```

### Hoặc thủ công:
```powershell
docker-compose up -d --build
```

**Chờ khoảng 30 giây để services khởi động hoàn tất.**

## 🧪 Bước 2: Test API với PowerShell

### 2.1. Đăng nhập lấy token
```powershell
$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"admin123"}'

$token = $loginResponse.data.token
Write-Host "Token: $token" -ForegroundColor Green

# Tạo headers cho các request tiếp theo
$headers = @{
  "Authorization" = "Bearer $token"
  "Content-Type" = "application/json"
}
```

### 2.2. Kiểm tra danh sách sinh viên (ban đầu rỗng)
```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" `
  -Method GET `
  -Headers $headers

Write-Host "Số sinh viên hiện tại: $($response.data.Count)" -ForegroundColor Cyan
```

### 2.3. Thêm sinh viên mẫu
```powershell
$sinhVien1 = @{
  maSV = "SV001"
  hoTen = "Nguyen Van An"
  ngaySinh = "2002-01-15"
  gioiTinh = "Nam"
  email = "nguyenvanan@gmail.com"
  soDienThoai = "0123456789"
  diaChi = "Ha Noi"
  gpa = 3.5
  trangThai = "DANG_HOC"
  maLop = "CNTT2020A"
  maKhoa = "CNTT"
} | ConvertTo-Json

$result1 = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" `
  -Method POST `
  -Headers $headers `
  -Body $sinhVien1

Write-Host "Đã thêm: $($result1.data.hoTen)" -ForegroundColor Green
```

```powershell
$sinhVien2 = @{
  maSV = "SV002"
  hoTen = "Tran Thi Binh"
  ngaySinh = "2002-03-20"
  gioiTinh = "Nữ"
  email = "tranthibinh@gmail.com"
  soDienThoai = "0987654321"
  diaChi = "Ho Chi Minh"
  gpa = 3.8
  trangThai = "DANG_HOC"
  maLop = "CNTT2020A"
  maKhoa = "CNTT"
} | ConvertTo-Json

$result2 = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" `
  -Method POST `
  -Headers $headers `
  -Body $sinhVien2

Write-Host "Đã thêm: $($result2.data.hoTen)" -ForegroundColor Green
```

```powershell
$sinhVien3 = @{
  maSV = "SV003"
  hoTen = "Le Van Cuong"
  ngaySinh = "2002-05-10"
  gioiTinh = "Nam"
  email = "levancuong@gmail.com"
  soDienThoai = "0912345678"
  diaChi = "Da Nang"
  gpa = 3.2
  trangThai = "DANG_HOC"
  maLop = "CNTT2020B"
  maKhoa = "CNTT"
} | ConvertTo-Json

$result3 = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" `
  -Method POST `
  -Headers $headers `
  -Body $sinhVien3

Write-Host "Đã thêm: $($result3.data.hoTen)" -ForegroundColor Green
```

### 2.4. Test tìm kiếm
```powershell
# Tìm theo tên
$searchResult = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/search?hoTen=Nguyen&page=0&size=10" `
  -Method GET `
  -Headers $headers

Write-Host "Tìm thấy $($searchResult.data.totalElements) sinh viên có tên 'Nguyen'" -ForegroundColor Cyan
$searchResult.data.content | Format-Table maSV, hoTen, gpa
```

```powershell
# Tìm theo khoa
$khoaResult = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/khoa/CNTT" `
  -Method GET `
  -Headers $headers

Write-Host "Khoa CNTT có $($khoaResult.data.Count) sinh viên" -ForegroundColor Cyan
```

### 2.5. Test cập nhật sinh viên
```powershell
$updateData = @{
  hoTen = "Nguyen Van An - Updated"
  gpa = 3.7
} | ConvertTo-Json

$updateResult = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/SV001" `
  -Method PUT `
  -Headers $headers `
  -Body $updateData

Write-Host "Cập nhật thành công: GPA mới = $($updateResult.data.gpa)" -ForegroundColor Green
```

### 2.6. Test xuất báo cáo Excel
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/export/excel/all" `
  -Method GET `
  -Headers $headers `
  -OutFile "DanhSachSinhVien.xlsx"

Write-Host "Đã xuất file Excel: DanhSachSinhVien.xlsx" -ForegroundColor Green
```

### 2.7. Test xuất báo cáo PDF
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/export/pdf/all" `
  -Method GET `
  -Headers $headers `
  -OutFile "DanhSachSinhVien.pdf"

Write-Host "Đã xuất file PDF: DanhSachSinhVien.pdf" -ForegroundColor Green
```

### 2.8. Test xóa sinh viên
```powershell
$deleteResult = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/SV003" `
  -Method DELETE `
  -Headers $headers

Write-Host $deleteResult.message -ForegroundColor Yellow
```

## 🔍 Kiểm tra database với pgAdmin

1. Mở trình duyệt: http://localhost:5050
2. Đăng nhập:
   - Email: `admin@qlsv.com`
   - Password: `admin123`
3. Add Server:
   - Name: `QLSV PostgreSQL`
   - Host: `postgres`
   - Port: `5432`
   - Database: `qlsv_db`
   - Username: `postgres`
   - Password: `postgres123`

## 📊 Xem logs

### Logs của backend
```powershell
docker-compose logs -f backend
```

### Logs của database
```powershell
docker-compose logs -f postgres
```

### Logs tất cả services
```powershell
docker-compose logs -f
```

## 🛑 Dừng và cleanup

### Dừng services
```powershell
docker-compose down
```

### Dừng và xóa hết dữ liệu (reset)
```powershell
docker-compose down -v
```

### Xem trạng thái containers
```powershell
docker-compose ps
```

## ⚡ Script test tự động (All-in-one)

Lưu script dưới đây vào file `test-complete.ps1`:

```powershell
# test-complete.ps1 - Test tự động toàn bộ hệ thống

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "  AUTO TEST - Student Management System" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

# 1. Login
Write-Host "`n[1/7] Đăng nhập..." -ForegroundColor Yellow
$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"admin123"}'

$token = $loginResponse.data.token
$headers = @{
  "Authorization" = "Bearer $token"
  "Content-Type" = "application/json"
}
Write-Host "✓ Đăng nhập thành công" -ForegroundColor Green

# 2. Thêm sinh viên
Write-Host "`n[2/7] Thêm 3 sinh viên..." -ForegroundColor Yellow
$students = @(
  @{maSV="SV001"; hoTen="Nguyen Van An"; ngaySinh="2002-01-15"; gioiTinh="Nam"; email="nguyenvanan@gmail.com"; soDienThoai="0123456789"; diaChi="Ha Noi"; gpa=3.5; trangThai="DANG_HOC"; maLop="CNTT2020A"; maKhoa="CNTT"},
  @{maSV="SV002"; hoTen="Tran Thi Binh"; ngaySinh="2002-03-20"; gioiTinh="Nữ"; email="tranthibinh@gmail.com"; soDienThoai="0987654321"; diaChi="Ho Chi Minh"; gpa=3.8; trangThai="DANG_HOC"; maLop="CNTT2020A"; maKhoa="CNTT"},
  @{maSV="SV003"; hoTen="Le Van Cuong"; ngaySinh="2002-05-10"; gioiTinh="Nam"; email="levancuong@gmail.com"; soDienThoai="0912345678"; diaChi="Da Nang"; gpa=3.2; trangThai="DANG_HOC"; maLop="CNTT2020B"; maKhoa="CNTT"}
)

foreach ($student in $students) {
  try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" `
      -Method POST `
      -Headers $headers `
      -Body ($student | ConvertTo-Json) | Out-Null
    Write-Host "  ✓ Đã thêm: $($student.hoTen)" -ForegroundColor Green
  } catch {
    Write-Host "  ✗ Lỗi khi thêm: $($student.hoTen)" -ForegroundColor Red
  }
}

# 3. Lấy danh sách
Write-Host "`n[3/7] Lấy danh sách sinh viên..." -ForegroundColor Yellow
$allStudents = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien" -Method GET -Headers $headers
Write-Host "✓ Có $($allStudents.data.Count) sinh viên" -ForegroundColor Green

# 4. Tìm kiếm
Write-Host "`n[4/7] Test tìm kiếm..." -ForegroundColor Yellow
$searchResult = Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/search?hoTen=Nguyen" -Method GET -Headers $headers
Write-Host "✓ Tìm thấy $($searchResult.data.totalElements) sinh viên" -ForegroundColor Green

# 5. Cập nhật
Write-Host "`n[5/7] Test cập nhật..." -ForegroundColor Yellow
$updateData = @{gpa=3.9} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/SV001" -Method PUT -Headers $headers -Body $updateData | Out-Null
Write-Host "✓ Cập nhật thành công" -ForegroundColor Green

# 6. Xuất báo cáo
Write-Host "`n[6/7] Test xuất báo cáo..." -ForegroundColor Yellow
Invoke-WebRequest -Uri "http://localhost:8080/api/export/excel/all" -Method GET -Headers $headers -OutFile "test-export.xlsx"
Write-Host "✓ Xuất Excel thành công: test-export.xlsx" -ForegroundColor Green

# 7. Xóa
Write-Host "`n[7/7] Test xóa..." -ForegroundColor Yellow
Invoke-RestMethod -Uri "http://localhost:8080/api/sinhvien/SV003" -Method DELETE -Headers $headers | Out-Null
Write-Host "✓ Xóa thành công" -ForegroundColor Green

Write-Host "`n=========================================" -ForegroundColor Cyan
Write-Host "  TEST HOÀN TẤT - TẤT CẢ ĐỀU PASS ✓" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Cyan
```

Chạy script:
```powershell
.\test-complete.ps1
```

## 🎯 Expected Results

- ✅ Đăng nhập thành công và nhận token JWT
- ✅ Thêm được 3 sinh viên
- ✅ Tìm kiếm trả về kết quả đúng
- ✅ Cập nhật GPA thành công
- ✅ Export Excel và PDF không lỗi
- ✅ Xóa sinh viên thành công

## ❌ Troubleshooting

### Lỗi kết nối
```powershell
# Kiểm tra containers đang chạy
docker-compose ps

# Restart services
docker-compose restart backend
```

### Lỗi authentication
```powershell
# Kiểm tra token còn hạn (24h)
# Đăng nhập lại để lấy token mới
```

### Lỗi validation
- Kiểm tra format dữ liệu (email, số điện thoại, ngày sinh, GPA)
- Xem API_DOCUMENTATION.md để biết format chính xác

---

Happy Testing! 🚀
