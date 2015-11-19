package purchase;

public class ErrorBean {
    private String message;

    public ErrorBean() {
    }

    public ErrorBean(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
