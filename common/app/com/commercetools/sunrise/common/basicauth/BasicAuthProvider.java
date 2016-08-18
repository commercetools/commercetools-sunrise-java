package com.commercetools.sunrise.common.basicauth;

import com.commercetools.sunrise.common.SunriseConfigurationException;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;

public final class BasicAuthProvider implements Provider<BasicAuth> {

    private static final Logger logger = LoggerFactory.getLogger(BasicAuthProvider.class);
    public static final String CONFIG_REALM = "application.auth.realm";
    public static final String CONFIG_CREDENTIALS = "application.auth.credentials";
    public static final String REGEX_CREDENTIALS = "^[^ :]+:[^ :]+$";
    @Inject
    private Configuration configuration;

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
                throw new SunriseConfigurationException("Basic access authentication credentials must be of the form 'username:password', matching: " + REGEX_CREDENTIALS, CONFIG_CREDENTIALS);
            }
        } else {
            logger.info("Basic authentication: disabled");
            return null;
        }
    }
}
