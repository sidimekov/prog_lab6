package commandManagers.commands;

import commandManagers.CommandInvoker;
import enums.ReadModes;
import network.Response;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteScriptCommand extends Command {
    public static final String USAGE = "execute_script <имя файла>";
    public static final String DESC = "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        if (args.length == 1) {
            String path = args[0];
            path = path.replaceAll("\"", "");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
                CommandInvoker invoker = CommandInvoker.getInstance();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (readMode == ReadModes.CONSOLE) {
                        invoker.clearScriptCounter();
                    } else {
                        invoker.scriptCount();
                    }
                    if (invoker.getScriptCounter() < invoker.SCRIPT_RECURSION_LIMIT) {
                        invoker.runCommand(line, ReadModes.FILE);
                    } else {
                        // чтоб не спамило:
                        if (invoker.getScriptCounter() == invoker.SCRIPT_RECURSION_LIMIT)
                            return new Response("Рекурсивный вызов скриптов!");
                        break;
                    }
                }
            } catch (IOException e) {
//                throw new RuntimeException(e);
                return new Response("Не удалось считать данные из файла (возможно, файл не найден)");
            }
        } else {
            return new Response(String.format("Неверное количество аргументов (got %s, expected 1)", args.length));
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
