import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Simple HTTP Server to serve the Plants vs Zombies game on localhost
 * This provides a web interface and information about the game
 */
public class GameServer {
    private static HttpServer server;
    private static final int PORT = 8080;

    public static void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new GameHandler());
            server.createContext("/launch", new LaunchHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Game Server started on http://localhost:" + PORT);
            System.out.println("Open your browser and navigate to: http://localhost:" + PORT);
            System.out.println("Press Ctrl+C to stop the server");
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }

    static class GameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = getHTMLPage();
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, html.length());
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        }

        private String getHTMLPage() {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Plants vs Zombies - Java Game</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                    "            margin: 0;\n" +
                    "            padding: 20px;\n" +
                    "            color: white;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            max-width: 800px;\n" +
                    "            margin: 0 auto;\n" +
                    "            background: rgba(255, 255, 255, 0.1);\n" +
                    "            padding: 30px;\n" +
                    "            border-radius: 10px;\n" +
                    "            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);\n" +
                    "        }\n" +
                    "        h1 {\n" +
                    "            text-align: center;\n" +
                    "            font-size: 2.5em;\n" +
                    "            margin-bottom: 10px;\n" +
                    "        }\n" +
                    "        .subtitle {\n" +
                    "            text-align: center;\n" +
                    "            font-size: 1.2em;\n" +
                    "            margin-bottom: 30px;\n" +
                    "            opacity: 0.9;\n" +
                    "        }\n" +
                    "        .launch-btn {\n" +
                    "            display: block;\n" +
                    "            width: 200px;\n" +
                    "            margin: 30px auto;\n" +
                    "            padding: 15px 30px;\n" +
                    "            background: #4CAF50;\n" +
                    "            color: white;\n" +
                    "            text-decoration: none;\n" +
                    "            border-radius: 5px;\n" +
                    "            text-align: center;\n" +
                    "            font-size: 1.2em;\n" +
                    "            transition: background 0.3s;\n" +
                    "        }\n" +
                    "        .launch-btn:hover {\n" +
                    "            background: #45a049;\n" +
                    "        }\n" +
                    "        .info-section {\n" +
                    "            background: rgba(255, 255, 255, 0.05);\n" +
                    "            padding: 20px;\n" +
                    "            border-radius: 5px;\n" +
                    "            margin: 20px 0;\n" +
                    "        }\n" +
                    "        .info-section h2 {\n" +
                    "            margin-top: 0;\n" +
                    "            color: #FFD700;\n" +
                    "        }\n" +
                    "        .info-section ul {\n" +
                    "            line-height: 1.8;\n" +
                    "        }\n" +
                    "        .status {\n" +
                    "            text-align: center;\n" +
                    "            padding: 10px;\n" +
                    "            background: rgba(76, 175, 80, 0.3);\n" +
                    "            border-radius: 5px;\n" +
                    "            margin: 20px 0;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>🌱 Plants vs Zombies 🌱</h1>\n" +
                    "        <p class=\"subtitle\">Java Desktop Game</p>\n" +
                    "        \n" +
                    "        <div class=\"status\">\n" +
                    "            ✅ Server is running on localhost:" + PORT + "\n" +
                    "        </div>\n" +
                    "        \n" +
                    "        <div class=\"info-section\">\n" +
                    "            <h2>📋 How to Play</h2>\n" +
                    "            <ul>\n" +
                    "                <li>Click on plant cards at the top to select a plant</li>\n" +
                    "                <li>Click on the grid to place plants</li>\n" +
                    "                <li>Collect suns to earn points</li>\n" +
                    "                <li>Defend your garden from zombies!</li>\n" +
                    "            </ul>\n" +
                    "        </div>\n" +
                    "        \n" +
                    "        <div class=\"info-section\">\n" +
                    "            <h2>🎮 Plant Types</h2>\n" +
                    "            <ul>\n" +
                    "                <li><strong>Sunflower</strong> - Costs 50 sun, produces suns</li>\n" +
                    "                <li><strong>Peashooter</strong> - Costs 100 sun, shoots peas</li>\n" +
                    "                <li><strong>Freeze Peashooter</strong> - Costs 175 sun, shoots freezing peas</li>\n" +
                    "            </ul>\n" +
                    "        </div>\n" +
                    "        \n" +
                    "        <div class=\"info-section\">\n" +
                    "            <h2>💻 Running the Game</h2>\n" +
                    "            <p>The game window should open automatically. If not, check the terminal/console where you started the server.</p>\n" +
                    "            <p>You can also run the game directly using:</p>\n" +
                    "            <pre style=\"background: rgba(0,0,0,0.3); padding: 10px; border-radius: 5px; overflow-x: auto;\">java -cp bin:src GameWindow</pre>\n" +
                    "        </div>\n" +
                    "        \n" +
                    "        <a href=\"/launch\" class=\"launch-btn\">🚀 Launch Game</a>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
        }
    }

    static class LaunchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Launch the game in a new thread
            new Thread(() -> {
                try {
                    GameWindow.main(new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            String response = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Launching Game...</title>\n" +
                    "    <meta http-equiv=\"refresh\" content=\"2;url=/\">\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                    "            display: flex;\n" +
                    "            justify-content: center;\n" +
                    "            align-items: center;\n" +
                    "            height: 100vh;\n" +
                    "            margin: 0;\n" +
                    "            color: white;\n" +
                    "        }\n" +
                    "        .message {\n" +
                    "            text-align: center;\n" +
                    "            font-size: 1.5em;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"message\">\n" +
                    "        <h1>🚀 Launching Game...</h1>\n" +
                    "        <p>The game window should open shortly!</p>\n" +
                    "        <p>Redirecting back to home page...</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void main(String[] args) {
        // Start the server
        startServer();

        // Also launch the game window
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Wait a bit for server to start
                GameWindow.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Keep the server running
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopServer();
        }));
    }
}

