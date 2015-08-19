package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.emptyList;

public final class MultiSelectFacetUiData<T> extends BaseSelectFacetUiData<T> {
    private final List<TermUiData<T>> termsUiData;
    private final boolean matchesAll;

    private MultiSelectFacetUiData(final String key, final String label, final TermFacetResult<T> facetResult, final List<T> selectedValues,
                                   @Nullable final Long threshold, @Nullable final Long limit,
                                   final List<TermUiData<T>> termsUiData, final boolean matchesAll) {
        super(key, label, facetResult, selectedValues, threshold, limit);
        this.termsUiData = termsUiData;
        this.matchesAll = matchesAll;
    }

    @Override
    public List<TermUiData<T>> getAllTermsUiData() {
        return termsUiData;
    }

    public List<T> getSelectedValues() {
        return selectedValues;
    }

    /**
     * Defines whether the results should match all selected values in the facet (AND operator effect)
     * or just at least one selected value (OR operator effect)
     * @return true if results should match all selected values, false otherwise
     */
    public boolean matchesAll() {
        return matchesAll;
    }

    public static <T> MultiSelectFacetUiData<T> of(final String key, final String label, final TermFacetResult<T> facetResult,
                                                   final List<T> selectedValues, @Nullable final Long threshold,
                                                   @Nullable final Long limit, final boolean matchesAll) {
        final List<TermUiData<T>> termsUiData = toTermsUiData(facetResult, selectedValues);
        return new MultiSelectFacetUiData<>(key, label, facetResult, selectedValues, threshold, limit, termsUiData, matchesAll);
    }

    public static <T> MultiSelectFacetUiData<T> of(final String key, final String label, final TermFacetResult<T> facetResult,
                                                   final boolean matchesAll) {
        return of(key, label, facetResult, emptyList(),  null, null, matchesAll);
    }
}
