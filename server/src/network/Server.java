package network;

import commandManagers.CommandInvoker;
import commandManagers.RouteManager;
import commandManagers.commands.Command;
import enums.ReadModes;
import enums.RequestTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

        try (ServerSocketChannel sscServer = ServerSocketChannel.open()) {


            sscServer.bind(serverSocketAddr);

            SocketChannel scClient = sscServer.accept();

            clientAddr = scClient.socket().getInetAddress();
            clientPort = scClient.socket().getPort();

            ObjectInputStream ois = new ObjectInputStream(scClient.socket().getInputStream());
            request = (Request) ois.readObject();

            ois.close();

            String msg = request.toString();
            System.out.printf("Получен запрос от %s:%s : %s\n", clientAddr, clientPort, msg.length() > 16 ? msg.substring(0,16) : msg);


        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при получении запросов: %s\n", e.getMessage());
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.printf("Ошибка при формировании запроса от клиента по адресу %s:%s\n", clientAddr, clientPort);
        }

        try {
            if (request.getType() == RequestTypes.COMMAND) {
                response = handleRequest((CommandRequest) request);
            }
        } catch (NullPointerException e) {
            System.out.printf("Запрос оказался null: %s\n", e.getMessage());
        }

        return response;
    }

    /**
     * Ожидает ответ от клиента, на отправленный сервером запрос (в основном запросы от сервера - buildRequest)
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
            System.out.printf("Получен ответ от %s:%s : %s\n", clientAddr, clientPort, msg.length() > 16 ? msg.substring(0,16) : msg);

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при получении запросов: %s\n", e.getMessage());
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
    private Response handleRequest(CommandRequest request) {
        Response response = null;

        String cmdName = request.getCommand();
        String[] args = request.getArgs();
        ReadModes readMode = request.getReadMode();

        CommandInvoker cmdInvoker = CommandInvoker.getInstance();
        Command command = cmdInvoker.getCommand(cmdName);


        if (command == null) {
            response = new Response("Указанной команды не существует!", true);
            System.out.println("Такой команды не существует, отправка ответа клиенту...");
        } else {
            response = cmdInvoker.runCommand(command, args, readMode);
            System.out.println("Команда выполнена, отправка ответа клиенту...");
        }

        return response;
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
                System.out.printf("Отправлен ответ с запросом клиенту по адресу %s:%s, ответ: %s...\n\n", clientAddr, clientPort, response.getMessage());
            } else {
                System.out.printf("Отправлен ответ клиенту по адресу %s:%s, ответ: %s...\n\n", clientAddr, clientPort, response.getMessage());
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
}
