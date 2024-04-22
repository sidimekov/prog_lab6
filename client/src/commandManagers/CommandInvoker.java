package commandManagers;

import enums.ReadModes;
import input.InputManager;
import network.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;

public class CommandInvoker {
    private static CommandInvoker instance;

    private CommandInvoker() {
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

    public void runCommand(String line, ReadModes readMode) {
        String[] tokens = line.split(" ");
        String cmdName = tokens[0].toLowerCase();
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (!cmdName.isEmpty()) {

            Client client = Client.getInstance();
            Request request = new CommandRequest(cmdName, args, readMode);

            InetSocketAddress serverSocketAddr = client.serverSocketAddr;

            Response response;

            response = client.sendRequest(request, serverSocketAddr.getAddress(), serverSocketAddr.getPort());

            System.out.println(response.getMessage());

            while (!response.isFinal()) {
                response = client.listenResponse(serverSocketAddr.getAddress(), serverSocketAddr.getPort());

                if (response.hasResponseRequest()) {
                    // Если сервер при посылке ответа, послал запрос (например передать элемент)

                    BuildRequest buildRequest = (BuildRequest) response.getResponseRequest();

                    Response buildResponse = listenResponse(buildRequest.getMessage());
                    System.out.println(buildRequest.getMessage());

                    client.sendResponse(buildResponse, serverSocketAddr.getAddress(), serverSocketAddr.getPort());

                } else {
                    System.out.println(response.getMessage());
                }
            }

        } else {
            System.out.println("Пустая команда!");
        }
    }

    public Response listenResponse(String message) {
        try (BufferedReader reader = InputManager.getConsoleReader()) {
//            while (true) {
                System.out.println(message);
                String line = reader.readLine();
                return new Response(line);
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

