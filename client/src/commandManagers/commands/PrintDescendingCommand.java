package commandManagers.commands;

import commandManagers.CommandManager;
import enums.ReadModes;
import network.Response;

public class PrintDescendingCommand extends Command {
    private final String USAGE = "print_descending";
    private final String DESC = "вывести элементы коллекции в порядке убывания";

//    @Override
//    public Response execute(ReadModes readMode, String[] args) {
//        CommandManager rm = CommandManager.getInstance();
//        rm.printDescending();
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
