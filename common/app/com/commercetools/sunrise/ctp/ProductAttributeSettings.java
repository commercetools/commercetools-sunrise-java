package com.commercetools.sunrise.ctp;

import com.google.inject.ImplementedBy;

import java.util.ArrayList;
import java.util.List;

@ImplementedBy(ConfigurableProductAttributeSettings.class)
public interface ProductAttributeSettings {

    /**
     * List of the displayed attributes for the product.
     * @return the list of displayed attributes
     */
    List<String> getDisplayedAttributes();

    /**
     * List of primary attributes that allow to change to a different product variant.
     * Primary attributes are those attributes that affect the aspect of the displayed product,
     * e.g. the color is a typical secondary attribute for clothes on an online store.
     * @return the list of selectable primary attributes
     */
    List<String> getSelectablePrimaryAttributes();

    /**
     * List of secondary attributes that allow to change to a different product variant.
     * Secondary attributes are those attributes that do not affect the aspect of the displayed product,
     * e.g. the size is a typical secondary attribute for clothes on an online store.
     * @return the list of selectable secondary attributes
     */
    List<String> getSelectableSecondaryAttributes();

    /**
     * List of all attributes that allow to change to a different product variant.
     * @return the list of selectable attributes
     */
    default List<String> getSelectableAttributes() {
        final List<String> selectableAttributes = new ArrayList<>(getSelectablePrimaryAttributes());
        selectableAttributes.addAll(getSelectableSecondaryAttributes());
        return selectableAttributes;
    }
}

