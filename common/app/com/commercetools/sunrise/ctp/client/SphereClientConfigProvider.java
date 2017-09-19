package com.commercetools.sunrise.ctp.client;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientConfigBuilder;
import play.Configuration;

import javax.inject.Inject;

public final class SphereClientConfigProvider implements Provider<SphereClientConfig> {

    private static final String CONFIG_ROOT = "sunrise.ctp.client";
    private final Configuration configuration;

    @Inject
    SphereClientConfigProvider(final Configuration globalConfig) {
        this.configuration = globalConfig.getConfig(CONFIG_ROOT);
    }

    @Override
    public SphereClientConfig get() {
        try {
            final String projectKey = configuration.getString("projectKey");
            final String clientId = configuration.getString("clientId");
            final String clientSecret = configuration.getString("clientSecret");
            return SphereClientConfigBuilder.ofKeyIdSecret(projectKey, clientId, clientSecret)
                    .authUrl(configuration.getString("authUrl"))
                    .apiUrl(configuration.getString("apiUrl"))
                    .scopeStrings(configuration.getStringList("scopes"))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new SunriseConfigurationException("Could not initialize SphereClientConfig", CONFIG_ROOT, e);
        }
    }
}