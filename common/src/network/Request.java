package network;

import enums.ReadModes;
import enums.RequestTypes;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public abstract class Request implements Serializable {
    private final RequestTypes type;

    public Request(RequestTypes type) {
        this.type = type;
    }

    public RequestTypes getType() {
        return type;
    }

}
