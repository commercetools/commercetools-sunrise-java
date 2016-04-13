package common.template.cms;

public class CmsPageNotFoundException extends RuntimeException {

    public CmsPageNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
