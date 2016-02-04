package basicauth;

import io.sphere.sdk.models.Base;

import java.util.Base64;

/**
 * Contains information related to the HTTP basic access authentication.
 */
public class BasicAuth extends Base {
    private final String realm;
    private final String encodedCredentials;

    private BasicAuth(final String realm, final String encodedCredentials) {
        this.realm = realm;
        this.encodedCredentials = encodedCredentials;
    }

    public String getRealm() {
        return realm;
    }

    /**
     * Decides whether the authentication header is valid, i.e. it is equal to "Basic username:password",
     * where "username:password" is encoded in Base64 scheme.
     * @param authorizationHeader the contents of the HTTP Authorization header
     * @return true if the header complies with an Authorization header and contains the correct credentials,
     * false otherwise
     */
    public boolean isAuthorized(final String authorizationHeader) {
        final String expectedAuthHeader = "Basic " + encodedCredentials;
        return expectedAuthHeader.equals(authorizationHeader);
    }

    public static BasicAuth of(final String realm, final String credentials) {
        final String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return new BasicAuth(realm, encodedCredentials);
    }
}
