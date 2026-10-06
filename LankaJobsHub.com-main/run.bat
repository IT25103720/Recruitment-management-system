@echo off
setlocal
echo Starting LankaJobsHub...

REM Look for mvn in PATH first, else fallback to IntelliJ Maven
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set MVN_CMD=mvn
) else if exist "C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.3\plugins\maven\lib\maven3\bin\mvn.cmd" (
    set "MVN_CMD=C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.3\plugins\maven\lib\maven3\bin\mvn.cmd"
) else (
    echo Error: Maven executable not found in PATH or standard IntelliJ directory.
    pause
    exit /b 1
)

echo Using Maven: %MVN_CMD%
"%MVN_CMD%" spring-boot:run
pause
