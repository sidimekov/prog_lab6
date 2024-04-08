package util;

import commandManagers.RouteManager;

public class IdManager {
    private static long currentUsedId = 1;

    public static long getId() {
        long maybeId;
        if (RouteManager.isInitialized()) {
            do {
                maybeId = currentUsedId++;
            }
            while (RouteManager.getInstance().getIds().contains(maybeId));
            return maybeId;
        } else {
            return currentUsedId++;
        }
    }
}
