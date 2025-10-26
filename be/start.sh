#!/bin/bash
# Quick start script for development

echo "========================================="
echo "Student Management System - Quick Start"
echo "========================================="

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Error: Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Error: Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo ""
echo "Starting services with Docker Compose..."
docker-compose up -d --build

echo ""
echo "Waiting for services to be ready..."
sleep 10

echo ""
echo "========================================="
echo "Services are now running!"
echo "========================================="
echo ""
echo "Backend API: http://localhost:8080/api"
echo "PostgreSQL: localhost:5432"
echo "pgAdmin: http://localhost:5050"
echo "  - Email: admin@qlsv.com"
echo "  - Password: admin123"
echo ""
echo "Default accounts:"
echo "  Admin - username: admin, password: admin123"
echo "  User  - username: user1, password: user123"
echo ""
echo "View logs:"
echo "  docker-compose logs -f backend"
echo ""
echo "Stop services:"
echo "  docker-compose down"
echo ""
echo "========================================="
