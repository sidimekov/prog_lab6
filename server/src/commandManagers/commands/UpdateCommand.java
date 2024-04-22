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

public class UpdateCommand extends Command {

    private static String USAGE = "update ИЛИ update <элемент в формате .json>";
    private static String DESC = "обновить значение элемента коллекции, id которого равен заданному";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (args.length == 0) {
            try {
                BufferedReader reader = InputManager.getConsoleReader();
                Route element = RouteManager.buildNew(reader, true);
                if (readMode == ReadModes.CONSOLE) {
                    rm.update(element, true); // если с консоли, уже отвалидировано
                } else {
                    return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE), true);
                }
            } catch (IOException e) {
                return new Response(e.getMessage(), true);
            }
        } else {
            // из файла .json
            String path = args[0];
            try {
                Route element = JSONManager.readElement(path);
                rm.update(element);
            } catch (FailedValidationException | FailedJSONReadException e) {
                return new Response(e.getMessage(), true);
            }
        }
        if (readMode == ReadModes.CONSOLE) {
            return new Response("Обновлён элемент в коллекции", true);
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
