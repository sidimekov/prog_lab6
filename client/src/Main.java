
import commandManagers.CommandInvoker;

public class Main {

    public static void main(String[] args) {
        CommandInvoker invoker = CommandInvoker.getInstance();
        invoker.listenCommands();

//        while (true) {
//
//            System.out.println("Введите сообщение, которое хотите направить на сервер");
//
//            Scanner sc = new Scanner(System.in);
//            String request = sc.next();
//
//            int clientLocalPort;
//
//            // отправка запроса клиентом с помощью сетевых каналов
//
//            try (SocketChannel socketChannel = SocketChannel.open()) {
//
//                InetSocketAddress serverSocketAddr = new InetSocketAddress("localhost", 8000);
//                socketChannel.connect(serverSocketAddr);
//                clientLocalPort = socketChannel.socket().getLocalPort();
//
//                System.out.printf("Установлено соединение с %s:%s\n", serverSocketAddr.getAddress(), serverSocketAddr.getPort());
//
//                ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
//
////                String request = "Сообщение серверу";
//                oos.writeObject(request);
//                System.out.println("Послал сообщение серверу");
//
//                oos.flush();
//                oos.close();
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            // приём ответа клиентом с помощью датаграмм
//
//            try (DatagramSocket dsClient = new DatagramSocket(clientLocalPort)) {
//                System.out.println("Ожидание ответа от сервера...");
//
//                byte[] bytesResponse = new byte[1024];
//                DatagramPacket dpResponse = new DatagramPacket(bytesResponse, bytesResponse.length);
//
//                dsClient.receive(dpResponse);
//                System.out.println("Данные получены");
//
//                byte[] responseData = dpResponse.getData();
//
//                ByteArrayInputStream bais = new ByteArrayInputStream(responseData);
//                ObjectInputStream ois = new ObjectInputStream(bais);
//
//                String response = (String) ois.readObject();
//
//                bais.close();
//                ois.close();
//
//                System.out.printf("Клиент получил ответ: %s\n", response);
//            } catch (IOException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}