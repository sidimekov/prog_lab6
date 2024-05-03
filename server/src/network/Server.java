package network;

import commandManagers.CommandInvoker;
import commandManagers.RouteManager;
import commandManagers.commands.Command;
import commandManagers.commands.ExecuteScriptCommand;
import enums.ReadModes;
import enums.RequestTypes;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    private static Server server;
    private InetSocketAddress serverSocketAddr;
    private InetAddress clientAddr;
    private int clientPort;

    private Server() {
        server = this;
    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void run(String serverAddr, int serverPort) {

        RouteManager rm = RouteManager.getInstance();

        this.serverSocketAddr = new InetSocketAddress(serverAddr, serverPort);

        System.out.printf("Сервер запущен по адресу: %s:%s\n", serverAddr, serverPort);

        while (true) {

            Response response = listenRequest();

            sendResponse(response);
        }
    }

    /**
     * Ожидает запросы от клиента, после получения выполняет запрос и формирует ответ
     * Получение запроса с помощью сетевого канала
     *
     * @return response - Возвращаемый ответ от сервера
     */
    private Response listenRequest() {
        Response response = null;
        Request request = null;

        String fileContent = null;

        try (ServerSocketChannel sscServer = ServerSocketChannel.open()) {


            sscServer.bind(serverSocketAddr);

            SocketChannel scClient = sscServer.accept();

            clientAddr = scClient.socket().getInetAddress();
            clientPort = scClient.socket().getPort();

            ObjectInputStream ois = new ObjectInputStream(scClient.socket().getInputStream());
            request = (Request) ois.readObject();

            ois.close();

            String msg = request.toString();
            System.out.printf("Получен запрос от %s:%s : %s\n", clientAddr, clientPort, msg);

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при получении запросов: %s\n", e.getMessage());
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.printf("Ошибка при формировании запроса от клиента по адресу %s:%s\n", clientAddr, clientPort);
        }

        if (request != null && request.getFilePath() != null) {

            // посылка запроса контента файла, если путь был указан

            // создание запроса FileRequest, путь указывается который был послан серверу как аргумент
            FileRequest fileRequest = new FileRequest();
            fileRequest.setFilePath(request.getFilePath());

            // формирование ответа с запросом FileRequest и его отправка
            Response serverResponse = new Response(fileRequest);

            // получение того, что в файле клиента
            Response clientFileContent = sendResponse(serverResponse);

            fileContent = clientFileContent.getMessage();
        }

        // обработка запроса
        try {
            if (request.getType() == RequestTypes.COMMAND) {
                if (fileContent == null) {
                    response = handleRequest((CommandRequest) request);
                } else {
                    response = handleRequest((CommandRequest) request, fileContent);
                }
            }
        } catch (NullPointerException e) {
            System.out.printf("Запрос оказался null: %s\n", e.getMessage());
        }

        return response;
    }

    /**
     * Ожидает ответ от клиента, на отправленный сервером запрос (запросы от сервера - buildRequest или fileRequest)
     *
     * @return response - полученный ответ
     */
    public Response listenResponse() {
//        System.out.println("Слушаю ответ");
        Response response = null;

        try (ServerSocketChannel sscServer = ServerSocketChannel.open()) {

            sscServer.bind(serverSocketAddr);

            SocketChannel scClient = sscServer.accept();

            clientAddr = scClient.socket().getInetAddress();
            clientPort = scClient.socket().getPort();

            ObjectInputStream ois = new ObjectInputStream(scClient.socket().getInputStream());
            response = (Response) ois.readObject();

            ois.close();

            String msg = response.getMessage();
            System.out.printf("Получен ответ от %s:%s : %s...\n", clientAddr, clientPort, msg.length() > 16 ? msg.substring(0, 16) : msg);

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при получении запросов: %s\n", e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.printf("Ошибка при формировании запроса от клиента по адресу %s:%s. Ошибка: %s\n", clientAddr, clientPort, e.getMessage());
        }

        return response;
    }

    /**
     * Обработать командный запрос, указанный в параметрах и вернуть ответ
     *
     * @param request - Запрос для обработки
     * @return response - Ответ после запроса
     */
    private Response handleRequest(CommandRequest request, String fileContent) {
        Response response = null;

//        System.out.println("fileContent: " + fileContent);
        String cmdName = request.getCommand();
        String[] args = request.getArgs();
        ReadModes readMode = request.getReadMode();

        CommandInvoker cmdInvoker = CommandInvoker.getInstance();
        Command command = cmdInvoker.getCommand(cmdName);

        if (request.getFilePath() != null && fileContent != null) {
            switch (cmdName) {
                case "execute_script" -> {
                    ExecuteScriptCommand exe = (ExecuteScriptCommand) command;

                    exe.setScript(fileContent);
                }
                case "add", "add_if_min" -> {

                }
                case "update" -> {
                    //update
                }
            }
        }

        if (command == null) {
            response = new Response("Указанной команды не существует!");
            System.out.println("Такой команды не существует, отправка ответа клиенту...");
        } else {
            response = cmdInvoker.runCommand(command, args, readMode);
            System.out.printf("Команда %s выполнена, отправка ответа клиенту...\n", cmdName);
        }

        return response;
    }

    private Response handleRequest(CommandRequest request) {
        return handleRequest(request, null);
    }


    /**
     * Отправка ответа на запрос клиенту
     *
     * @param response - отправляемый ответ
     */
    public Response sendResponse(Response response) {

        try (DatagramSocket dsServer = new DatagramSocket()) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(response);

            oos.flush();
            oos.close();

            baos.close();
            byte[] byteResponse = baos.toByteArray();

            DatagramPacket dpServer = new DatagramPacket(byteResponse, byteResponse.length, clientAddr, clientPort);

            dsServer.send(dpServer);

            if (response.hasResponseRequest()) {
                System.out.printf("Отправлен ответ с запросом типа %s клиенту по адресу %s:%s\n\n", response.getResponseRequest().getType().toString(), clientAddr, clientPort);
            } else {
                System.out.printf("Отправлен ответ клиенту по адресу %s:%s\n\n", clientAddr, clientPort);
//                System.out.println(response);
            }
//            System.out.printf("Запрос: %s\n", response.getMessage());

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при посылке ответа клиенту по адресу %s:%s : %s\n", clientAddr, clientPort, e.getMessage());
            return null;
        }

        if (response.hasResponseRequest()) {
            // Наш посланный ответ содержит запрос, поэтому нужно выждать ответ

            Response clientResponse = listenResponse();

            return clientResponse;

        } else {
            return null;
        }
    }

    public Response sendRequest(Request request) {
        try (DatagramSocket dsServer = new DatagramSocket()) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(request);

            oos.flush();
            oos.close();

            baos.close();
            byte[] byteResponse = baos.toByteArray();

            DatagramPacket dpServer = new DatagramPacket(byteResponse, byteResponse.length, clientAddr, clientPort);

            dsServer.send(dpServer);

            System.out.printf("Отправлен запрос клиенту по адресу %s:%s\n\n", clientAddr, clientPort);
//            System.out.printf("Запрос: %s\n", response.getMessage());

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при посылке запроса клиенту по адресу %s:%s : %s\n", clientAddr, clientPort, e.getMessage());
            return null;
        }

        Response response = listenResponse();

        return response;
    }
}
