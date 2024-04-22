package input;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InputManager {
    private static BufferedReader consoleReader;

    public static BufferedReader getConsoleReader() {
        if (consoleReader == null) {
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
        }
        return consoleReader;
    }
}
