package io.sphere.sdk.facets;

import io.sphere.sdk.search.FacetResult;

public interface FacetUI {

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
     * Gets the facet result concerning a particular attribute for this facet UI representation
     * @return the facet result for this facet UI
     */
    FacetResult getFacetResult();

    /**
     * Whether the facet can be displayed or not.
     * @return true if the facet can be displayed, false otherwise
     */
    boolean canBeDisplayed();
}
