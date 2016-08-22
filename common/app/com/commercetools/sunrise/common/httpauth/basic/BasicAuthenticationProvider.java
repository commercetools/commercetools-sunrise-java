package com.commercetools.sunrise.common.httpauth.basic;

import com.commercetools.sunrise.common.SunriseConfigurationException;
import com.commercetools.sunrise.common.httpauth.HttpAuthentication;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.Optional;

public final class BasicAuthenticationProvider implements Provider<HttpAuthentication> {

    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationProvider.class);
    private static final String CONFIG_REALM = "httpAuth.basic.realm";
    private static final String CONFIG_CREDENTIALS = "httpAuth.basic.credentials";
    private static final String REGEX_CREDENTIALS = "^[^ :]+:[^ :]+$";
    @Inject
    private Configuration configuration;

    @Override
    public HttpAuthentication get() {
        return findCredentials()
                .map(this::enabledBasicHttpAuth)
                .orElseGet(this::disabledBasicHttpAuth);
    }

    private BasicHttpAuthentication enabledBasicHttpAuth(final String credentials) {
        if (credentials.matches(REGEX_CREDENTIALS)) {
            final String realm = realm();
            logger.info("Basic authentication: enabled for realm \"{}\"", realm);
            return new BasicHttpAuthentication(realm, credentials);
        } else {
            throw new SunriseConfigurationException("Basic access authentication credentials must be of the form"
                    + " 'username:password', matching: " + REGEX_CREDENTIALS, CONFIG_CREDENTIALS);
        }
    }

    private BasicHttpAuthentication disabledBasicHttpAuth() {
        logger.info("Basic authentication: disabled");
        return new BasicHttpAuthentication();
    }

    private Optional<String> findCredentials() {
        return Optional.ofNullable(configuration.getString(CONFIG_CREDENTIALS))
                .filter(credentials -> credentials != null && !credentials.isEmpty());
    }

    private String realm() {
        return configuration.getString(CONFIG_REALM, "Sunrise Authentication");
    }
}
