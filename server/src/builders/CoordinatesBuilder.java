package builders;

import entity.Coordinates;

import java.io.BufferedReader;
import java.io.IOException;

public class CoordinatesBuilder {
    public static Coordinates build(BufferedReader reader) throws IOException {

        System.out.println("Настройка координат...");

        double x;
        while (true) {
            System.out.println("Введите x (double, макс. 790) > ");
            try {
                x = Double.parseDouble(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
            if (Coordinates.checkX(x)) break;
        }

        Integer y = null;
        do {
            System.out.println("Введите y (Integer, не null, больше -858) > ");
            try {
                y = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
        } while (!Coordinates.checkY(y));

        System.out.println("Координаты настроены");
        return new Coordinates(x, y);
    }
}
