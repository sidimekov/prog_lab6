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

public class AddIfMinCommand extends Command {
    @Serial
    private static final long serialVersionUID = -2899451559481014307L;
    public static final String USAGE = "add_if_min ИЛИ add_if_min <элемент в формате .json>";
    public static final String DESC = "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        Route minElement = rm.getMinElement();
        Route element;
        if (args.length == 0) {
            try {
                BufferedReader reader = InputManager.getConsoleReader();
                element = RouteManager.buildNew(reader); // если с консоли
            } catch (IOException e) {
                return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getStackTrace()));
            }
        } else {
            String path = args[0];
            try {
                element = JSONManager.readElement(path);
            } catch (FailedValidationException | FailedJSONReadException e) {
                return new Response(String.format("Ошибка при добавлении в коллекцию: %s\n", e.getStackTrace()));
            }
        }


        if (element.compareTo(minElement) < 0) {
            if (readMode == ReadModes.CONSOLE) {
                rm.addElement(element, true);
                return new Response("Минимальный элемент добавлен в коллекцию");
            } else {
                rm.addElement(element);
            }
        } else {
            if (readMode == ReadModes.CONSOLE) {
                return new Response("Указанный элемент не будет самым минимальным");
            }
        }
        return new Response();
    }

    @Override
    public String getDesc() {
        return DESC;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }
}
