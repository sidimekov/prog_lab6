package network;

import commandManagers.CommandInvoker;
import commandManagers.RouteManager;
import commandManagers.commands.Command;
import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;
import enums.ReadModes;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Server {

    private static Server server;
    public static final int PORT = 8000;

    private InetAddress clientAddr = null;

    private DatagramSocket datagramSocket = null;
    private int clientPort = 0;

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

            datagramSocket = new DatagramSocket(PORT);

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
                System.out.println("ща handlю реквеcт");
                response = handleRequest(request);
                System.out.println(response.getMessage());
            } else {
                response = new Response("Принятый запрос - null");
            }

            if (clientAddr != null && clientPort != 0) {
                System.out.println("ща пошлю клиенту ответ");
                sendObject(response, clientAddr, clientPort);
                System.out.println("послал " + response.getMessage() + "  " + clientAddr + " " + clientPort);
            }


            datagramSocket.close();
        }
    }

    private Response handleRequest(Request request) {
        Command command = request.getCommand();
        String[] args = request.getArgs();
        ReadModes readMode = request.getReadMode();

        Response response = CommandInvoker.getInstance().runCommand(command, args, readMode);

        return response;
    }

    private Object receiveObject(DatagramSocket datagramSocket) throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(packet);

        byte[] data = packet.getData();
        Object obj;

        clientAddr = packet.getAddress();
        clientPort = packet.getPort();

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
    public void sendObject(Object obj, InetAddress addr, int port) throws IOException {
        byte[] data;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            oos.writeObject(obj);
            data = bos.toByteArray();
        }
        sendData(data, addr, port);
    }

    public void sendData(byte[] data, InetAddress address, int port) throws IOException {

        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

        datagramSocket.send(packet);
    }

    public void sendData(byte[] data) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        sendData(data, address, PORT);
    }
}
