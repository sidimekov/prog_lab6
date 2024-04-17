package network;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {

    private static Client client;
    public static final int PORT = 8000;
    public static final String HOST = "localhost";
    private DatagramSocket datagramSocket;

    private Client() {
        client = this;
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public void sendObject(Object obj) throws IOException {
        byte[] data;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            oos.writeObject(obj);
            data = bos.toByteArray();
        } catch (IOException e) {
            System.out.printf("Ошибка ввода-вывода в потоке объектов: %s\n", e.getMessage());
//            e.printStackTrace();
            return;
        }
        sendData(data);
    }

    private void sendData(byte[] data) throws IOException {

        InetAddress address = InetAddress.getByName(HOST);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);

//        DatagramSocket datagramSocket = new DatagramSocket();


        datagramSocket.send(packet);
//        System.out.println("Послано");
//        datagramSocket.close();
    }


    public Object receiveObject(DatagramSocket datagramSocket) throws IOException {
        byte[] buffer = new byte[512];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        datagramSocket.receive(packet);

        byte[] data = packet.getData();
        Object obj;

        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
        ) {
            obj = ois.readObject();
            return obj;
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка: не найден класс при чтении запроса");
            return null;
        }
    }

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }
}
