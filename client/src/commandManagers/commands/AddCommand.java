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

public class AddCommand extends Command {
    @Serial
    private static final long serialVersionUID = -2914911397015071577L;
    private static final String USAGE = "add ИЛИ add <элемент в формате .json>";
    private static final String DESC = "добавить новый элемент в коллекцию";

}
