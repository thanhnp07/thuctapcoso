# Quick start script for Windows PowerShell

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Student Management System - Quick Start" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

# Check if Docker is installed
if (!(Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "Error: Docker is not installed. Please install Docker Desktop first." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Starting services with Docker Compose..." -ForegroundColor Green
docker-compose up -d --build

Write-Host ""
Write-Host "Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Services are now running!" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend API: " -NoNewline
Write-Host "http://localhost:8080/api" -ForegroundColor Yellow
Write-Host "PostgreSQL: " -NoNewline
Write-Host "localhost:5432" -ForegroundColor Yellow
Write-Host "pgAdmin: " -NoNewline
Write-Host "http://localhost:5050" -ForegroundColor Yellow
Write-Host "  - Email: admin@qlsv.com"
Write-Host "  - Password: admin123"
Write-Host ""
Write-Host "Default accounts:" -ForegroundColor Cyan
Write-Host "  Admin - username: admin, password: admin123"
Write-Host "  User  - username: user1, password: user123"
Write-Host ""
Write-Host "View logs:" -ForegroundColor Cyan
Write-Host "  docker-compose logs -f backend"
Write-Host ""
Write-Host "Stop services:" -ForegroundColor Cyan
Write-Host "  docker-compose down"
Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
