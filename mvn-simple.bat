@echo off
setlocal

set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.11\apache-maven-3.9.11

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Maven not found. Please install Maven manually or use your IDE's Maven integration.
    echo You can download Maven from: https://maven.apache.org/download.cgi
    exit /b 1
)

"%MAVEN_HOME%\bin\mvn.cmd" %*