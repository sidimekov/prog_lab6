package network;

import enums.ReadModes;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 5114584243644685314L;

    private String cmdName;
    private String[] args;
    private ReadModes readMode;

    public Request(String cmdName, String[] args, ReadModes readMode) {
        this.cmdName = cmdName;
        this.args = args;
        this.readMode = readMode;
    }

    public String getCommand() {
        return cmdName;
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
                "command=" + cmdName +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
