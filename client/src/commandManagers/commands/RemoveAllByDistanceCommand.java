package commandManagers.commands;

import commandManagers.CommandManager;
import enums.ReadModes;

public class RemoveAllByDistanceCommand extends Command {
    private final String USAGE = "remove_all_by_distance <дистанция(double)>";
    private final String DESC = "удалить из коллекции все элементы, значение поля distance которого эквивалентно заданному";


//    @Override
//    public void execute(ReadModes readMode, String[] args) {
//        CommandManager rm = CommandManager.getInstance();
//        if (args.length == 1) {
//            double distance;
//            try {
//                distance = Double.parseDouble(args[0]);
//                rm.removeAllByDistance(distance);
//                if (readMode == ReadModes.CONSOLE) {
//                    System.out.printf("Все элементы с дистанцией %s удалены\n", distance);
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Некорректная дистанция");
//            }
//        } else {
//            System.out.printf("Некорректные аргументы, использование: %s\n", USAGE);
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
