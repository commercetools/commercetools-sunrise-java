package io.sphere.sdk.facets;

import io.sphere.sdk.search.FacetExpression;

public interface FacetUI<T> {

    /**
     * Gets the string identifying this facet UI representation.
     * @return the key that identifies this facet UI
     */
    String getKey();

    /**
     * Gets the label displayed in the facet UI representation.
     * @return the label displayed in this facet UI
     */
    String getLabel();

    /**
     * Gets the expression that generates the facet data.
     * @return the facet expression for this UI representation
     */
    FacetExpression<T> getExpression();

    /**
     * Whether the facet can be displayed or not.
     * @return true if the facet can be displayed, false otherwise
     */
    boolean canBeDisplayed();
}
