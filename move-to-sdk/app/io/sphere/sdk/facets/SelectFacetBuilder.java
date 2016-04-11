package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.model.FacetedSearchSearchModel;

import javax.annotation.Nullable;
import java.util.Optional;

public final class SelectFacetBuilder<T> extends FacetBuilder<SelectFacetBuilder<T>> implements Builder<SelectFacet<T>> {
    private final FacetedSearchSearchModel<T> searchModel;
    private boolean multiSelect = true;
    private boolean matchingAll = false;
    private Optional<TermFacetResult> facetResult = Optional.empty();
    private Optional<Long> threshold = Optional.of(1L);
    private Optional<Long> limit = Optional.empty();
    private Optional<FacetOptionMapper> mapper = Optional.empty();

    private SelectFacetBuilder(final String key, final FacetedSearchSearchModel<T> searchModel) {
        super(key);
        this.searchModel = searchModel;
    }

    @Override
    public SelectFacet<T> build() {
        return new SelectFacetImpl<>(getKey(), getLabel().orElse(null), isCountHidden(), getType(), searchModel, multiSelect, matchingAll,
                selectedValues, facetResult.orElse(null), threshold.orElse(null), limit.orElse(null), mapper.orElse(null));
    }

    public SelectFacetBuilder<T> mapper(@Nullable final FacetOptionMapper mapper) {
        this.mapper = Optional.ofNullable(mapper);
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
        this.facetResult = Optional.ofNullable(facetResult);
        return this;
    }

    public SelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        this.threshold = Optional.ofNullable(threshold);
        return this;
    }

    public SelectFacetBuilder<T> limit(@Nullable final Long limit) {
        this.limit = Optional.ofNullable(limit);
        return this;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public boolean isMatchingAll() {
        return matchingAll;
    }

    public Optional<TermFacetResult> getFacetResult() {
        return facetResult;
    }

    public Optional<Long> getThreshold() {
        return threshold;
    }

    public Optional<Long> getLimit() {
        return limit;
    }

    public Optional<FacetOptionMapper> getMapper() {
        return mapper;
    }

    public FacetedSearchSearchModel<T> getSearchModel() {
        return searchModel;
    }

    @Override
    protected SelectFacetBuilder<T> getThis() {
        return this;
    }

    public static <T> SelectFacetBuilder<T> of(final String key, final FacetedSearchSearchModel<T> searchModel) {
        return new SelectFacetBuilder<>(key, searchModel);
    }

    public static <T> SelectFacetBuilder<T> of(final SelectFacet<T> facet) {
        final SelectFacetBuilder<T> builder = new SelectFacetBuilder<>(facet.getKey(), facet.getSearchModel());
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
