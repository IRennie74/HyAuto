@echo off
set INSTANCE_NAME=Worker1
set MMC_PATH=C:\Users\renni\Downloads\mmc-develop-win32\MultiMC

echo 🔄 Restarting %INSTANCE_NAME%...

:: Gracefully close Minecraft instance tied to this MultiMC window
taskkill /F /FI "WINDOWTITLE eq MultiMC: %INSTANCE_NAME%*" /IM javaw.exe >nul 2>&1
timeout /T 2 >nul

:: Also ensure MultiMC is fresh
taskkill /F /IM MultiMC.exe >nul 2>&1
timeout /T 1 >nul

:: Relaunch MultiMC instance
cd /D "%MMC_PATH%"
start "" MultiMC.exe --launch "%INSTANCE_NAME%"

echo ✅ Restart attempted for %INSTANCE_NAME%
pause
