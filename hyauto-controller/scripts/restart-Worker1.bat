@echo off
:: Replace with your exact MultiMC instance name (case-sensitive!)
set INSTANCE_NAME="Worker1"

echo Attempting to restart %INSTANCE_NAME%...

:: 1. Try to close gracefully first
taskkill /F /FI "WINDOWTITLE eq Minecraft*%INSTANCE_NAME%*" /IM javaw.exe >nul 2>&1
timeout /T 2 >nul

:: 2. Force kill if still running
taskkill /F /IM javaw.exe >nul 2>&1
taskkill /F /IM MultiMC.exe >nul 2>&1
timeout /T 1 >nul

:: 3. ALWAYS attempt to relaunch (even if kill failed)
cd /D "C:\Users\renni\Downloads\mmc-develop-win32\MultiMC\"
start "" MultiMC.exe -l %INSTANCE_NAME%

:: 4. Simple success message (will show even if Java was already closed)
echo Restart attempted for %INSTANCE_NAME%
echo Check MultiMC for the instance window
pause