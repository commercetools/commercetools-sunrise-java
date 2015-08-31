package io.sphere.sdk.facets;

import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;

public final class FlexibleSelectFacet<T> extends BaseSelectFacet<T> {
    private final FacetOptionMapper mapper;

    FlexibleSelectFacet(final String key, final String label, final FacetType type, final UntypedSearchModel<T> searchModel,
                        final boolean multiSelect, final boolean matchingAll, final List<String> selectedValues,
                        @Nullable final TermFacetResult termFacetResult, @Nullable final Long threshold, @Nullable final Long limit,
                        final FacetOptionMapper mapper) {
        super(key, label, type, searchModel, multiSelect, matchingAll, selectedValues, termFacetResult, threshold, limit);
        this.mapper = mapper;
    }

    @Override
    protected List<FacetOption> getOptions() {
        return mapper.map(super.getOptions());
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

    /**
     * Gets the mapper for this facet.
     * @return the facet option mapper
     */
    public FacetOptionMapper getMapper() {
        return mapper;
    }

    @Override
    public FlexibleSelectFacet<T> withSelectedValues(final List<String> selectedValues) {
        return FlexibleSelectFacetBuilder.of(this).selectedValues(selectedValues).build();
    }

    @Override
    public FlexibleSelectFacet<T> withSearchResult(final PagedSearchResult<T> searchResult) {
        final TermFacetResult termFacetResult = searchResult.getTermFacetResult(searchModel.faceted().byAllTerms());
        return FlexibleSelectFacetBuilder.of(this).facetResult(termFacetResult).build();
    }
}
