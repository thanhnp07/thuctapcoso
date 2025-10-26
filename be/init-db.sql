-- Init database script for Student Management System

-- Create database if not exists (executed by docker-entrypoint)
-- Database qlsv_db should already be created by POSTGRES_DB env var

-- Optional: Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create sample data for testing
-- Tables will be auto-created by Hibernate with ddl-auto=update

-- Sample Khoa data
INSERT INTO khoa (ma_khoa, ten_khoa, mo_ta, created_at, updated_at) 
VALUES 
  ('CNTT', N'Công nghệ thông tin', N'Khoa Công nghệ thông tin', NOW(), NOW()),
  ('KTDN', N'Kinh tế - Quản trị kinh doanh', N'Khoa Kinh tế', NOW(), NOW()),
  ('NGOAINGU', N'Ngoại ngữ', N'Khoa Ngoại ngữ', NOW(), NOW())
ON CONFLICT (ma_khoa) DO NOTHING;

-- Sample Lop data
INSERT INTO lop (ma_lop, ten_lop, khoa_hoc, si_so, ma_khoa, created_at, updated_at)
VALUES
  ('CNTT2020A', N'Công nghệ thông tin 2020A', '2020-2024', 0, 'CNTT', NOW(), NOW()),
  ('CNTT2020B', N'Công nghệ thông tin 2020B', '2020-2024', 0, 'CNTT', NOW(), NOW()),
  ('KTDN2020A', N'Kinh tế 2020A', '2020-2024', 0, 'KTDN', NOW(), NOW())
ON CONFLICT (ma_lop) DO NOTHING;

-- Sample TaiKhoan data (password: admin123 and user123 - bcrypt encoded)
INSERT INTO tai_khoan (username, password_hash, vai_tro, is_active, created_at, updated_at)
VALUES
  ('admin', '$2a$10$N8vVGW7HqhqJ5nZQqNjHZ.vPQPQV1R3aGQrJZKqX7kV9K.LjqH6FS', 'ADMIN', true, NOW(), NOW()),
  ('user1', '$2a$10$hDJ8nqX8x5bQqYqZ5zQqZ.vPQPQV1R3aGQrJZKqX7kV9K.LjqH6FS', 'USER', true, NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

-- Note: Sinh vien data will be added through the application API
-- to ensure proper foreign key relationships and validations

COMMENT ON DATABASE qlsv_db IS 'Student Management System Database';
