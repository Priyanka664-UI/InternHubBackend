@echo off
echo Testing POST endpoint...
curl -X POST http://localhost:8082/api/admin/students ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Test Student\",\"email\":\"test@example.com\",\"password\":\"password123\",\"college\":\"Test College\",\"course\":\"Computer Science\"}"
pause