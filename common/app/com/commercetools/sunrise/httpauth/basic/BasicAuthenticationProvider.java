package com.commercetools.sunrise.httpauth.basic;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.httpauth.HttpAuthentication;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;

public final class BasicAuthenticationProvider implements Provider<HttpAuthentication> {

    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationProvider.class);
    private static final String CONFIG_REALM = "httpAuth.basic.realm";
    private static final String CONFIG_CREDENTIALS = "httpAuth.basic.credentials";
    private static final String REGEX_CREDENTIALS = "^[^ :]+:[^ :]+$";

    @Nullable
    private final String credentials;
    private final String realm;

    @Inject
    public BasicAuthenticationProvider(final Configuration configuration) {
        final String credentials = configuration.getString(CONFIG_CREDENTIALS, "");
        this.credentials = credentials.isEmpty() ? null : credentials;
        this.realm = configuration.getString(CONFIG_REALM, "Sunrise Authentication");
    }

    @Override
    public HttpAuthentication get() {
        if (credentials != null) {
            return enabledBasicHttpAuth(credentials);
        } else {
            return disabledBasicHttpAuth();
        }
    }

    private BasicHttpAuthentication enabledBasicHttpAuth(final String credentials) {
        if (credentials.matches(REGEX_CREDENTIALS)) {
            logger.debug("Basic authentication enabled for realm \"{}\"", realm);
            return new BasicHttpAuthentication(realm, credentials);
        } else {
            throw new SunriseConfigurationException("Basic access authentication credentials must be of the form"
                    + " 'username:password', matching: " + REGEX_CREDENTIALS, CONFIG_CREDENTIALS);
        }
    }

    private BasicHttpAuthentication disabledBasicHttpAuth() {
        logger.info("Basic authentication disabled");
        return new BasicHttpAuthentication();
    }
}
