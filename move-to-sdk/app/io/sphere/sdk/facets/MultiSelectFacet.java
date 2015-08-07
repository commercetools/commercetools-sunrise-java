package io.sphere.sdk.facets;

public interface MultiSelectFacet {

    /**
     * Defines whether the results should match all selected values in the facet (AND operator effect)
     * or just at least one selected value (OR operator effect)
     * @return true if results should match all selected values, false otherwise
     */
    boolean matchesAll();
}
