package commandManagers.commands;

import commandManagers.CommandManager;
import entity.Route;
import enums.ReadModes;
import exceptions.FailedJSONReadException;
import exceptions.FailedValidationException;
import input.InputManager;
import input.JSONManager;
import network.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serial;

public class AddIfMinCommand extends Command {
    @Serial
    private static final long serialVersionUID = -2899451559481014307L;
    private static final String USAGE = "add_if_min ИЛИ add_if_min <элемент в формате .json>";
    private static final String DESC = "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
}
