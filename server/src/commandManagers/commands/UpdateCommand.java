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

public class UpdateCommand extends Command {

    private static String USAGE = "update ИЛИ update <элемент в формате .json>";
    private static String DESC = "обновить значение элемента коллекции, id которого равен заданному";

    private String jsonContent;
    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (args.length == 0) {
            // если нет аргументов, то нужно построить из консоли, значит если файл то бан
            if (readMode == ReadModes.CONSOLE) {
                try {
                    BufferedReader reader = InputManager.getConsoleReader();
                    Route element = RouteManager.buildNew(reader, true);
                    rm.update(element, true); // если с консоли, уже отвалидировано
                } catch (IOException e) {
                    return new Response(e.getMessage());
                }
            } else {
                return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE));
            }
        } else {
            // из файла .json
            if (jsonContent != null) {
                try {
                    Route element = JSONManager.readElement(jsonContent);
                    jsonContent = null;
                    rm.update(element);
                } catch (FailedValidationException | FailedJSONReadException e) {
                    return new Response(e.getMessage());
                }
            } else {
                return new Response("Файл не найден / был пуст");
            }
        }
        return new Response("Обновлён элемент в коллекции");
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
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
