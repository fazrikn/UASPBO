@echo off
REM Pastikan file sqlite-jdbc-3.42.0.0.jar sudah ada di folder lib

set SQLITE_JDBC=lib\sqlite-jdbc-3.42.0.0.jar

if not exist "%SQLITE_JDBC%" (
    echo File %SQLITE_JDBC% tidak ditemukan.
    echo Silakan unduh dari https://github.com/xerial/sqlite-jdbc/releases dan letakkan di folder lib
    pause
    exit /b 1
)

echo Mengompilasi file Java...
javac -cp "%SQLITE_JDBC%;src" src\*.java
if errorlevel 1 (
    echo Kompilasi gagal.
    pause
    exit /b 1
)

echo Menjalankan aplikasi...
java -cp "%SQLITE_JDBC%;src" MainApp

pause
