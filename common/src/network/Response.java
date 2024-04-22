package network;

import enums.RequestTypes;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = -1202337532505101798L;
    private String message;
    private Request responseRequest;
    // вместо isFinal проверка на null реквеста
    private boolean isFinal;

    public Response(Request responseRequest) {
        this.responseRequest = responseRequest;
        if (responseRequest.getType() == RequestTypes.BUILD) {
            this.message = ((BuildRequest) responseRequest).getMessage();
        }
    }
    public Response(String message) {
        this.message = message;
    }
    public Response(String message, boolean isFinal) {
        this.message = message;
        this.isFinal = isFinal;
    }
    public Response() {
        this.isFinal = true;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasResponseRequest() {
        return (responseRequest != null);
    }

    public Request getResponseRequest() {
        return responseRequest;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }
}
