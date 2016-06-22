package com.commercetools.sunrise.common.basicauth;

import com.commercetools.sunrise.common.SunriseInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

public final class BasicAuthProvider implements Provider<BasicAuth> {
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthProvider.class);

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
                logger.info("Basic authentication: enabled for realm \"{}\"", realm);
                return BasicAuth.of(realm, credentials);
            } else {
                throw new SunriseInitializationException("Basic access authentication credentials must be of the form 'username:password', matching: " + REGEX_CREDENTIALS);
            }
        } else {
            logger.info("Basic authentication: disabled");
            return null;
        }
    }
}
