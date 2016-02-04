package basicauth;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicAuthTest {
    private static final String MY_REALM = "My Realm";
    private static final String CREDENTIALS = "username:password";
    private static final BasicAuth BASIC_AUTH = BasicAuth.of(MY_REALM, CREDENTIALS);
    private static final String ENCODED_CREDENTIALS = "dXNlcm5hbWU6cGFzc3dvcmQ=";

    @Test
    public void isAuthorizedWithCorrectCredentials() throws Exception {
        assertThat(BASIC_AUTH.isAuthorized("Basic " + ENCODED_CREDENTIALS)).isTrue();
    }

    @Test
    public void isNotAuthorizedWithDecodedCredentials() throws Exception {
        assertThat(BASIC_AUTH.isAuthorized("Basic " + CREDENTIALS)).isFalse();
    }

    @Test
    public void isNotAuthorizedWithInvalidCredentials() throws Exception {
        assertThat(BASIC_AUTH.isAuthorized("Basic wrong:password")).isFalse();
    }

    @Test
    public void isNotAuthorizedWithoutBasicHeader() throws Exception {
        assertThat(BASIC_AUTH.isAuthorized(ENCODED_CREDENTIALS)).isFalse();
    }
}
