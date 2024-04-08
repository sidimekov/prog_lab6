package commandManagers;

import commandManagers.commands.*;
import enums.ReadModes;
import input.InputManager;
import commandManagers.commands.Command;
import network.Client;
import network.Request;
import network.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    private Map<String, Command> commands;
    private static CommandInvoker instance;

    private int scriptCounter;
    public final int SCRIPT_RECURSION_LIMIT = 10;

    private CommandInvoker() {
        CommandManager.getInstance();

        commands = new HashMap<String, Command>();

        commands.put("help", new HelpCommand());
        commands.put("add", new AddCommand());
        commands.put("info", new InfoCommand());
        commands.put("show", new ShowCommand());
        commands.put("update", new UpdateCommand());
        commands.put("execute_script", new ExecuteScriptCommand());
        commands.put("save", new SaveCommand());
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

    public void listenCommands() {
        try (BufferedReader reader = InputManager.getConsoleReader()) {
            while (true) {
                String line = reader.readLine();
                runCommand(line, ReadModes.CONSOLE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runCommand(String line, ReadModes readMode) {
        String[] tokens = line.split(" ");
        Command command = commands.get(tokens[0].toLowerCase());
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (command != null) {

            Client client = Client.getInstance();
            Request request = new Request(command, args, readMode);
            Response response = null;

            try {
                client.sendObject(request);
                response = (Response) client.receiveObject();

            } catch (IOException e) {
                System.out.printf("Ошибка ввода-вывода при посылке запроса на сервер: %s\n", e.getMessage());
            } catch (ClassCastException e) {
                System.out.println("Принятый объект не является ответом");
            }

            if (response != null) {
                System.out.println(response.getMessage());
            } else {
                System.out.println("Ответ на запрос - null");
            }

        } else {
            System.out.println("Такой команды не существует!");
        }
    }
    public Map<String, Command> getCommands() {
        return commands;
    }
}

