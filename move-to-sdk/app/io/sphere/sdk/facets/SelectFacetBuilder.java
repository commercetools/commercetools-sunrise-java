package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Builder;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class SelectFacetBuilder<T> extends Base implements Builder<SelectFacet<T>> {
    private final String key;
    private final String label;
    private FacetType type = DefaultFacetType.SELECT;
    private final TermFacetedSearchSearchModel<T> searchModel;
    private boolean countHidden = false;
    private boolean multiSelect = true;
    private boolean matchingAll = false;
    private List<String> selectedValues = Collections.emptyList();
    private Optional<TermFacetResult> facetResult = Optional.empty();
    private Optional<Long> threshold = Optional.of(1L);
    private Optional<Long> limit = Optional.empty();
    private Optional<FacetOptionMapper> mapper = Optional.empty();

    private SelectFacetBuilder(final String key, final String label, final TermFacetedSearchSearchModel<T> searchModel) {
        this.key = key;
        this.label = label;
        this.searchModel = searchModel;
    }

    @Override
    public SelectFacet<T> build() {
        return new SelectFacetImpl<>(getKey(), getLabel(), countHidden, getType(), searchModel, multiSelect, matchingAll,
                selectedValues, facetResult.orElse(null), threshold.orElse(null), limit.orElse(null), mapper.orElse(null));
    }

    public SelectFacetBuilder<T> mapper(@Nullable final FacetOptionMapper mapper) {
        this.mapper = Optional.ofNullable(mapper);
        return this;
    }

    public SelectFacetBuilder<T> type(final FacetType type) {
        this.type = type;
        return this;
    }

    public SelectFacetBuilder<T> countHidden(final boolean countHidden) {
        this.countHidden = countHidden;
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

    public SelectFacetBuilder<T> selectedValues(final List<String> selectedValues) {
        this.selectedValues = selectedValues;
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

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public FacetType getType() {
        return type;
    }

    public boolean isCountHidden() {
        return countHidden;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public boolean isMatchingAll() {
        return matchingAll;
    }

    public List<String> getSelectedValues() {
        return selectedValues;
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

    public TermFacetedSearchSearchModel<T> getSearchModel() {
        return searchModel;
    }

    public static <T> SelectFacetBuilder<T> of(final String key, final String label, final TermFacetedSearchSearchModel<T> searchModel) {
        return new SelectFacetBuilder<>(key, label, searchModel);
    }

    public static <T> SelectFacetBuilder<T> of(final SelectFacet<T> facet) {
        final SelectFacetBuilder<T> builder = new SelectFacetBuilder<>(facet.getKey(), facet.getLabel(), facet.getSearchModel());
        builder.type = facet.getType();
        builder.mapper = facet.getMapper();
        builder.countHidden = facet.isCountHidden();
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
