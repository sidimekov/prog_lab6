package network;

import enums.RequestTypes;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client {
    private static Client client;
    private int clientPort;
    public final InetSocketAddress serverSocketAddr = new InetSocketAddress("localhost", 8000);

    private Client() {
        client = this;
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    /**
     * Отправка запроса по указанным адресу и порту с помощью сетевого канала
     * Затем возвращает Response - ответ от сервера
     * @param request - Запрос
     * @param serverAddr - Адрес сервера для подключения
     * @param serverPort - Порт сервера для подключения
     *
     * @return response - Ответ от сервера
     */
    public Response sendRequest(Request request, InetAddress serverAddr, int serverPort) {

        InetSocketAddress serverSocketAddr = new InetSocketAddress(serverAddr, serverPort);

        try (SocketChannel scClient = SocketChannel.open()) {

            scClient.connect(serverSocketAddr);
            clientPort = scClient.socket().getLocalPort();

            OutputStream out = scClient.socket().getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);

            oos.writeObject(request);

            oos.flush();
            oos.close();

        } catch (FileNotFoundException e) {
            System.out.printf("Файл %s не найден, ошибка: %s\n", request.getFilePath(), e.getMessage());
        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при посылке запроса при создании канала сокетов по адресу %s:%s. Ошибка: %s\n", serverAddr, serverPort, e.getMessage());
            e.printStackTrace();
        }

        Response response = null;

        try (DatagramSocket dsClient = new DatagramSocket(clientPort)) {

            byte[] bytesResponse = new byte[4096];
            DatagramPacket dpClient = new DatagramPacket(bytesResponse, bytesResponse.length);

            dsClient.receive(dpClient);

            byte[] data = dpClient.getData();

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);

            response = (Response) ois.readObject();

            bais.close();
            ois.close();

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при получении ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            System.out.printf("Ошибка при формировании ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
        }

        return response;
    }

    /**
     * Послать файл на сервер, используя сетевой канал
     * @param path - путь к файлу
     * @param serverAddr - адрес сервера
     * @param serverPort - порт сервера
     */
    public void sendFile(String path, InetAddress serverAddr, int serverPort) {

//        File file = new File(path);
        String fileContent = null;

        try {
            fileContent = Files.readString(Path.of(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.printf("Ошибка при отправке содержимого файла: %s", e.getMessage());
        }

        sendResponse(new Response(fileContent), serverAddr, serverPort);

//        try (SocketChannel scClient = SocketChannel.open()) {
//
//            InetSocketAddress serverSocketAddr = new InetSocketAddress(serverAddr, serverPort);
//
//            scClient.connect(serverSocketAddr);
//            clientPort = scClient.socket().getLocalPort();
//
//            OutputStream out = scClient.socket().getOutputStream();
//
//            if (file.exists()) {
//                FileInputStream fis = new FileInputStream(file);
//
//                int count;
//                byte[] buffer = new byte[4096];
//                while ((count = fis.read()) >= 0) {
//                    out.write(buffer, 0, count);
//                }
//
//                out.close();
//                fis.close();
//            } else {
//
//            }
//        } catch (FileNotFoundException e) {
//
//        } catch (IOException e) {
//            System.out.printf("Ошибка ");
//        }
    }

    public void sendResponse(Response response, InetAddress serverAddr, int serverPort) {
        InetSocketAddress serverSocketAddr = new InetSocketAddress(serverAddr, serverPort);

        try (SocketChannel scClient = SocketChannel.open()) {

            scClient.connect(serverSocketAddr);
            clientPort = scClient.socket().getLocalPort();

            ObjectOutputStream oos = new ObjectOutputStream(scClient.socket().getOutputStream());

            oos.writeObject(response);

            oos.flush();
            oos.close();

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при посылке ответа при создании канала сокетов по адресу %s:%s\n", serverAddr, serverPort);
        }
    }

    public Response listenResponse(InetAddress serverAddr, int serverPort) {
//        System.out.println("Слушаю ответ");

        Response response = null;

        try (DatagramSocket dsClient = new DatagramSocket(clientPort)) {

            byte[] bytesResponse = new byte[4096];
            DatagramPacket dpClient = new DatagramPacket(bytesResponse, bytesResponse.length);

            dsClient.receive(dpClient);

            byte[] data = dpClient.getData();

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);

            response = (Response) ois.readObject();

            bais.close();
            ois.close();

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при получении ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            System.out.printf("Ошибка при формировании ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
        }

        return response;

    }

    public Response listenRequest(InetAddress serverAddr, int serverPort) {
//        System.out.println("Слушаю ответ");

        Request request = null;

        try (DatagramSocket dsClient = new DatagramSocket(clientPort)) {

            byte[] bytesResponse = new byte[4096];
            DatagramPacket dpClient = new DatagramPacket(bytesResponse, bytesResponse.length);

            dsClient.receive(dpClient);

            byte[] data = dpClient.getData();

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);

            request = (Request) ois.readObject();

            bais.close();
            ois.close();

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при получении ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
//            e.printStackTrace();
        } catch (ClassNotFoundException | ClassCastException e) {
//            e.printStackTrace();
            System.out.printf("Ошибка при формировании ответа с сервера по адресу %s:%s : %s\n", serverAddr, serverPort, e.getMessage());
        }

        // Обработка запроса (пока может принимать только запросы типа Message при правильном использовании)
        Response response = null;
        if (request != null && request.getType() == RequestTypes.MESSAGE) {
            response = new Response();
        }
        return response;

    }
}
