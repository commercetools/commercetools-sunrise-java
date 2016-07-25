package com.commercetools.sunrise.common.template.cms;

/**
 * An unchecked exception signalling that there was a problem when obtaining the content from the CMS.
 * It could be caused by wrong call parameters (eg. wrong credentials) or server side issues.
 * <p>
 * Exceptions of this type will often wrap a lower-level exception.
 *
 * @see Exception#getCause()
 */
public class CmsServiceException extends RuntimeException {

    public CmsServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CmsServiceException(final Throwable cause) {
        super(cause);
    }
}
