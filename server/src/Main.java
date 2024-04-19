import network.Server;
import java.nio.channels.SocketChannel;

public class Main {

    private static SocketChannel clientSocketChannel;

    public static void main(String[] args) {
        Server server = Server.getInstance();

        server.run("localhost", 8000);

//        while (true) {
//
//            InetAddress clientAddr;
//            int clientPort;
//
//            Object obj;
//
//            // приём запроса сервером с помощью сетевых каналов
//
//            try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
//                ssc.socket().bind(new InetSocketAddress(8000));
//                clientSocketChannel = ssc.accept();
//                ssc.configureBlocking(false);
//
//                clientAddr = clientSocketChannel.socket().getInetAddress();
//                clientPort = clientSocketChannel.socket().getPort();
//                System.out.printf("Установлено соединение с %s:%s\n", clientAddr, clientPort);
//
//                ObjectInputStream ois = new ObjectInputStream(clientSocketChannel.socket().getInputStream());
//
//                obj = ois.readObject();
//                System.out.printf("Получен объект %s\n", obj.toString());
//                ois.close();
//
//
//            } catch (IOException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//
//            // Отправка ответа сервером через датаграммы
//
//            try (DatagramSocket dsResponse = new DatagramSocket()) {
//
//                System.out.println("Посылка ответа клиенту...");
//
//
//                String response = String.format("я ща получил объект %s, спасибо тебе чел\n", obj);
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ObjectOutputStream oos = new ObjectOutputStream(baos);
//
//                oos.writeObject(response);
//                byte[] byteResponse = baos.toByteArray();
//
//                DatagramPacket dpResponse = new DatagramPacket(byteResponse, byteResponse.length, clientAddr, clientPort);
//
//                dsResponse.send(dpResponse);
//                System.out.println("Ответ послан клиенту");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}