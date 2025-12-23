# Quick Start Guide

## 🚀 Fastest Way to Run

### Step 1: Open Terminal/Command Prompt
Navigate to the `PlantsVsZombies` folder:
```bash
cd /Users/rik_mac/Desktop/JAVA_Project/PlantsVsZombies
```

### Step 2: Run the Launcher

**On macOS/Linux:**
```bash
./run.sh
```

**On Windows:**
```cmd
run.bat
```

### Step 3: Choose Your Option
- **Option 1**: Run with web server → Opens browser at `http://localhost:8080` + game window
- **Option 2**: Run game directly → Just opens the game window

## 🌐 Accessing the Web Interface

If you chose Option 1, the game will be available at:
- **URL**: `http://localhost:8080`
- The game window will also open automatically
- You can click "Launch Game" on the web page to open another game window

## 🎮 Playing the Game

1. **Start with 150 sun points**
2. **Click** a plant card at the top (Sunflower, Peashooter, or Freeze Peashooter)
3. **Click** on a grid square to place the plant
4. **Click** on falling suns to collect them
5. **Defend** your garden from zombies!

## ⚠️ Troubleshooting

**"Command not found" or "Permission denied"**
- Make the script executable: `chmod +x run.sh` (macOS/Linux)

**"Java is not recognized"**
- Install Java JDK from: https://www.oracle.com/java/technologies/downloads/

**Port 8080 already in use**
- Close other applications using port 8080
- Or modify `GameServer.java` to use a different port

**Images not showing**
- Make sure you ran the launcher script (it copies images automatically)
- Check that `bin/images/` directory exists

That's it! Enjoy the game! 🎮

