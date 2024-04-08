package network;

import commandManagers.commands.Command;
import enums.ReadModes;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 5114584243644685314L;

    private Command command;
    private String[] args;
    private ReadModes readMode;

    public Request(Command command, String[] args, ReadModes readMode) {
        this.command = command;
        this.args = args;
        this.readMode = readMode;
    }

    public Command getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    public ReadModes getReadMode() {
        return readMode;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command=" + command +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
