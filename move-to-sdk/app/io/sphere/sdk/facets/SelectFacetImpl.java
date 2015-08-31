package io.sphere.sdk.facets;

import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;

public final class SelectFacetImpl<T> extends BaseSelectFacet<T> {

    SelectFacetImpl(final String key, final String label, final FacetType type, final UntypedSearchModel<T> searchModel,
                    final boolean multiSelect, final boolean matchingAll, final List<String> selectedValues,
                    @Nullable final TermFacetResult termFacetResult, @Nullable final Long threshold, @Nullable final Long limit) {
        super(key, label, type, searchModel, multiSelect, matchingAll, selectedValues, termFacetResult, threshold, limit);
    }

    @Override
    public List<FacetOption> getLimitedOptions() {
        return super.getLimitedOptions();
    }

    @Override
    public boolean isAvailable() {
        return super.isAvailable();
    }

    @Override
    public boolean isMultiSelect() {
        return super.isMultiSelect();
    }

    @Override
    public boolean isMatchingAll() {
        return super.isMatchingAll();
    }

    @Override
    public SelectFacet<T> withSelectedValues(final List<String> selectedValues) {
        return SelectFacetBuilder.of(this).selectedValues(selectedValues).build();
    }

    @Override
    public SelectFacet<T> withSearchResult(final PagedSearchResult<T> searchResult) {
        final TermFacetResult termFacetResult = searchResult.getTermFacetResult(searchModel.faceted().byAllTerms());
        return SelectFacetBuilder.of(this).facetResult(termFacetResult).build();
    }
}
