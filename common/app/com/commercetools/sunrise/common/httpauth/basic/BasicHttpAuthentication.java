package com.commercetools.sunrise.common.httpauth.basic;

import com.commercetools.sunrise.common.httpauth.HttpAuthentication;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.Base64;

/**
 * Enables HTTP Basic Access Authentication.
 */
final class BasicHttpAuthentication extends Base implements HttpAuthentication {

    @Nullable
    private final String realm;
    @Nullable
    private final String encodedCredentials;

    BasicHttpAuthentication() {
        this.realm = null;
        this.encodedCredentials = null;
    }

    BasicHttpAuthentication(final String realm, final String credentials) {
        this.realm = realm;
        this.encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    @Override
    public boolean isEnabled() {
        return encodedCredentials != null;
    }

    @Override
    public String getWwwAuthenticateHeader() {
        return "Basic realm=\"" + realm + "\"";
    }

    /**
     * Decides whether the HTTP Authorization complies with a Basic HTTP Authorization Header and contains the correct
     * credentials, i.e. it is equal to "Basic username:password", where "username:password" is encoded in Base64 scheme.
     * @param rawAuthorizationHttpHeader the contents of the HTTP Authorization header
     * @return true if the HTTP Authorization Header is valid and contains the correct credentials, false otherwise
     */
    @Override
    public boolean isAuthorized(final String rawAuthorizationHttpHeader) {
        if (isEnabled()) {
            final String expectedAuthorizationHttpHeader = "Basic " + encodedCredentials;
            return expectedAuthorizationHttpHeader.equals(rawAuthorizationHttpHeader);
        } else {
            return true;
        }
    }
}
