#!/bin/bash

# Plants vs Zombies Game Launcher Script
# This script compiles and runs the game

echo "🌱 Plants vs Zombies - Java Game Launcher 🌱"
echo "=============================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    echo "Please install Java JDK 8 or higher"
    exit 1
fi

# Display Java version
echo "Java version:"
java -version
echo ""

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir -p bin
fi

# Compile Java files
echo "📦 Compiling Java files..."
javac -d bin -sourcepath src src/*.java

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "✅ Compilation successful!"
echo ""

# Copy resources to bin directory (preserve structure for getResource to work)
echo "📁 Copying resources..."
mkdir -p bin/images
cp -r src/images/* bin/images/ 2>/dev/null || true
echo "✅ Resources copied!"
echo ""

# Ask user how to run
echo "How would you like to run the game?"
echo "1) Run with web server (localhost:8080)"
echo "2) Run game directly (desktop window only)"
read -p "Enter choice (1 or 2): " choice

if [ "$choice" == "1" ]; then
    echo ""
    echo "🚀 Starting game with web server..."
    echo "The game will be available at: http://localhost:8080"
    echo "The game window will also open automatically."
    echo ""
    java -cp bin:src GameServer
else
    echo ""
    echo "🚀 Starting game..."
    java -cp bin:src GameWindow
fi

