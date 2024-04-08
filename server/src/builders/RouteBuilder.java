package builders;

import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;

import java.io.BufferedReader;
import java.io.IOException;

public class RouteBuilder {
    public static Route build(BufferedReader reader, boolean withId) throws IOException {
        System.out.println("Создание маршрута...");

        long id = 0;
        if (withId) {
            while (true) {
                System.out.println("Введите id маршрута (long, больше 0)");
                try {
                    id = Long.parseLong(reader.readLine());
                } catch (NumberFormatException e) {
                    continue;
                }
                if (id > 0) break;
            }
        }

        String name;
        do {
            System.out.println("Введите имя маршрута (String, не null, строка не может быть пустой) > ");
            name = reader.readLine();
        } while (!Route.checkName(name));

        Coordinates coordinates = CoordinatesBuilder.build(reader);

        LocationFrom locFrom = LocationFromBuilder.build(reader);

        LocationTo locTo = LocationToBuilder.build(reader);

        double distance;
        while (true) {
            System.out.println("Введите дистанцию маршрута (double, больше 1) > ");
            try {
                distance = Double.parseDouble(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
            if (Route.checkDistance(distance)) break;
        }

        System.out.println("Маршрут настроен");
        Route route;
        if (withId) {
            route = new Route(id, name, coordinates, locFrom, locTo, distance);
        } else {
            route = new Route(name, coordinates, locFrom, locTo, distance);
        }
        return route;
    }
}
