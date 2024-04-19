package network;

import java.io.*;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {

    private static Client client;
    public static final int PORT = 8000;
    public static final String HOST = "localhost";

    public SocketChannel socketChannel;

    private Client() {
        client = this;
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public Response sendRequest(Request request) {

        openSocketChannel();

        try (ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream())) {
            oos.writeObject(request);
            oos.flush();

            Response response = (Response) ois.readObject();
            System.out.println(response);

        } catch (IOException e) {
            System.out.println("Ошибка ввода/выводы при отправке запроса на сервер");
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("Не найден класс при чтении объекта из ответа сервера");
        }

        return null;
    }

    public void openSocketChannel() {
        InetSocketAddress addr = new InetSocketAddress(HOST, PORT);

//        Selector selector = null;
//        try {
//            selector = Selector.open();
//        } catch (IOException e) {
//            System.out.println("Ошибка при создании селектора на сервере");
//            return;
//        }

        try {
            socketChannel = SocketChannel.open(addr);
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            System.out.printf("Ошибка при создании сетевого канала по адресу %s:%s\n", HOST, PORT);
            e.printStackTrace();
        }
    }
}
