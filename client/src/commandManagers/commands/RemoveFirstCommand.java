package commandManagers.commands;

import commandManagers.CommandManager;
import enums.ReadModes;

public class RemoveFirstCommand extends Command {
    public static final String USAGE = "remove_first";
    public static final String DESC = "удалить первый элемент из коллекции";

//    @Override
//    public void execute(ReadModes readMode, String[] args) {
//        CommandManager rm = CommandManager.getInstance();
//        rm.getCollection().remove();
//        if (readMode == ReadModes.CONSOLE) {
//            System.out.println("Первый элемент коллекции удалён");
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
