package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
import exceptions.FailedJSONReadException;
import exceptions.FailedValidationException;
import input.JSONManager;
import network.Response;
import util.InputManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serial;

public class AddCommand extends Command {
    @Serial
    private static final long serialVersionUID = -2914911397015071577L;
    private String USAGE = "add (только в консоли) ИЛИ add <элемент в формате .json>";
    private String DESC = "добавить новый элемент в коллекцию";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (args.length == 0) {
            // если нет аргументов, то нужно построить из консоли, значит если файл то бан
            if (readMode == ReadModes.CONSOLE) {
                try {
                    BufferedReader reader = InputManager.getConsoleReader();

                    Route element = RouteManager.buildNew(reader); // если с консоли

                    rm.addElement(element, true);

                } catch (IOException e) {
                    return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getMessage()));
                }
            } else {
                return new Response(String.format("Ошибка в использовании аргументов. Использование: %s", USAGE));
            }
        } else {
            // из файла .json
            String path = args[0];
            try {
                Route element = JSONManager.readElement(path);
                RouteManager.getInstance().addElement(element);
            } catch (FailedValidationException | FailedJSONReadException e) {
                return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getMessage()));
            }
        }
        return new Response("Добавлен элемент в коллекцию");
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

    @Override
    public String getDesc() {
        return DESC;
    }
}
