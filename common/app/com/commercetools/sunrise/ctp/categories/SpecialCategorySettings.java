package com.commercetools.sunrise.ctp.categories;

import play.Configuration;

import java.util.List;

public interface SpecialCategorySettings {

    /**
     * @return the external ID of this category
     */
    String externalId();

    /**
     * @return the list of product filter expressions to be used to fetch the related products for this category
     */
    List<String> productFilterExpressions();

    /**
     * @return whether this category is a sales category
     */
    boolean isSale();

    static SpecialCategorySettings of(final Configuration configuration) {
        return new SpecialCategorySettingsImpl(configuration);
    }
}