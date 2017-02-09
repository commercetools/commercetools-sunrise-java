package com.commercetools.sunrise.common.ctp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Singleton
public final class AttributeSettings {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeSettings.class);

    private static final String CONFIG_DISPLAYED_ATTRS = "productAttributes.displayedAttributes";
    private static final String CONFIG_SOFT_SELECTABLE_ATTRS = "productAttributes.softSelectableAttributes";
    private static final String CONFIG_HARD_SELECTABLE_ATTRS = "productAttributes.hardSelectableAttributes";

    private final List<String> displayedAttributes;
    private final List<String> softSelectableAttributes;
    private final List<String> hardSelectableAttributes;

    @Inject
    public AttributeSettings(final Configuration configuration) {
        this.displayedAttributes = configuration.getStringList(CONFIG_DISPLAYED_ATTRS, emptyList());
        this.softSelectableAttributes = configuration.getStringList(CONFIG_SOFT_SELECTABLE_ATTRS, emptyList());
        this.hardSelectableAttributes = configuration.getStringList(CONFIG_HARD_SELECTABLE_ATTRS, emptyList());
        LOGGER.debug("Provide AttributeSettings: displayed attributes {}, soft selectable attributes {}, hard selectable attributes {}",
                displayedAttributes, softSelectableAttributes, hardSelectableAttributes);
    }

    public List<String> getDisplayedAttributes() {
        return displayedAttributes;
    }

    public List<String> getSoftSelectableAttributes() {
        return softSelectableAttributes;
    }

    public List<String> getHardSelectableAttributes() {
        return hardSelectableAttributes;
    }

    public List<String> getSelectableAttributes() {
        final List<String> selectableAttributes = new ArrayList<>(hardSelectableAttributes);
        selectableAttributes.addAll(softSelectableAttributes);
        return selectableAttributes;
    }
}

