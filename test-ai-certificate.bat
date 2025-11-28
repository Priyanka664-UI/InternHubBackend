@echo off
echo Testing AI Certificate Generation Endpoint...

curl -X POST http://192.168.0.100:8083/api/admin/certificates/generate-ai ^
  -H "Content-Type: application/json" ^
  -d "{\"studentName\":\"John Doe\",\"studentEmail\":\"john@example.com\",\"college\":\"Test College\",\"internshipTitle\":\"Software Development\",\"companyName\":\"Test Company\",\"startDate\":\"2024-01-01\",\"endDate\":\"2024-03-01\",\"template\":\"professional\",\"customPrompt\":\"Excellent performance\"}"

pause