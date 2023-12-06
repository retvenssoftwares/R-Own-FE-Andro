package app.retvens.rown.ApiRequest;

public class CallResponse{
    private String message;

    public CallResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}