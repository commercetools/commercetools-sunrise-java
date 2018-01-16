package com.commercetools.sunrise.models.attributes;

import com.google.inject.ImplementedBy;
import play.Configuration;

import java.util.List;
import java.util.Optional;

@ImplementedBy(AttributeSettingsImpl.class)
public interface AttributeSettings {

    /**
     * @return the list of the displayed attributes for the product
     */
    List<String> displayed();

    /**
     * @return the list of selectable attributes that allow to change to a different product variant
     */
    List<String> selectable();

    /**
     * Secondary attribute is an attribute that does not affect the aspect or price of the displayed product,
     * hence it does not require reloading the page with the new variant.
     *
     * For example the size is a typical secondary attribute for clothes on an online store.
     *
     * @return the secondary attribute if any, empty otherwise
     */
    Optional<String> secondary();

    default boolean isSecondary(final String attributeName) {
        return secondary().map(secondary -> secondary.equals(attributeName)).orElse(false);
    }

    static AttributeSettings of(final Configuration globalConfig, final String configPath) {
        return new AttributeSettingsImpl(globalConfig, configPath);
    }
}
