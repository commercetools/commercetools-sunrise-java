package common.templates;

public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
