package io.sphere.sdk.facets;

public interface FacetUiData {

    /**
     * Gets the string identifying this facet UI representation.
     * @return the key that identifies this facet UI data
     */
    String getKey();

    /**
     * Gets the label displayed in the facet UI representation.
     * @return the label displayed in this facet UI data
     */
    String getLabel();

    /**
     * Whether the facet can be displayed or not.
     * @return true if the facet can be displayed, false otherwise
     */
    boolean canBeDisplayed();
}
