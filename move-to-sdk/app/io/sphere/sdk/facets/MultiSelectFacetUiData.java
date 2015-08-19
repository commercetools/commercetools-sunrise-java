package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.emptyList;

public final class MultiSelectFacetUiData<T> extends BaseSelectFacetUiData<T> {
    private final List<TermUiData<T>> termsUI;
    private final boolean matchesAll;

    private MultiSelectFacetUiData(final String key, final String label, final TermFacetResult<T> facetResult, final List<T> selectedValues,
                                   @Nullable final Long termsThreshold, @Nullable final Long termsLimit,
                                   final List<TermUiData<T>> termsUI, final boolean matchesAll) {
        super(key, label, facetResult, selectedValues, termsThreshold, termsLimit);
        this.termsUI = termsUI;
        this.matchesAll = matchesAll;
    }

    @Override
    public List<TermUiData<T>> getAllTermsUI() {
        return termsUI;
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
                                               final List<T> selectedValues, @Nullable final Long termsThreshold,
                                               @Nullable final Long termsLimit, final boolean matchesAll) {
        final List<TermUiData<T>> termsUI = toTermsUI(facetResult, selectedValues);
        return new MultiSelectFacetUiData<>(key, label, facetResult, selectedValues, termsThreshold, termsLimit, termsUI, matchesAll);
    }

    public static <T> MultiSelectFacetUiData<T> of(final String key, final String label, final TermFacetResult<T> facetResult,
                                               final boolean matchesAll) {
        return of(key, label, facetResult, emptyList(),  null, null, matchesAll);
    }
}
