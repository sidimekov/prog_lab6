package builders;

import entity.LocationTo;

import java.io.BufferedReader;
import java.io.IOException;

public class LocationToBuilder {
    public static LocationTo build(BufferedReader reader) throws IOException {
        System.out.println("Настройка окончательной локации...");

        String name;
        do {
            System.out.println("Введите имя окончательной локации (String, не null, длина не больше 443) > ");
            name = reader.readLine();
        } while (!LocationTo.checkName(name));

        float x;
        while (true) {
            System.out.println("Введите x (float) > ");
            try {
                x = Float.parseFloat(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
            if (LocationTo.checkX(x)) break;
        }

        Integer y = null;
        do {
            System.out.println("Введите y (Integer, не null) > ");
            try {
                y = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
        } while (!LocationTo.checkY(y));

        long z;
        while (true) {
            System.out.println("Введите z (long) > ");
            try {
                z = Long.parseLong(reader.readLine());
            } catch (NumberFormatException e) {
                continue;
            }
            if (LocationTo.checkZ(z)) break;
        }

        System.out.println("Окончательная локация настроена");
        return new LocationTo(name, x, y, z);
    }
}
