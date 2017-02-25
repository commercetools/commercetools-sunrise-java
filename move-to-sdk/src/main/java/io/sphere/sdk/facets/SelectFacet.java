package io.sphere.sdk.facets;

import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Facet that contains a list of selectable options.
 * @param <T> type of the resource for this facet (e.g. ProductProjection)
 */
public interface SelectFacet<T> extends Facet<T> {

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
    @Nullable
    Long getThreshold();

    /**
     * Gets the limit for the maximum amount of options allowed to be displayed in the facet.
     * @return the limit for the amount of options that can be displayed, or absent if it has no limit
     */
    @Nullable
    Long getLimit();

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
     * Gets the associated term facet result for this select facet.
     * @return the term facet result, or absent if there is no associated facet result
     */
    @Nullable
    TermFacetResult getFacetResult();

    /**
     * Gets the mapper for this facet.
     * @return the facet option mapper, or absent if there is no mapper
     */
    @Nullable
    FacetOptionMapper getMapper();

    SelectFacet<T> withSearchResult(final PagedSearchResult<T> searchResult);

}
