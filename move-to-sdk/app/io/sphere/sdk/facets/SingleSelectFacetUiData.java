package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public final class SingleSelectFacetUiData<T> extends BaseSelectFacetUiData<T> {
    private final List<TermUiData<T>> termsUI;

    private SingleSelectFacetUiData(final String key, final String label, final TermFacetResult<T> facetResult, final List<T> selectedValues,
                                    @Nullable final Long termsThreshold, @Nullable final Long termsLimit, final List<TermUiData<T>> termsUI) {
        super(key, label, facetResult, selectedValues, termsThreshold, termsLimit);
        this.termsUI = termsUI;
    }

    @Override
    public List<TermUiData<T>> getAllTermsUI() {
        return termsUI;
    }

    public Optional<T> getSelectedValue() {
        return selectedValues.stream().findFirst();
    }

    public static <T> SingleSelectFacetUiData<T> of(final String key, final String label, final TermFacetResult<T> facetResult,
                                                @Nullable final T selectedValue, @Nullable final Long termsThreshold, @Nullable final Long termsLimit) {
        final List<T> selectedValues = Optional.ofNullable(selectedValue).map(Collections::singletonList).orElse(emptyList());
        final List<TermUiData<T>> termsUI = toTermsUI(facetResult, selectedValues);
        return new SingleSelectFacetUiData<>(key, label, facetResult, selectedValues, termsThreshold, termsLimit, termsUI);
    }

    public static <T> SingleSelectFacetUiData<T> of(final String key, final String label, final TermFacetResult<T> facetResult) {
        return of(key, label, facetResult, null, null, null);
    }
}
