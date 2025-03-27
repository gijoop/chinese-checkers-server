package com.chinese_checkers.server;

import com.chinese_checkers.server.Connection.Server;
import com.chinese_checkers.server.Game.Board;
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
        "select_ruleset", this::selectRuleset,
        "start", this::startServer,
        "stop", this::stopServer,
        "exit", this::exit
    );

    private final Set<Integer> allowedPlayerCounts = Set.of(2, 3, 4, 6);

    private Ruleset getRuleset(Board board, CornerHelper cornerHelper, String rulesetName) {
        return switch (rulesetName) {
            case "standard" -> new StandardRuleset(board, cornerHelper);
            case "fast_paced" -> new FastPacedRuleset(board, cornerHelper);
            default -> throw new IllegalArgumentException("Invalid ruleset: " + rulesetName);
        };
    }


    public CLI() {}

    public void start() {
        System.out.println("Chinese Checkers Server CLI started");
        System.out.println("Usage: <command> <args>\n Available commands: \n" +
        "- set_port <1024 - 65535> \n " +
        "- set_player_count <2, 3, 4, 6> \n " +
        "- set_bot_count <0 - 5> (at least one human player must remain)\n " +
        "- show_saves\n " +
        "- load_and_start <game_id>\n " +
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

    private String selectRuleset(String[] args) {
        if (args.length != 2) {
            return  "Invalid number of arguments. Usage: select_ruleset <ruleset>.";
        }

        String rulesetNameValue = args[1];

        try {
            ruleset = getRuleset(board, cornerHelper, rulesetNameValue);
            rulesetName = rulesetNameValue;
            return "Ruleset set to " + rulesetName;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

    }

    private String startServer(String[] args) {
        if (server != null) {
            return "Server already started";
        }

        cornerHelper = new CornerHelper(playerCount, board.getSize());
        ruleset = getRuleset(board, cornerHelper, rulesetName);

        server = new Server(playerCount, botCount, port, ruleset, board, cornerHelper);
        server.start();

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

    private String exit(String[] args) {
        if (server != null) {
            server.stop();
        }

        running = false;
        return "Exiting...";
    }
}
