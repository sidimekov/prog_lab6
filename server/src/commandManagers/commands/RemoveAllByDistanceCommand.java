package commandManagers.commands;

import commandManagers.RouteManager;
import enums.ReadModes;
import network.Response;

public class RemoveAllByDistanceCommand extends Command {
    private final String USAGE = "remove_all_by_distance <дистанция(double)>";
    private final String DESC = "удалить из коллекции все элементы, значение поля distance которого эквивалентно заданному";


    @Override
    public Response execute(ReadModes readMode, String[] args) {
        RouteManager rm = RouteManager.getInstance();
        if (args.length == 1) {
            double distance;
            try {
                distance = Double.parseDouble(args[0]);
                rm.removeAllByDistance(distance);
                return new Response(String.format("Все элементы с дистанцией %s удалены\n", distance));
            } catch (NumberFormatException e) {
                return new Response("Некорректная дистанция");
            }
        } else {
            return new Response(String.format("Некорректные аргументы, использование: %s\n", USAGE));
        }
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
