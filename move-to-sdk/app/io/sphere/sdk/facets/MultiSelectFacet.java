package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;

public final class MultiSelectFacet<T> extends BaseSelectFacet<T> {

    MultiSelectFacet(final String key, final String label, final UntypedSearchModel<T> searchModel, final SelectFacetType type,
                     final boolean matchingAll, final List<String> selectedValues, @Nullable final TermFacetResult termFacetResult,
                     @Nullable final Long threshold, @Nullable final Long limit) {
        super(key, label, searchModel, type, matchingAll, selectedValues, termFacetResult, threshold, limit);
    }

    @Override
    public List<FacetOption> getLimitedOptions() {
        return super.getLimitedOptions();
    }

    @Override
    public boolean canBeDisplayed() {
        return super.canBeDisplayed();
    }

    @Override
    public boolean isMatchingAll() {
        return super.isMatchingAll();
    }

    @Override
    public MultiSelectFacet<T> withSelectedValues(final List<String> selectedValues) {
        return MultiSelectFacetBuilder.of(this).selectedValues(selectedValues).build();
    }

    @Override
    public MultiSelectFacet<T> withTermFacetResult(final TermFacetResult termFacetResult) {
        return MultiSelectFacetBuilder.of(this).termFacetResult(termFacetResult).build();
    }
}
