package io.sphere.sdk.facets;

public interface Facet {

    /**
     * Gets the string identifying this facet representation.
     * @return the key that identifies this facet
     */
    String getKey();

    /**
     * Gets the label displayed in the facet representation.
     * @return the label displayed in this facet
     */
    String getLabel();

    /**
     * Whether the facet can be displayed or not.
     * @return true if the facet can be displayed, false otherwise
     */
    boolean canBeDisplayed();
}
