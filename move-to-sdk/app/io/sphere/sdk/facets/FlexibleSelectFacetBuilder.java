package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermModel;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;

public final class FlexibleSelectFacetBuilder<T> extends BaseSelectFacetBuilder<FlexibleSelectFacetBuilder<T>> implements Builder<FlexibleSelectFacet<T>> {
    private final UntypedSearchModel<T> searchModel;
    private FacetOptionMapper mapper;

    private FlexibleSelectFacetBuilder(final String key, final String label, final FacetType type,
                                       final UntypedSearchModel<T> searchModel, final FacetOptionMapper mapper) {
        super(key, label, type);
        this.searchModel = searchModel;
        this.mapper = mapper;
    }

    @Override
    public FlexibleSelectFacet<T> build() {
        return new FlexibleSelectFacet<>(getKey(), getLabel(), getType(), searchModel, multiSelect, matchingAll, selectedValues,
                facetResult.orElse(null), threshold.orElse(null), limit.orElse(null), mapper);
    }

    @Override
    public FlexibleSelectFacetBuilder<T> multiSelect(final boolean multiSelect) {
        return super.multiSelect(multiSelect);
    }

    @Override
    public FlexibleSelectFacetBuilder<T> matchingAll(final boolean matchingAll) {
        return super.matchingAll(matchingAll);
    }

    @Override
    public FlexibleSelectFacetBuilder<T> selectedValues(final List<String> selectedValues) {
        return super.selectedValues(selectedValues);
    }

    @Override
    public FlexibleSelectFacetBuilder<T> facetResult(@Nullable final TermFacetResult termFacetResult) {
        return super.facetResult(termFacetResult);
    }

    @Override
    public FlexibleSelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        return super.threshold(threshold);
    }

    @Override
    public FlexibleSelectFacetBuilder<T> limit(@Nullable final Long limit) {
        return super.limit(limit);
    }

    public FacetOptionMapper getMapper() {
        return mapper;
    }

    public static <T> FlexibleSelectFacetBuilder<T> of(final String key, final String label, final FacetType type,
                                                     final TermModel<T, ?> searchModel, final FacetOptionMapper mapper) {
        return new FlexibleSelectFacetBuilder<>(key, label, type, searchModel.untyped(), mapper);
    }

    public static <T> FlexibleSelectFacetBuilder<T> of(final FlexibleSelectFacet<T> facet) {
        final FlexibleSelectFacetBuilder<T> builder = new FlexibleSelectFacetBuilder<>(facet.getKey(), facet.getLabel(),
                facet.getType(), facet.getSearchModel(), facet.getMapper());
        builder.multiSelect = facet.isMultiSelect();
        builder.matchingAll = facet.isMatchingAll();
        builder.threshold = facet.getThreshold();
        builder.limit = facet.getLimit();
        builder.facetResult = facet.getFacetResult();
        builder.selectedValues = facet.getSelectedValues();
        builder.matchingAll = facet.isMatchingAll();
        return builder;
    }

    @Override
    protected FlexibleSelectFacetBuilder<T> getThis() {
        return this;
    }
}
