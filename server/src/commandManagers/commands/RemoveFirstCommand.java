package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import network.Response;

public class RemoveFirstCommand extends Command {
    public static final String USAGE = "remove_first";
    public static final String DESC = "удалить первый элемент из коллекции";

    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        rm.getCollection().remove();
        if (readMode == ReadModes.CONSOLE) {
            return new Response("Первый элемент коллекции удалён", true);
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
