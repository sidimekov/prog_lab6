package input;

import com.google.gson.*;
import commandManagers.RouteManager;
import entity.Route;
import exceptions.FailedJSONReadException;
import util.IdManager;
import util.InputManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;

public class JSONManager {
    public static Route readElement(String path) throws FailedJSONReadException {
        Gson gson = new Gson();
        try {
            Route element = gson.fromJson(new InputStreamReader(new FileInputStream(path)), Route.class);
            JsonObject jsonObject = gson.fromJson(new InputStreamReader(new FileInputStream(path)), JsonObject.class);
            // если id изначально не было, то поменять тот 0, который дал gson, на автоматический
            JsonElement id = jsonObject.get("id");
            if (id == null) {
                element.setId(IdManager.getId());
            }
            return element;
        } catch (FileNotFoundException e) {
            throw new FailedJSONReadException("Не удалось считать из файла json");
        }
    }

    public static void writeElement(String path, Route element) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(element);
        InputManager.write(path, json);
    }

    public static void writeCollection(String path) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PriorityQueue<Route> collection = RouteManager.getInstance().getCollection();
        String json = gson.toJson(collection);
        InputManager.write(path, json);
    }

    public static PriorityQueue<Route> readCollection(String path) throws RuntimeException {
        Gson gson = new Gson();
        Route[] arrayCollection;
        try {
            PriorityQueue<Route> collection = new PriorityQueue<>();
            JsonArray elements = gson.fromJson(new InputStreamReader(new FileInputStream(path)), JsonArray.class);

            // сначала считываем те, у которых указан id, чтоб потом тем, кто без id нормально сгенерить
            for (int i = 0; i < elements.size(); i++) {
                JsonObject element = elements.get(i).getAsJsonObject();
                JsonElement id = element.get("id");
                Route route;
                if (id != null) {
                    route = gson.fromJson(element, Route.class);
                    route.setId(IdManager.getId());
                    collection.add(route);
                }
            }
            for (int i = 0; i < elements.size(); i++) {
                JsonObject element = elements.get(i).getAsJsonObject();
                JsonElement id = element.get("id");
                Route route;
                if (id == null) {
                    route = gson.fromJson(element, Route.class);
                    route.setId(IdManager.getId());
                    collection.add(route);
                }
            }
            return collection;
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
        return new PriorityQueue<>();
    }
}
