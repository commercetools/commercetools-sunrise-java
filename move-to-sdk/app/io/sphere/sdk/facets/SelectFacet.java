package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import java.util.List;
import java.util.Optional;

public interface SelectFacet<T> extends Facet<T> {

    List<String> getSelectedValues();

    Optional<TermFacetResult> getTermFacetResult();

    /**
     * Gets the complete options without limitations.
     * @return the complete possible options
     */
    List<FacetOption> getAllOptions();

    /**
     * Obtains the truncated options list according to the defined limit.
     * @return the truncated options if the limit is defined, the whole list otherwise
     */
    List<FacetOption> getLimitedOptions();

    /**
     * Gets the threshold indicating the minimum amount of options allowed to be displayed in the facet.
     * @return the threshold for the amount of options that can be displayed, or absent if it has no threshold
     */
    Optional<Long> getThreshold();

    /**
     * Gets the limit for the maximum amount of options allowed to be displayed in the facet.
     * @return the limit for the amount of options that can be displayed, or absent if it has no limit
     */
    Optional<Long> getLimit();

    /**
     * Defines whether multiple options can be selected or only one.
     * @return true if multiple values can be selected, false otherwise
     */
    boolean isMultiSelect();

    /**
     * Defines whether the results should match all selected values in the facet (AND operator effect)
     * or just at least one selected value (OR operator effect)
     * @return true if results should match all selected values, false otherwise
     */
    boolean isMatchingAll();

    /**
     * Gets a new instance of SelectFacet with the same attributes as this, but with the given list of selected values.
     * @param selectedValues the new list of selected values
     * @return a new instance with same values of this but, with the given list of selected values
     */
    SelectFacet<T> withSelectedValues(final List<String> selectedValues);

    /**
     * Gets a new instance of SelectFacet with the same attributes as this, but with the given term facet result.
     * @param termFacetResult the new term facet result
     * @return a new instance with same values of this but, with the given term facet result
     */
    SelectFacet<T> withTermFacetResult(final TermFacetResult termFacetResult);

}
