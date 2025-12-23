@echo off
REM Plants vs Zombies Game Launcher Script for Windows
REM This script compiles and runs the game

echo 🌱 Plants vs Zombies - Java Game Launcher 🌱
echo ==============================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Java is not installed or not in PATH
    echo Please install Java JDK 8 or higher
    pause
    exit /b 1
)

REM Display Java version
echo Java version:
java -version
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

REM Compile Java files
echo 📦 Compiling Java files...
javac -d bin -sourcepath src src\*.java

if %errorlevel% neq 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo ✅ Compilation successful!
echo.

REM Copy resources to bin directory (preserve structure for getResource to work)
echo 📁 Copying resources...
if not exist "bin\images" mkdir bin\images
xcopy /E /I /Y src\images bin\images >nul 2>&1
echo ✅ Resources copied!
echo.

REM Ask user how to run
echo How would you like to run the game?
echo 1) Run with web server (localhost:8080)
echo 2) Run game directly (desktop window only)
set /p choice="Enter choice (1 or 2): "

if "%choice%"=="1" (
    echo.
    echo 🚀 Starting game with web server...
    echo The game will be available at: http://localhost:8080
    echo The game window will also open automatically.
    echo.
    java -cp bin;src GameServer
) else (
    echo.
    echo 🚀 Starting game...
    java -cp bin;src GameWindow
)

pause

