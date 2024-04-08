package commandManagers;

import builders.RouteBuilder;
import comparators.RouteComparator;
import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;
import exceptions.FailedValidationException;
import input.InputManager;
import input.JSONManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

// Receiver
public class CommandManager {
    private PriorityQueue<Route> collection;
    private static CommandManager instance;
    private static Date initializationDate;

    private CommandManager() {

    }

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
            initializationDate = new Date();
        }
        return instance;
    }

    public static boolean isInitialized() {
        return (instance != null);
    }

    public Date getInitializationDate() {
        return initializationDate;
    }

    public PriorityQueue<Route> getCollection() {
        return collection;
    }

    public Route getById(long id) {
        return collection.stream().filter(el -> el.getId() == id).findFirst().orElse(null);
    }

    public boolean hasElement(long id) {
        return (getById(id) != null);
    }

    public void addElement(Route el) throws FailedValidationException {
        addElement(el, false);
    }

    public void addElement(Route el, boolean skipValidations) throws FailedValidationException {
        if (skipValidations) {
            collection.add(el);
        } else {
            if (CommandManager.validateElement(el)) {
                collection.add(el);
            } else {
                throw new FailedValidationException("Ошибка в валидации");
            }
        }
    }

    public static Route buildNew(BufferedReader reader, boolean withId) throws IOException {
        return RouteBuilder.build(reader, withId);
    }

    public static Route buildNew(BufferedReader reader) throws IOException {
        return buildNew(reader, false);
    }

    /**
     * если пользователь не поставил id, то JSONManager выдаёт ему свой, и под условие ниже он не попадает и добавляется
     * если ввёл id, чтобы заменить элемент с этим id, то прошлый элемент удаляется и новый добавляется
     */
    public void update(Route element, boolean skipValidations) throws FailedValidationException {
        long id = element.getId();
        if (getIds().contains(id)) {
            removeElement(id);
        }
        addElement(element, skipValidations);
    }

    public void update(Route element) throws FailedValidationException {
        update(element, false);
    }

    public void removeElement(long id) {
        removeElement(getById(id));
    }

    public void removeElement(Route route) {
        List<Route> list = convertToList(collection);
        list.remove(route);
        collection = convertFromList(list);
    }

    public List<Long> getIds() {
        return collection.stream().map(Route::getId).collect(Collectors.toList());
    }

    /**
     * Проверить уникальность коллекции
     *
     * @return true, если уникальна, иначе false
     */
    public boolean checkIdUniqueness() {
        ArrayList<Long> ids = (ArrayList<Long>) getIds();
        Set<Long> idSet = new HashSet<>(ids);
        return (idSet.size() == ids.size());
    }

    public Route getMinElement() {
        return collection.stream().min(new RouteComparator()).orElse(null);
    }


//    public static PriorityQueue<Route> convertFromArray(Route[] array) {
//        PriorityQueue<Route> collection = new PriorityQueue<>();
//        collection.addAll(Arrays.asList(array));
//        return collection;
//    }
//
//    public static Route[] convertToArray(PriorityQueue<Route> collection) {
//        return collection.toArray(new Route[0]);
//    }


    /**
     * Конвертировать лист в коллекцию
     * @param list - лист
     * @return - коллекция
     */
    public static PriorityQueue<Route> convertFromList(List<Route> list) {
        PriorityQueue<Route> collection = new PriorityQueue<>();
        collection.addAll(list);
        return collection;
    }


    public static List<Route> convertToList(PriorityQueue<Route> collection) {
        return new ArrayList<>(collection);
    }

    public static boolean validateElement(Route el) {
        if (!Route.checkId(el.getId())) {
            System.out.println("Неверный id (возможно, он уже занят)");
            return false;
        }

        if (!Route.checkName(el.getName())) {
            System.out.println("Неверное имя элемента (Поле не может быть null, Строка не может быть пустой)");
            return false;
        }

        if (!Route.checkCreationDate(el.getCreationDate())) {
            System.out.println("Неверная дата создания (Поле не может быть null)");
            return false;
        }

        Coordinates coordinates = el.getCoordinates();
        if (!Route.checkCoordinates(coordinates)) {
            System.out.println("Некорректные координаты (Поле не может быть null)");
            return false;
        }
        if (!Coordinates.checkX(coordinates.getX()) || !Coordinates.checkY(coordinates.getY())) {
            System.out.println("Некорректные координаты (x: Максимальное значение поля: 790, y: Значение поля должно быть больше -858, Поле не может быть null");
            return false;
        }

        LocationFrom from = el.getFrom();
        if (!Route.checkFrom(from)) {
            System.out.println("Некорректная изначальная локация (Поле не может быть null)");
            return false;
        }
        if (!LocationFrom.checkY(from.getY())) {
            System.out.println("Некорректная изначальная локация (y: Поле не может быть null");
            return false;
        }

        LocationTo to = el.getTo();
        if (to != null) {
            if (!LocationTo.checkY(to.getY()) || !LocationTo.checkName(to.getName())) {
                System.out.println("Некорректная окончательная локация (y: Поле не может быть null, name: Длина строки не должна быть больше 443, Поле не может быть null)");
                return false;
            }
        }

        if (!Route.checkDistance(el.getDistance())) {
            System.out.println("Некорректная дистанция (Значение поля должно быть больше 1)");
            return false;
        }

        return true;
    }

    public void removeAllByDistance(double distance) {
//        collection
//                .stream()
//                .filter(el -> (el.getDistance() == distance))
//                .forEach(el -> RouteManager.getInstance().getCollection().remove(el));
        collection.removeIf(el -> (el.getDistance() == distance));
    }

    public long countGreaterThanDistance(double distance) {
        return collection
                .stream()
                .filter(el -> (el.getDistance() > distance))
                .count();
    }

    public static void printCollection(PriorityQueue<Route> collection) {
        List<Route> list = convertToList(collection);
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста!");
        } else {
            for (int i = 0; i < collection.size(); i++) {
                System.out.printf("Элемент %s / %s:\n%s\n", i + 1, collection.size(), list.get(i));
            }
        }
    }

    public void printDescending() {
        List<Route> list = CommandManager.convertToList(collection);
        list.sort(new RouteComparator());
        Collections.reverse(list);
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста!");
        } else {
            for (int i = 0; i < collection.size(); i++) {
                System.out.printf("Элемент %s / %s:\n%s\n", i + 1, collection.size(), list.get(i));
            }
        }
    }
}
