package commandManagers.commands;

import commandManagers.CommandManager;
import entity.Route;
import enums.ReadModes;

import java.util.PriorityQueue;

public class ShowCommand extends Command {
    private static String USAGE = "show";
    private static String DESC = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";

//    @Override
//    public void execute(ReadModes readMode, String[] args) {
//        CommandManager rm = CommandManager.getInstance();
//        PriorityQueue<Route> collection = rm.getCollection();
//        CommandManager.printCollection(collection);
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
