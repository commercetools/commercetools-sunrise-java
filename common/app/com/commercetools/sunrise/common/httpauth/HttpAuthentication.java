package com.commercetools.sunrise.common.httpauth;

public interface HttpAuthentication {

    /**
     * Whether the HTTP authentication is enabled for the project.
     * @return true if HTTP authentication is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Content of the WWW-Authenticate HTTP Header for a no authenticated request.
     * @return the content of the WWW-Authenticate HTTP Header
     */
    String getWwwAuthenticateHeaderValue();

    /**
     * Decides whether the Authorization HTTP Header has valid authorized information, thus the request can be granted access.
     * If authentication is disabled, it should always return true.
     * @param rawAuthorizationHttpHeader the contents of the Authorization HTTP Header as it is received from the request
     * @return true if the header contains valid authorized information, false otherwise
     */
    boolean isAuthorized(final String rawAuthorizationHttpHeader);
}
