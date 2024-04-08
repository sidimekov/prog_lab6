package network;

import commandManagers.RouteManager;
import commandManagers.commands.Command;
import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;
import enums.ReadModes;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Server {

    private static Server server;
    public static final int PORT = 8000;

    private Server() {
        server = this;
    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void run() throws IOException {

        RouteManager rm = RouteManager.getInstance();
//        rm.loadCollection();

        while (true) {

            DatagramSocket datagramSocket = new DatagramSocket(PORT);

            Object obj;
            obj = receiveObject(datagramSocket);

//            System.out.println(obj.toString());
            Request request;
            try {
                request = (Request) obj;
            } catch (ClassCastException e) {
                System.out.println("Принятый объект не является запросом");
                continue;
            }

            Response response;
            if (request != null) {
                response = handleRequest(request);
            } else {
                response = new Response("Принятый запрос - null");
            }

            sendObject(response);

            datagramSocket.close();
        }
    }

    private Response handleRequest(Request request) {
        Command command = request.getCommand();
        String[] args = request.getArgs();
        ReadModes readMode = request.getReadMode();

        return command.execute(readMode, args);
    }

    private Object receiveObject(DatagramSocket datagramSocket) throws IOException {
        byte[] buffer = new byte[1024];
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

    public void sendObject(Object obj) throws IOException {
        byte[] data;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            oos.writeObject(obj);
            data = bos.toByteArray();
        }
        sendData(data);
    }

    public void sendData(byte[] data) throws IOException {

        InetAddress address = InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);

        DatagramSocket datagramSocket = new DatagramSocket();

        datagramSocket.send(packet);

        datagramSocket.close();
    }
}
