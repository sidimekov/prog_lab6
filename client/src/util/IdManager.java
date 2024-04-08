package util;

import commandManagers.CommandManager;

public class IdManager {
    private static long currentUsedId = 1;

    public static long getId() {
        long maybeId;
        if (CommandManager.isInitialized()) {
            do {
                maybeId = currentUsedId++;
            }
            while (CommandManager.getInstance().getIds().contains(maybeId));
            return maybeId;
        } else {
            return currentUsedId++;
        }
    }
}
