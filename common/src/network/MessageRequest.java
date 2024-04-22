package network;

import enums.RequestTypes;

/**
 * Запрос с сообщением, никак не обрабатывает ответ, но требует его, чтобы понять.
 * что сообщение получено клиентом/сервером и можно продолжить работу
 */
public class MessageRequest extends Request {

    private String message;
    public MessageRequest(String message) {
        super(RequestTypes.MESSAGE);
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageRequest{" +
                "message='" + message + '\'' +
                '}';
    }
}
