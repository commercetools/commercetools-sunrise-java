package basicauth;

import inject.SunriseInitializationException;
import play.Configuration;
import play.Logger;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

class BasicAuthProvider implements Provider<BasicAuth> {
    public static final String CONFIG_REALM = "application.auth.realm";
    public static final String CONFIG_CREDENTIALS = "application.auth.credentials";
    public static final String REGEX_CREDENTIALS = "^[^ :]+:[^ :]+$";
    private final Configuration configuration;

    @Inject
    public BasicAuthProvider(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Nullable
    @Override
    public BasicAuth get() {
        final String realm = configuration.getString(CONFIG_REALM, "Sunrise Authentication");
        final String credentials = configuration.getString(CONFIG_CREDENTIALS);
        if (credentials != null && !credentials.isEmpty()) {
            if (credentials.matches(REGEX_CREDENTIALS)) {
                Logger.debug("Basic authentication: enabled for realm \"{}\"", realm);
                return BasicAuth.of(realm, credentials);
            } else {
                throw new SunriseInitializationException("Basic access authentication credentials must be of the form 'username:password', matching: " + REGEX_CREDENTIALS);
            }
        } else {
            Logger.debug("Basic authentication: disabled");
            return null;
        }
    }
}
