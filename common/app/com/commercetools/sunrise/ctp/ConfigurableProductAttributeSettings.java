package com.commercetools.sunrise.ctp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static java.util.Collections.emptyList;

@Singleton
final class ConfigurableProductAttributeSettings implements ProductAttributeSettings {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductAttributeSettings.class);

    private static final String CONFIG_DISPLAYED_ATTRS = "productAttributes.displayedAttributes";
    private static final String CONFIG_PRIMARY_SELECTABLE_ATTRS = "productAttributes.primarySelectableAttributes";
    private static final String CONFIG_SECONDARY_SELECTABLE_ATTRS = "productAttributes.secondarySelectableAttributes";

    private final List<String> displayedAttributes;
    private final List<String> primarySelectableAttributes;
    private final List<String> secondarySelectableAttributes;

    @Inject
    ConfigurableProductAttributeSettings(final Configuration configuration) {
        this.displayedAttributes = configuration.getStringList(CONFIG_DISPLAYED_ATTRS, emptyList());
        this.primarySelectableAttributes = configuration.getStringList(CONFIG_PRIMARY_SELECTABLE_ATTRS, emptyList());
        this.secondarySelectableAttributes = configuration.getStringList(CONFIG_SECONDARY_SELECTABLE_ATTRS, emptyList());
        LOGGER.debug("Initialize ProductAttributeSettings: displayed attributes {}, primary selectable attributes {}, secondary selectable attributes {}",
                displayedAttributes,
                primarySelectableAttributes,
                secondarySelectableAttributes);
    }

    @Override
    public List<String> getDisplayedAttributes() {
        return displayedAttributes;
    }

    @Override
    public List<String> getSelectablePrimaryAttributes() {
        return primarySelectableAttributes;
    }

    @Override
    public List<String> getSelectableSecondaryAttributes() {
        return secondarySelectableAttributes;
    }
}

