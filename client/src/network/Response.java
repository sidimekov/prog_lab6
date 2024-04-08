package network;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = -1202337532505101798L;
    private String message;

    public Response(String message) {
        this.message = message;
    }
    public Response() {}

    public String getMessage() {
        return message;
    }
}
