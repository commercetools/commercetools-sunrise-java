package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.emptyList;

public final class MultiSelectFacetUI<T> extends BaseSelectFacetUI<T> {
    private final List<TermUI<T>> termsUI;
    private final boolean matchesAll;

    private MultiSelectFacetUI(final String key, final String label, final TermFacetResult<T> facetResult, final List<T> selectedValues,
                               @Nullable final Long termsThreshold, @Nullable final Long termsLimit,
                               final List<TermUI<T>> termsUI, final boolean matchesAll) {
        super(key, label, facetResult, selectedValues, termsThreshold, termsLimit);
        this.termsUI = termsUI;
        this.matchesAll = matchesAll;
    }

    @Override
    public List<TermUI<T>> getAllTermsUI() {
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

    public static <T> MultiSelectFacetUI<T> of(final String key, final String label, final TermFacetResult<T> facetResult,
                                               final List<T> selectedValues, @Nullable final Long termsThreshold,
                                               @Nullable final Long termsLimit, final boolean matchesAll) {
        final List<TermUI<T>> termsUI = toTermsUI(facetResult, selectedValues);
        return new MultiSelectFacetUI<>(key, label, facetResult, selectedValues, termsThreshold, termsLimit, termsUI, matchesAll);
    }

    public static <T> MultiSelectFacetUI<T> of(final String key, final String label, final TermFacetResult<T> facetResult,
                                               final boolean matchesAll) {
        return of(key, label, facetResult, emptyList(),  null, null, matchesAll);
    }
}
