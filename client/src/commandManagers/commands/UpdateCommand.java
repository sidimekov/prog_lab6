package commandManagers.commands;

import commandManagers.CommandManager;
import entity.Route;
import enums.ReadModes;
import exceptions.FailedJSONReadException;
import exceptions.FailedValidationException;
import input.InputManager;
import input.JSONManager;

import java.io.BufferedReader;
import java.io.IOException;

public class UpdateCommand extends Command {

    private static String USAGE = "update ИЛИ update <элемент в формате .json>";
    private static String DESC = "обновить значение элемента коллекции, id которого равен заданному";

//    @Override
//    public void execute(ReadModes readMode, String[] args) {
//        CommandManager rm = CommandManager.getInstance();
//        if (args.length == 0) {
//            try {
//                BufferedReader reader = InputManager.getConsoleReader();
//                Route element = CommandManager.buildNew(reader, true);
//                if (readMode == ReadModes.CONSOLE) {
//                    rm.update(element, true); // если с консоли, уже отвалидировано
//                } else {
//                    System.out.printf("Некорректные аргументы, использование: %s\n", USAGE);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            // из файла .json
//            String path = args[0];
//            try {
//                Route element = JSONManager.readElement(path);
//                rm.update(element);
//            } catch (FailedValidationException | FailedJSONReadException e) {
//                System.out.println(e.getMessage());
//                return;
//            }
//        }
//        if (readMode == ReadModes.CONSOLE) {
//            System.out.println("Обновлён элемент в коллекции");
//        }
//    }


    @Override
    public String getDesc() {
        return DESC;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

}
