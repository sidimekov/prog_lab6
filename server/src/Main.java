import network.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Server server = Server.getInstance();

        try {
            server.run();
        } catch (IOException e) {
            System.out.printf("Ошибка ввода-вывода: %s\n", e.getMessage());
            e.printStackTrace();
        }
    }
}