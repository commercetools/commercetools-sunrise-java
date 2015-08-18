package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public final class SingleSelectFacetUI<T> extends BaseSelectFacetUI<T> {
    private final List<TermUI<T>> termsUI;

    private SingleSelectFacetUI(final String key, final String label, final TermFacetResult<T> facetResult, final List<T> selectedValues,
                                @Nullable final Long termsThreshold, @Nullable final Long termsLimit, final List<TermUI<T>> termsUI) {
        super(key, label, facetResult, selectedValues, termsThreshold, termsLimit);
        this.termsUI = termsUI;
    }

    @Override
    public List<TermUI<T>> getAllTermsUI() {
        return termsUI;
    }

    public Optional<T> getSelectedValue() {
        return selectedValues.stream().findFirst();
    }

    public static <T> SingleSelectFacetUI<T> of(final String key, final String label, final TermFacetResult<T> facetResult,
                                                @Nullable final T selectedValue, @Nullable final Long termsThreshold, @Nullable final Long termsLimit) {
        final List<T> selectedValues = Optional.ofNullable(selectedValue).map(Collections::singletonList).orElse(emptyList());
        final List<TermUI<T>> termsUI = toTermsUI(facetResult, selectedValues);
        return new SingleSelectFacetUI<>(key, label, facetResult, selectedValues, termsThreshold, termsLimit, termsUI);
    }

    public static <T> SingleSelectFacetUI<T> of(final String key, final String label, final TermFacetResult<T> facetResult) {
        return of(key, label, facetResult, null, null, null);
    }
}
