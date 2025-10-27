package com.qlsv.config;

import com.qlsv.entity.Khoa;
import com.qlsv.entity.Lop;
import com.qlsv.entity.SinhVien;
import com.qlsv.entity.TaiKhoan;
import com.qlsv.repository.KhoaRepository;
import com.qlsv.repository.LopRepository;
import com.qlsv.repository.SinhVienRepository;
import com.qlsv.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    
    private final KhoaRepository khoaRepository;
    private final LopRepository lopRepository;
    private final SinhVienRepository sinhVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Bắt đầu load dữ liệu mẫu...");
        
        // Tạo Khoa
        if (khoaRepository.count() == 0) {
            Khoa cntt = Khoa.builder()
                    .maKhoa("CNTT")
                    .tenKhoa("Công nghệ thông tin")
                    .moTa("Khoa Công nghệ thông tin")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Khoa ktdn = Khoa.builder()
                    .maKhoa("KTDN")
                    .tenKhoa("Kinh tế - Quản trị kinh doanh")
                    .moTa("Khoa Kinh tế")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Khoa ngoaiNgu = Khoa.builder()
                    .maKhoa("NGOAINGU")
                    .tenKhoa("Ngoại ngữ")
                    .moTa("Khoa Ngoại ngữ")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Khoa dien = Khoa.builder()
                    .maKhoa("DIEN")
                    .tenKhoa("Điện - Điện tử")
                    .moTa("Khoa Điện - Điện tử")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Khoa co = Khoa.builder()
                    .maKhoa("CO")
                    .tenKhoa("Cơ khí - Chế tạo máy")
                    .moTa("Khoa Cơ khí")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Khoa hoa = Khoa.builder()
                    .maKhoa("HOA")
                    .tenKhoa("Hóa học - Môi trường")
                    .moTa("Khoa Hóa học và Môi trường")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            khoaRepository.save(cntt);
            khoaRepository.save(ktdn);
            khoaRepository.save(ngoaiNgu);
            khoaRepository.save(dien);
            khoaRepository.save(co);
            khoaRepository.save(hoa);
            log.info("Đã tạo {} khoa", khoaRepository.count());
        }
        
        // Tạo Lớp
        if (lopRepository.count() == 0) {
            Lop cntt2020A = Lop.builder()
                    .maLop("CNTT2020A")
                    .tenLop("Công nghệ thông tin 2020A")
                    .khoaHoc("2020-2024")
                    .siSo(0)
                    .maKhoa("CNTT")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Lop cntt2020B = Lop.builder()
                    .maLop("CNTT2020B")
                    .tenLop("Công nghệ thông tin 2020B")
                    .khoaHoc("2020-2024")
                    .siSo(0)
                    .maKhoa("CNTT")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Lop ktdn2020A = Lop.builder()
                    .maLop("KTDN2020A")
                    .tenLop("Kinh tế 2020A")
                    .khoaHoc("2020-2024")
                    .siSo(0)
                    .maKhoa("KTDN")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Lop dien2021A = Lop.builder()
                    .maLop("DIEN2021A")
                    .tenLop("Điện - Điện tử 2021A")
                    .khoaHoc("2021-2025")
                    .siSo(0)
                    .maKhoa("DIEN")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Lop co2021A = Lop.builder()
                    .maLop("CO2021A")
                    .tenLop("Cơ khí 2021A")
                    .khoaHoc("2021-2025")
                    .siSo(0)
                    .maKhoa("CO")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Lop hoa2021A = Lop.builder()
                    .maLop("HOA2021A")
                    .tenLop("Hóa học - Môi trường 2021A")
                    .khoaHoc("2021-2025")
                    .siSo(0)
                    .maKhoa("HOA")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            lopRepository.save(cntt2020A);
            lopRepository.save(cntt2020B);
            lopRepository.save(ktdn2020A);
            lopRepository.save(dien2021A);
            lopRepository.save(co2021A);
            lopRepository.save(hoa2021A);
            log.info("Đã tạo {} lớp", lopRepository.count());
        }
        
        // Tạo Sinh viên mẫu
        if (sinhVienRepository.count() == 0) {
            // Sinh viên CNTT
            SinhVien sv1 = SinhVien.builder()
                    .maSV("SV001")
                    .hoTen("Nguyễn Văn An")
                    .ngaySinh(LocalDate.of(2002, 5, 15))
                    .gioiTinh("Nam")
                    .email("nguyenvanan@gmail.com")
                    .soDienThoai("0901234567")
                    .diaChi("Hà Nội")
                    .maLop("CNTT2020A")
                    .maKhoa("CNTT")
                    .gpa(3.45)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            SinhVien sv2 = SinhVien.builder()
                    .maSV("SV002")
                    .hoTen("Trần Thị Bình")
                    .ngaySinh(LocalDate.of(2002, 8, 20))
                    .gioiTinh("Nữ")
                    .email("tranthibinh@gmail.com")
                    .soDienThoai("0912345678")
                    .diaChi("Hồ Chí Minh")
                    .maLop("CNTT2020A")
                    .maKhoa("CNTT")
                    .gpa(3.75)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            SinhVien sv3 = SinhVien.builder()
                    .maSV("SV003")
                    .hoTen("Lê Hoàng Cường")
                    .ngaySinh(LocalDate.of(2002, 3, 10))
                    .gioiTinh("Nam")
                    .email("lehoangcuong@gmail.com")
                    .soDienThoai("0923456789")
                    .diaChi("Đà Nẵng")
                    .maLop("CNTT2020B")
                    .maKhoa("CNTT")
                    .gpa(3.20)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            SinhVien sv4 = SinhVien.builder()
                    .maSV("SV004")
                    .hoTen("Phạm Thị Dung")
                    .ngaySinh(LocalDate.of(2002, 11, 25))
                    .gioiTinh("Nữ")
                    .email("phamthidung@gmail.com")
                    .soDienThoai("0934567890")
                    .diaChi("Hải Phòng")
                    .maLop("CNTT2020B")
                    .maKhoa("CNTT")
                    .gpa(3.60)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            // Sinh viên Kinh tế
            SinhVien sv5 = SinhVien.builder()
                    .maSV("SV005")
                    .hoTen("Hoàng Văn Em")
                    .ngaySinh(LocalDate.of(2002, 6, 5))
                    .gioiTinh("Nam")
                    .email("hoangvanem@gmail.com")
                    .soDienThoai("0945678901")
                    .diaChi("Cần Thơ")
                    .maLop("KTDN2020A")
                    .maKhoa("KTDN")
                    .gpa(3.30)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            SinhVien sv6 = SinhVien.builder()
                    .maSV("SV006")
                    .hoTen("Vũ Thị Giang")
                    .ngaySinh(LocalDate.of(2002, 9, 18))
                    .gioiTinh("Nữ")
                    .email("vuthigiang@gmail.com")
                    .soDienThoai("0956789012")
                    .diaChi("Huế")
                    .maLop("KTDN2020A")
                    .maKhoa("KTDN")
                    .gpa(3.55)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            // Sinh viên Điện - Điện tử
            SinhVien sv7 = SinhVien.builder()
                    .maSV("SV007")
                    .hoTen("Đặng Văn Hải")
                    .ngaySinh(LocalDate.of(2003, 4, 12))
                    .gioiTinh("Nam")
                    .email("dangvanhai@gmail.com")
                    .soDienThoai("0967890123")
                    .diaChi("Nha Trang")
                    .maLop("DIEN2021A")
                    .maKhoa("DIEN")
                    .gpa(3.25)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            SinhVien sv8 = SinhVien.builder()
                    .maSV("SV008")
                    .hoTen("Bùi Thị Lan")
                    .ngaySinh(LocalDate.of(2003, 7, 8))
                    .gioiTinh("Nữ")
                    .email("buithilan@gmail.com")
                    .soDienThoai("0978901234")
                    .diaChi("Vũng Tàu")
                    .maLop("DIEN2021A")
                    .maKhoa("DIEN")
                    .gpa(3.70)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            // Sinh viên Cơ khí
            SinhVien sv9 = SinhVien.builder()
                    .maSV("SV009")
                    .hoTen("Ngô Văn Minh")
                    .ngaySinh(LocalDate.of(2003, 1, 30))
                    .gioiTinh("Nam")
                    .email("ngovanminh@gmail.com")
                    .soDienThoai("0989012345")
                    .diaChi("Quảng Ninh")
                    .maLop("CO2021A")
                    .maKhoa("CO")
                    .gpa(3.15)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            SinhVien sv10 = SinhVien.builder()
                    .maSV("SV010")
                    .hoTen("Đinh Thị Nga")
                    .ngaySinh(LocalDate.of(2003, 10, 22))
                    .gioiTinh("Nữ")
                    .email("dinhthinga@gmail.com")
                    .soDienThoai("0990123456")
                    .diaChi("Nghệ An")
                    .maLop("CO2021A")
                    .maKhoa("CO")
                    .gpa(3.40)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            // Sinh viên Hóa học
            SinhVien sv11 = SinhVien.builder()
                    .maSV("SV011")
                    .hoTen("Phan Văn Phúc")
                    .ngaySinh(LocalDate.of(2003, 2, 14))
                    .gioiTinh("Nam")
                    .email("phanvanphuc@gmail.com")
                    .soDienThoai("0901357924")
                    .diaChi("Bình Dương")
                    .maLop("HOA2021A")
                    .maKhoa("HOA")
                    .gpa(3.50)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            SinhVien sv12 = SinhVien.builder()
                    .maSV("SV012")
                    .hoTen("Tạ Thị Quỳnh")
                    .ngaySinh(LocalDate.of(2003, 12, 5))
                    .gioiTinh("Nữ")
                    .email("tathiquynh@gmail.com")
                    .soDienThoai("0912468135")
                    .diaChi("Đồng Nai")
                    .maLop("HOA2021A")
                    .maKhoa("HOA")
                    .gpa(3.65)
                    .trangThai("DANG_HOC")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            sinhVienRepository.save(sv1);
            sinhVienRepository.save(sv2);
            sinhVienRepository.save(sv3);
            sinhVienRepository.save(sv4);
            sinhVienRepository.save(sv5);
            sinhVienRepository.save(sv6);
            sinhVienRepository.save(sv7);
            sinhVienRepository.save(sv8);
            sinhVienRepository.save(sv9);
            sinhVienRepository.save(sv10);
            sinhVienRepository.save(sv11);
            sinhVienRepository.save(sv12);
            log.info("Đã tạo {} sinh viên", sinhVienRepository.count());
        }
        
        // Tạo Tài khoản
        if (taiKhoanRepository.count() == 0) {
            TaiKhoan admin = TaiKhoan.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .vaiTro("ADMIN")
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            TaiKhoan user1 = TaiKhoan.builder()
                    .username("user1")
                    .passwordHash(passwordEncoder.encode("user123"))
                    .vaiTro("USER")
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            taiKhoanRepository.save(admin);
            taiKhoanRepository.save(user1);
            log.info("Đã tạo {} tài khoản", taiKhoanRepository.count());
        }
        
        log.info("Hoàn thành load dữ liệu mẫu!");
    }
}
