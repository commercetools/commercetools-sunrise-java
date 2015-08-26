package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;

public final class SingleSelectFacet<T> extends BaseSelectFacet<T> {

    SingleSelectFacet(final String key, final String label, final UntypedSearchModel<T> searchModel, final SelectFacetType type,
                      final List<String> selectedValues, @Nullable final TermFacetResult termFacetResult,
                      @Nullable final Long threshold, @Nullable final Long limit) {
        super(key, label, searchModel, type, false, selectedValues, termFacetResult, threshold, limit);
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
    public SingleSelectFacet<T> withSelectedValues(final List<String> selectedValues) {
        return SingleSelectFacetBuilder.of(this).selectedValues(selectedValues).build();
    }

    @Override
    public SingleSelectFacet<T> withTermFacetResult(final TermFacetResult termFacetResult) {
        return SingleSelectFacetBuilder.of(this).termFacetResult(termFacetResult).build();
    }
}
