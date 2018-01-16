package com.commercetools.sunrise.models.attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
final class AttributeSettingsImpl implements AttributeSettings {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeSettings.class);

    private final List<String> displayed;
    private final List<String> selectable;
    @Nullable
    private final String secondary;

    @Inject
    AttributeSettingsImpl(final Configuration globalConfig) {
        this(globalConfig, "sunrise.attributes");
    }

    AttributeSettingsImpl(final Configuration globalConfig, final String configPath) {
        final Configuration config = globalConfig.getConfig(configPath);
        this.displayed = config.getStringList("displayed");
        this.selectable = config.getStringList("selectable");
        this.secondary = config.getString("secondary");
        LOGGER.debug("Initialized AttributeSettings: displayed attributes {}, selectable attributes {}, secondary attribute {}",
                displayed,
                selectable,
                secondary);
    }

    @Override
    public List<String> displayed() {
        return displayed;
    }

    @Override
    public List<String> selectable() {
        return selectable;
    }

    @Override
    public Optional<String> secondary() {
        return Optional.ofNullable(secondary);
    }
}
