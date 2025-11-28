@echo off
echo Building and starting Spring Boot Application...
cd /d "c:\Finalyearproject\InternHubBackend"

echo Cleaning and compiling...
call mvnw.cmd clean compile

echo Starting application...
call mvnw.cmd spring-boot:run

pause