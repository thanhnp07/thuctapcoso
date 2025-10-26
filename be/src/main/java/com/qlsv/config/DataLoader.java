package com.qlsv.config;

import com.qlsv.entity.Khoa;
import com.qlsv.entity.Lop;
import com.qlsv.entity.TaiKhoan;
import com.qlsv.repository.KhoaRepository;
import com.qlsv.repository.LopRepository;
import com.qlsv.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    
    private final KhoaRepository khoaRepository;
    private final LopRepository lopRepository;
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
            
            khoaRepository.save(cntt);
            khoaRepository.save(ktdn);
            khoaRepository.save(ngoaiNgu);
            log.info("Đã tạo {} khoa", khoaRepository.count());
        }
        
        // Tạo Lớp
        if (lopRepository.count() == 0) {
            Khoa cntt = khoaRepository.findById("CNTT").orElseThrow();
            Khoa ktdn = khoaRepository.findById("KTDN").orElseThrow();
            
            Lop cntt2020A = Lop.builder()
                    .maLop("CNTT2020A")
                    .tenLop("Công nghệ thông tin 2020A")
                    .khoaHoc("2020-2024")
                    .siSo(0)
                    .khoa(cntt)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Lop cntt2020B = Lop.builder()
                    .maLop("CNTT2020B")
                    .tenLop("Công nghệ thông tin 2020B")
                    .khoaHoc("2020-2024")
                    .siSo(0)
                    .khoa(cntt)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Lop ktdn2020A = Lop.builder()
                    .maLop("KTDN2020A")
                    .tenLop("Kinh tế 2020A")
                    .khoaHoc("2020-2024")
                    .siSo(0)
                    .khoa(ktdn)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            lopRepository.save(cntt2020A);
            lopRepository.save(cntt2020B);
            lopRepository.save(ktdn2020A);
            log.info("Đã tạo {} lớp", lopRepository.count());
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
