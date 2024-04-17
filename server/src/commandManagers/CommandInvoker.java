package commandManagers;

import commandManagers.commands.*;
import enums.ReadModes;
import input.InputManager;
import commandManagers.commands.Command;
import network.Response;
import network.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandInvoker {
    private Map<String, Command> commands;
    private static CommandInvoker instance;

    private int scriptCounter;
    public final int SCRIPT_RECURSION_LIMIT = 10;

    private CommandInvoker() {
        RouteManager.getInstance();

        commands = new HashMap<String, Command>();

        commands.put("help", new HelpCommand());
        commands.put("add", new AddCommand());
        commands.put("info", new InfoCommand());
        commands.put("show", new ShowCommand());
        commands.put("update", new UpdateCommand());
        commands.put("execute_script", new ExecuteScriptCommand());
        commands.put("remove_by_id", new RemoveByIdCommand());
        commands.put("clear", new ClearCommand());
        commands.put("remove_first", new RemoveFirstCommand());
        commands.put("add_if_min", new AddIfMinCommand());
        commands.put("remove_greater", new RemoveGreaterCommand());
        commands.put("remove_all_by_distance", new RemoveAllByDistanceCommand());
        commands.put("count_greater_than_distance", new CountGreaterThanDistanceCommand());
        commands.put("print_descending", new PrintDescendingCommand());
    }

    public static CommandInvoker getInstance() {
        if (instance == null) {
            instance = new CommandInvoker();
        }
        return instance;
    }

    public void clearScriptCounter() {
        scriptCounter = 0;
    }
    public void scriptCount() {
        scriptCounter++;
    }
    public int getScriptCounter() {
        return scriptCounter;
    }

    public Response runCommand(String line, ReadModes readMode) throws IOException {
        String[] tokens = line.split(" ");
        Command command = commands.get(tokens[0].toLowerCase());
        Response response = runCommand(command, Arrays.copyOfRange(tokens, 1, tokens.length), readMode);
        return response;
    }

    public Response runCommand(Command command, String[] args, ReadModes readMode) {
        Response response = null;
        if (command != null) {
            if (args.length > 0) {
                response = command.execute(readMode, args);
            } else {
                response = command.execute(readMode, new String[0]);
            }
        } else {
            System.out.println("Такой команды не существует!");
        }
        return response;
    }
    public Map<String, Command> getCommands() {
        return commands;
    }
}

