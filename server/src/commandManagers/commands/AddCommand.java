package commandManagers.commands;

import commandManagers.RouteManager;
import entity.Route;
import enums.ReadModes;
import exceptions.FailedJSONReadException;
import exceptions.FailedValidationException;
import input.InputManager;
import input.JSONManager;
import network.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serial;

public class AddCommand extends Command {
    @Serial
    private static final long serialVersionUID = -2914911397015071577L;
    private String USAGE = "add ИЛИ add <элемент в формате .json>";
    private String DESC = "добавить новый элемент в коллекцию";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (args.length == 0) {
            try {
                BufferedReader reader = InputManager.getConsoleReader();
                Route element = RouteManager.buildNew(reader); // если с консоли

                rm.addElement(element, true);

            } catch (IOException e) {
                return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getStackTrace()));
            }
        } else {
            // из файла .json
            String path = args[0];
            try {
                Route element = JSONManager.readElement(path);
                RouteManager.getInstance().addElement(element);
            } catch (FailedValidationException | FailedJSONReadException e) {
                return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getStackTrace()));
            }
        }
        if (readMode == ReadModes.CONSOLE) {
            return new Response("Добавлен элемент в коллекцию");
        }
        return new Response();
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
