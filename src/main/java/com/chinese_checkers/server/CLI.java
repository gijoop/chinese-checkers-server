package com.chinese_checkers.server;

import com.chinese_checkers.server.Connection.Server;
import com.chinese_checkers.server.DBConnection.MoveRepository;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.SaveManager;
import com.chinese_checkers.server.Game.StandardBoard;
import com.chinese_checkers.server.Game.Ruleset.CornerHelper;
import com.chinese_checkers.server.Game.Ruleset.FastPacedRuleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.StandardRuleset;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * The CLI class provides a command-line interface for the Chinese Checkers Server.
 * It allows users to configure the server's port and player count, start and stop the server, and exit the CLI.
 * 
 * Commands:
 * - set_port [1024 - 65535]: Sets the server port to the specified value within the range.
 * - set_player_count [2 - 10]: Sets the number of players for the server within the specified range.
 * - start: Starts the server with the configured port and player count.
 * - stop: Stops the server if it is running.
 * - exit: Exits the CLI and stops the server if it is running.
 * 
 * Default values:
 * - Default port: 12345
 * - Default player count: 2
 * 
 * Usage:
 * [command] [args]
 * 
 * Example:
 *  set_port 8080
 *  set_player_count 4
 *  start
 *  stop
 *  exit
 */
public class CLI
{
    private boolean running = true;

    private int playerCount = 2;
    private int botCount = 0;
    private int port = 12345;
    private String rulesetName = "standard";
    private int saveId = -1;
    private final Board board = new StandardBoard(5);

    private CornerHelper cornerHelper = new CornerHelper(playerCount, board.getSize());
    private Ruleset ruleset = new StandardRuleset(board, cornerHelper);
    private Server server = null;

    // Map <command, function of <args, return message>>
    // args are: command + arguments
    private final Map<String, Function<String[], String>> commands = Map.of(
        "set_port", this::setPort,
        "set_player_count", this::setPlayerCount,
        "set_bot_count", this::setBotCount,
        "show_saves", this::showSaves,
        "load_game", this::loadGame,
        "select_ruleset", this::selectRuleset,
        "start", this::startServer,
        "stop", this::stopServer,
        "restart", this::restartServer,
        "exit", this::exit
    );

    private final Set<Integer> allowedPlayerCounts = Set.of(2, 3, 4, 6);

    private final Map<String, Ruleset> rulesets = Map.of(
        "standard", new StandardRuleset(board, cornerHelper),
        "fast_paced", new FastPacedRuleset(board, cornerHelper)
    );


    public CLI() {}

    public void start() {
        System.out.println("Chinese Checkers Server CLI started");
        System.out.println("Usage: <command> <args>\n Available commands: \n" +
        "- set_port <1024 - 65535> \n " +
        "- set_player_count <2, 3, 4, 6> \n " +
        "- set_bot_count <0 - 5> \n " +
        "- show_saves\n " +
        "- load_game <game_id>\n " +
        "- select_ruleset <standard/fast_paced> \n " +
        "- start\n " +
        "- exit\n");
        

        System.out.println("Default port: " + port + "\nDefault player count: " + playerCount + "\nDefault ruleset: standard");

        while (running) {
            System.out.print("> ");
            String input = System.console().readLine();
            String[] tokens = input.split(" ");

            if (tokens.length == 0) {
                continue;
            }

            String command = tokens[0];
            String[] args = tokens;

            if (!commands.containsKey(command)) {
                System.out.println("Invalid command. Type 'help' for a list of commands.");
                continue;
            }

            // Execute command
            String commandReturnMessage = commands.get(command).apply(args);

            if (commandReturnMessage != null) {
                System.out.println(commandReturnMessage);
            }
        }
    }


    private String setPort(String[] args)
    {
        if (args.length != 2) {
            return "Invalid number of arguments. Usage: set_port <port>";
        }

        int portValue;
        try {
            portValue = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return "Port must be an integer";
        }

        if (portValue < 1024 || portValue > 65535) {
            return "Port must be between 1024 and 65535";
        }

        port = portValue;

        return "Port set to " + portValue;
    }

    private String setPlayerCount(String[] args) {
        if (args.length != 2) {
            return "Invalid number of arguments. Usage: set_player_count <player_count>";
        }

        int playerCountValue;
        try {
            playerCountValue = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return "Player count must be an integer";
        }

        if (!allowedPlayerCounts.contains(playerCountValue)) {
            return "Player count must be 2, 3, 4 or 6";
        }

        playerCount = playerCountValue;
        return "Player count set to " + playerCount;
    }

    private String setBotCount(String[] args) {
        if (args.length != 2) {
            return "Invalid number of arguments. Usage: set_bot_count <bot_count>";
        }

        int botCountValue;
        try {
            botCountValue = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return "Bot count must be an integer";
        }

        if (botCountValue < 0 || botCountValue > 5) {
            return "Bot count must be between 0 and 5";
        }

        if (playerCount - botCountValue < 1) {
            return "There must be at least 1 human player. Current player count: " + playerCount;
        }

        botCount = botCountValue;
        return "Bot count set to " + botCount;
    }

    private String showSaves(String[] args) {
        if (args.length != 1) {
            return "Invalid number of arguments. Usage: show_saves";
        }

        SaveManager saveManager = new SaveManager();
        return saveManager.getSaves();
    }

    private String loadGame(String[] args) {
        if (args.length != 2) {
            return "Invalid number of arguments. Usage: load_game <game_id>";
        }

        int gameId;
        try {
            gameId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return "Game ID must be an integer";
        }

        if(gameId < 0) {
            return "Game ID must be a positive integer";
        }

        saveId = gameId;
        
        return "Game with ID " + gameId + " will be loaded upon start";
    }

    private String selectRuleset(String[] args) {
        if (args.length != 2) {
            return  "Invalid number of arguments. Usage: select_ruleset <ruleset>. \n" +
                    "   Available rulesets: " + String.join(", ", rulesets.keySet());
        }

        String rulesetNameValue = args[1];
        if (!rulesets.containsKey(rulesetNameValue)) {
            return "Invalid ruleset. Available rulesets: " + String.join(", ", rulesets.keySet());
        }

        rulesetName = rulesetNameValue;

        return "Ruleset set to " + rulesetName;
    }

    private String startServer(String[] args) {
        if (server != null) {
            return "Server already started";
        }

        cornerHelper = new CornerHelper(playerCount, board.getSize());
        ruleset = rulesets.get(rulesetName); // warning: depends on board and cornerHelper

        server = new Server(playerCount, botCount, port, ruleset, board, cornerHelper);
        server.start(saveId);

        return  "Server started on port " + port + " with: \n" +
                "   [player count]: " + playerCount + "\n" +
                "   [bot count]:    " + botCount + "\n" +
                "   [ruleset]:      " + ruleset.getName();
    }

    private String stopServer(String[] args) {
        if (server == null) {
            return "Server not started";
        }

        server.stop();
        server = null;

        return "Server stopped";
    }

    private String restartServer(String[] args)
    {
        if (server == null) {
            return "Server not started";
        }

        server.stop();

        cornerHelper = new CornerHelper(playerCount, board.getSize());
        ruleset = rulesets.get(rulesetName); // warning: depends on board and cornerHelper

        server = new Server(playerCount, botCount, port, ruleset, board, cornerHelper);
        server.start(-1);

        return  "Server restarted on port " + port + "with: \n" +
                "   [player count]: " + playerCount + "\n" +
                "   [bot count]:    " + botCount + "\n" +
                "   [ruleset]:      " + ruleset.getName();
    }

    private String exit(String[] args) {
        if (server != null) {
            server.stop();
        }

        running = false;
        return "Exiting...";
    }
}
