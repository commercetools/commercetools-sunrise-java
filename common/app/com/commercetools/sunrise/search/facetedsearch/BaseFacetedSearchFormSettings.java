package com.commercetools.sunrise.search.facetedsearch;

import javax.annotation.Nullable;

interface BaseFacetedSearchFormSettings {

    /**
     * Gets the label displayed in the facet.
     * @return the label displayed in this facet
     */
    String getFieldLabel();

    /**
     * Gets the facet expression associated, representing just the attribute path.
     * The expression might contain {@code {{locale}}}, which should be replaced with the current locale before using it.
     * @return the facet expression
     */
    String getAttributePath();

    /**
     * Whether the facet count should be hidden or not.
     * @return true if the count should be hidden, false otherwise
     */
    boolean isCountDisplayed();

    /**
     * Gets the UI type of this facet.
     * @return the UI type of this facet
     */
    @Nullable
    String getUIType();
}
