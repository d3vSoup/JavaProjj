# Plants vs Zombies - Java Game

## An Upgraded and Extended Version of Plants vs Zombies

This is an upgraded and extended simulation version of the classic Plants vs Zombies game, developed as a **Java Group Project**. The game features enhanced gameplay mechanics, multiple levels, improved graphics, and additional features beyond the original concept.

![Alt text](/../master/pvz.png?raw=true "Screenshot")

## 🚀 How to Run the Game

### Prerequisites
- **Java JDK 8 or higher** must be installed on your system
- Check if Java is installed by running: `java -version`

### Method 1: Using the Launcher Script (Recommended)

#### On macOS (Mac Terminal):
1. Open **Terminal** application (found in Applications > Utilities, or press `Cmd + Space` and type "Terminal")
2. Navigate to the project directory:
   ```bash
   cd /Users/rik_mac/Desktop/JAVA_Project/PlantsVsZombies
   ```
3. Run the launcher script:
   ```bash
   ./run.sh
   ```
4. Follow the prompts to choose how to run the game

#### On Linux:
```bash
cd PlantsVsZombies
./run.sh
```

#### On Windows:
```cmd
cd PlantsVsZombies
run.bat
```

The script will:
1. Check if Java is installed
2. Compile all Java files
3. Copy necessary resources
4. Ask you how to run the game:
   - **Option 1**: Run with web server (opens browser at `http://localhost:8080`)
   - **Option 2**: Run game directly (desktop window only)

### Method 2: Manual Compilation and Execution

#### For Mac Terminal:

1. Open **Terminal** application
2. Navigate to the project directory:
   ```bash
   cd /Users/rik_mac/Desktop/JAVA_Project/PlantsVsZombies
   ```

#### Step 1: Compile the Java files
```bash
# Create bin directory
mkdir -p bin

# Compile all Java files
javac -d bin -sourcepath src src/*.java

# Copy resources
cp -r src/images bin/
```

#### Step 2: Run the Game

**Option A: Run with Web Server (localhost)**
```bash
cd bin
java -cp .:../src GameServer
```
Then open your browser and navigate to: `http://localhost:8080`

**Option B: Run Game Directly (Desktop Window)**
```bash
cd bin
java -cp .:../src GameWindow
```

**Quick Mac Terminal Commands:**
```bash
# Full path to navigate to project
cd /Users/rik_mac/Desktop/JAVA_Project/PlantsVsZombies

# Compile and run in one go
mkdir -p bin && javac -d bin -sourcepath src src/*.java && cp -r src/images bin/ && cd bin && java -cp .:../src GameWindow
```

### Method 3: Using an IDE (IntelliJ IDEA, Eclipse, NetBeans)

1. Open the project in your IDE
2. Set the source directory to `src/`
3. Run `GameWindow.java` for desktop mode
4. Run `GameServer.java` for web server mode

## 🌐 Web Server Mode

When running with `GameServer`, you get:
- **Web Interface**: Visit `http://localhost:8080` in your browser
- **Game Information**: View game instructions and plant types
- **Launch Button**: Click to open the game window
- **Auto-launch**: The game window opens automatically when the server starts

The web server runs on **port 8080** by default. Make sure this port is not in use by another application.

## 🎮 Game Controls

- **Click** on plant cards at the top to select a plant
- **Click** on the grid to place plants
- **Click** on falling suns to collect them
- Plants automatically shoot at zombies

## 🌱 Plant Types

- **Sunflower** (50 sun) - Produces suns for you to collect
- **Peashooter** (100 sun) - Shoots peas at zombies
- **Freeze Peashooter** (175 sun) - Shoots freezing peas that slow zombies

## 📝 Notes

- The game saves your level progress in `LEVEL_CONTENT.vbhv`
- Make sure all image resources are in the `src/images/` directory
- The game window size is fixed at 1012x785 pixels

## 🛠️ Troubleshooting

**Problem**: "Java is not recognized"
- **Solution**: Install Java JDK and add it to your system PATH

**Problem**: "Port 8080 is already in use"
- **Solution**: Close the application using port 8080, or modify `GameServer.java` to use a different port

**Problem**: Images not loading
- **Solution**: Make sure the `src/images/` directory exists and contains all required image files

**Problem**: Compilation errors
- **Solution**: Ensure you're using Java JDK 8 or higher, and all source files are in the `src/` directory

**Problem**: Wrong level or wrong zombie types appearing
- **Solution**: Delete the level save file to reset to Level 1:
  ```bash
  cd /Users/rik_mac/Desktop/JAVA_Project/PlantsVsZombies
  rm LEVEL_CONTENT.vbhv
  ```
  This will reset the game to Level 1 when you restart.

## 📊 Console Output (Debug Information)

When you run the game from Terminal, you'll see console output including:
- Current level information
- Zombie spawning details
- Sun collection notifications
- Progress updates

**To view console output:**
- If running from Terminal: The output appears directly in the Terminal window
- If running from an IDE: Check the "Console" or "Output" tab in your IDE
- The console shows messages like:
  - `GamePanel initialized with level: X`
  - `Spawning: NormalZombie at lane X`
  - `Sun collected! Score: X -> Y`

## 📦 Project Structure

```
PlantsVsZombies/
├── src/              # Source code
│   ├── *.java        # Java source files
│   └── images/       # Game images and resources
├── bin/              # Compiled classes (created after compilation)
├── run.sh            # Linux/macOS launcher script
├── run.bat           # Windows launcher script
└── README.md         # This file
```

## 🎯 Features

- Multiple plant types (Sunflower, Peashooter, Freeze Peashooter)
- Multiple zombie types (Normal Zombie, Cone Head Zombie)
- Level progression system
- Sun collection mechanics
- Real-time game loop with timers

## 📚 Project Information

**Author**: Soup  
**Project Type**: Java Group Project  
**Description**: This is an upgraded and extended simulation version of Plants vs Zombies, featuring enhanced gameplay mechanics, multiple difficulty levels, and improved user experience.

Enjoy the game! 🌱🧟