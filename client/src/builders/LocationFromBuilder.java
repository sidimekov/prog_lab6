package builders;

import entity.LocationFrom;

import java.io.BufferedReader;
import java.io.IOException;

public class LocationFromBuilder {
    public static LocationFrom build(BufferedReader reader) throws IOException {
        System.out.println("Настройка изначальной локации...");

        int x;
        System.out.println("Введите x (int) > ");
        while (true) {
            try {
                x = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
            if (LocationFrom.checkX(x)) break;
        }

        Integer y = null;
        do {
            System.out.println("Введите y (Integer, не null) > ");
            try {
                y = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
        } while (!LocationFrom.checkY(y));

        float z;
        while (true) {
            System.out.println("Введите z (float) > ");
            try {
                z = Float.parseFloat(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
            if (LocationFrom.checkZ(z)) break;
        }

        System.out.println("Изначальная локация настроена");
        return new LocationFrom(x, y, z);
    }
}
