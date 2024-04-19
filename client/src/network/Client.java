package network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

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


            ObjectOutputStream oos = new ObjectOutputStream(scClient.socket().getOutputStream());

            oos.writeObject(request);

            oos.flush();
            oos.close();

        } catch (IOException e) {
            System.out.printf("Ошибка ввода/вывода при создании канала сокетов по адресу %s:%s\n", serverAddr, serverPort);
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
}
