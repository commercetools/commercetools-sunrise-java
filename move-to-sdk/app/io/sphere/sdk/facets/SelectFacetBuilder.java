package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.model.FacetedSearchSearchModel;

import javax.annotation.Nullable;

public final class SelectFacetBuilder<T> extends FacetBuilder<SelectFacetBuilder<T>> implements Builder<SelectFacet<T>> {

    private FacetedSearchSearchModel<T> facetedSearchSearchModel;
    private boolean multiSelect = true;
    private boolean matchingAll = false;
    @Nullable private TermFacetResult facetResult = null;
    @Nullable private Long threshold = 1L;
    @Nullable private Long limit = null;
    @Nullable private FacetOptionMapper mapper = null;

    private SelectFacetBuilder(final String key, final FacetedSearchSearchModel<T> facetedSearchSearchModel) {
        super(key);
        this.facetedSearchSearchModel = facetedSearchSearchModel;
    }

    @Override
    public SelectFacet<T> build() {
        return new SelectFacetImpl<>(getKey(), getLabel(), isCountHidden(), getType(), facetedSearchSearchModel,
                multiSelect, matchingAll, selectedValues, facetResult, threshold, limit, mapper);
    }

    public SelectFacetBuilder<T> facetedSearchSearchModel(final FacetedSearchSearchModel<T> facetedSearchSearchModel) {
        this.facetedSearchSearchModel = facetedSearchSearchModel;
        return this;
    }

    public SelectFacetBuilder<T> mapper(@Nullable final FacetOptionMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public SelectFacetBuilder<T> multiSelect(final boolean multiSelect) {
        this.multiSelect = multiSelect;
        return this;
    }

    public SelectFacetBuilder<T> matchingAll(final boolean matchingAll) {
        this.matchingAll = matchingAll;
        return this;
    }

    public SelectFacetBuilder<T> facetResult(@Nullable final TermFacetResult facetResult) {
        this.facetResult = facetResult;
        return this;
    }

    public SelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        this.threshold = threshold;
        return this;
    }

    public SelectFacetBuilder<T> limit(@Nullable final Long limit) {
        this.limit = limit;
        return this;
    }

    public FacetedSearchSearchModel<T> getFacetedSearchSearchModel() {
        return facetedSearchSearchModel;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public boolean isMatchingAll() {
        return matchingAll;
    }

    @Nullable
    public TermFacetResult getFacetResult() {
        return facetResult;
    }

    @Nullable
    public Long getThreshold() {
        return threshold;
    }

    @Nullable
    public Long getLimit() {
        return limit;
    }

    @Nullable
    public FacetOptionMapper getMapper() {
        return mapper;
    }

    @Override
    protected SelectFacetBuilder<T> getThis() {
        return this;
    }

    public static <T> SelectFacetBuilder<T> of(final String key, final FacetedSearchSearchModel<T> searchModel) {
        return new SelectFacetBuilder<>(key, searchModel);
    }

    public static <T> SelectFacetBuilder<T> of(final SelectFacet<T> facet) {
        final SelectFacetBuilder<T> builder = new SelectFacetBuilder<>(facet.getKey(), facet.getFacetedSearchSearchModel());
        builder.type = facet.getType();
        builder.label = facet.getLabel();
        builder.countHidden = facet.isCountHidden();
        builder.mapper = facet.getMapper();
        builder.multiSelect = facet.isMultiSelect();
        builder.matchingAll = facet.isMatchingAll();
        builder.threshold = facet.getThreshold();
        builder.limit = facet.getLimit();
        builder.facetResult = facet.getFacetResult();
        builder.selectedValues = facet.getSelectedValues();
        builder.matchingAll = facet.isMatchingAll();
        return builder;
    }
}
