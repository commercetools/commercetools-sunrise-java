package com.commercetools.sunrise.ctp.products;

import com.google.inject.ImplementedBy;
import play.Configuration;

import java.util.ArrayList;
import java.util.List;

@ImplementedBy(ProductAttributesSettingsImpl.class)
public interface ProductAttributesSettings {

    /**
     * @return the list of the displayed attributes for the product
     */
    List<String> displayed();

    /**
     * Primary attributes are those attributes that affect the aspect of the displayed product,
     * e.g. the color is a typical secondary attribute for clothes on an online store.
     * @return the list of primary attributes that allow to change to a different product variant
     */
    List<String> primarySelectable();

    /**
     * Secondary attributes are those attributes that do not affect the aspect of the displayed product,
     * e.g. the size is a typical secondary attribute for clothes on an online store.
     * @return the list of secondary attributes that allow to change to a different product variant
     */
    List<String> secondarySelectable();

    /**
     * @return the list of all attributes that allow to change to a different product variant
     */
    default List<String> selectable() {
        final List<String> selectableAttributes = new ArrayList<>(primarySelectable());
        selectableAttributes.addAll(secondarySelectable());
        return selectableAttributes;
    }

    static ProductAttributesSettings of(final Configuration globalConfig, final String configPath) {
        return new ProductAttributesSettingsImpl(globalConfig, configPath);
    }
}
