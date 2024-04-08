package commandManagers.commands;

import commandManagers.CommandManager;
import enums.ReadModes;

public class RemoveByIdCommand extends Command {
    public static final String USAGE = "remove_by_id <id>";
    public static final String DESC = "удалить элемент из коллекции по его id";
//    @Override
//    public void execute(ReadModes readMode, String[] args) {
//        CommandManager rm = CommandManager.getInstance();
//        if (args.length == 1) {
//            long id;
//            try {
//                id = Long.parseLong(args[0]);
//            } catch (NumberFormatException e) {
//                System.out.printf("Некорректные аргументы, используйте: %s\n", USAGE);
//                return;
//            }
//            if (rm.hasElement(id)) {
//                rm.removeElement(id);
//            } else {
//                System.out.println("Нет элемента с таким id");
//                return;
//            }
//        } else {
//            System.out.printf("Некорректные аргументы, используйте: %s\n", USAGE);
//            return;
//        }
//        if (readMode == ReadModes.CONSOLE) {
//            System.out.println("Элемент удалён");
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
