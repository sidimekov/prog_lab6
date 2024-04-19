package commandManagers;

import enums.ReadModes;
import input.InputManager;
import network.Client;
import network.Request;
import network.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class CommandInvoker {
    private static CommandInvoker instance;

    private int scriptCounter;
    public final int SCRIPT_RECURSION_LIMIT = 10;

    private CommandInvoker() {
        CommandManager.getInstance();
    }

    public static CommandInvoker getInstance() {
        if (instance == null) {
            instance = new CommandInvoker();
        }
        return instance;
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

    public void runCommand(String line, ReadModes readMode) throws SocketException {
        String[] tokens = line.split(" ");
        String cmdName = tokens[0].toLowerCase();
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (!cmdName.isEmpty()) {

            Client client = Client.getInstance();
            Request request = new Request(cmdName, args, readMode);

            Response response = client.sendRequest(request);

            System.out.println(response.getMessage());

        } else {
            System.out.println("Такой команды не существует!");
        }
    }

}

