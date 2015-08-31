package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;
import io.sphere.sdk.search.*;

import javax.annotation.Nullable;
import java.util.List;

public final class SelectFacetBuilder<T> extends BaseSelectFacetBuilder<SelectFacetBuilder<T>> implements Builder<SelectFacet<T>> {
    private final UntypedSearchModel<T> searchModel;

    private SelectFacetBuilder(final String key, final String label, final UntypedSearchModel<T> searchModel) {
        super(key, label, DefaultFacetType.SELECT);
        this.searchModel = searchModel;
    }

    @Override
    public SelectFacet<T> build() {
        return new SelectFacetImpl<>(getKey(), getLabel(), getType(), searchModel, multiSelect, matchingAll, selectedValues,
                facetResult.orElse(null), threshold.orElse(null), limit.orElse(null));
    }

    @Override
    public SelectFacetBuilder<T> multiSelect(final boolean multiSelect) {
        return super.multiSelect(multiSelect);
    }

    @Override
    public SelectFacetBuilder<T> matchingAll(final boolean matchingAll) {
        return super.matchingAll(matchingAll);
    }

    @Override
    public SelectFacetBuilder<T> selectedValues(final List<String> selectedValues) {
        return super.selectedValues(selectedValues);
    }

    @Override
    public SelectFacetBuilder<T> facetResult(@Nullable final TermFacetResult facetResult) {
        return super.facetResult(facetResult);
    }

    @Override
    public SelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        return super.threshold(threshold);
    }

    @Override
    public SelectFacetBuilder<T> limit(@Nullable final Long limit) {
        return super.limit(limit);
    }

    public static <T> SelectFacetBuilder<T> of(final String key, final String label, final TermModel<T, ?> searchModel) {
        return new SelectFacetBuilder<>(key, label, searchModel.untyped());
    }

    public static <T> SelectFacetBuilder<T> of(final SelectFacetImpl<T> facet) {
        final SelectFacetBuilder<T> builder = new SelectFacetBuilder<>(facet.getKey(), facet.getLabel(), facet.getSearchModel());
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
    protected SelectFacetBuilder<T> getThis() {
        return this;
    }
}
