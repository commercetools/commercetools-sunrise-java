package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;
import io.sphere.sdk.search.*;

import javax.annotation.Nullable;
import java.util.List;

public final class MultiSelectFacetBuilder<T> extends BaseSelectFacetBuilder<MultiSelectFacetBuilder<T>> implements Builder<MultiSelectFacet<T>> {
    private final UntypedSearchModel<T> searchModel;
    private boolean matchingAll = false;

    private MultiSelectFacetBuilder(final String key, final String label, final UntypedSearchModel<T> searchModel) {
        super(key, label);
        this.searchModel = searchModel;
    }

    @Override
    public MultiSelectFacet<T> build() {
        return new MultiSelectFacet<>(getKey(), getLabel(), searchModel, type, matchingAll, selectedValues,
                termFacetResult.orElse(null), threshold.orElse(null), limit.orElse(null));
    }

    @Override
    public MultiSelectFacetBuilder<T> type(final SelectFacetType type) {
        return super.type(type);
    }

    @Override
    public MultiSelectFacetBuilder<T> selectedValues(final List<String> selectedValues) {
        return super.selectedValues(selectedValues);
    }

    @Override
    public MultiSelectFacetBuilder<T> termFacetResult(@Nullable final TermFacetResult termFacetResult) {
        return super.termFacetResult(termFacetResult);
    }

    @Override
    public MultiSelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        return super.threshold(threshold);
    }

    @Override
    public MultiSelectFacetBuilder<T> limit(@Nullable final Long limit) {
        return super.limit(limit);
    }

    public MultiSelectFacetBuilder<T> matchingAll(final boolean matchingAll) {
        this.matchingAll = matchingAll;
        return this;
    }

    public boolean isMatchingAll() {
        return matchingAll;
    }

    public static <T> MultiSelectFacetBuilder<T> of(final String key, final String label, final TermModel<T, ?> searchModel) {
        return new MultiSelectFacetBuilder<>(key, label, searchModel.untyped());
    }

    public static <T> MultiSelectFacetBuilder<T> of(final String key, final String label, final RangeTermModel<T, ?> searchModel) {
        return new MultiSelectFacetBuilder<>(key, label, searchModel.untyped());
    }

    public static <T> MultiSelectFacetBuilder<T> of(final MultiSelectFacet<T> selectFacet) {
        final MultiSelectFacetBuilder<T> builder = new MultiSelectFacetBuilder<>(selectFacet.getKey(), selectFacet.getLabel(),
                selectFacet.getSearchModel());
        builder.type = selectFacet.getType();
        builder.threshold = selectFacet.getThreshold();
        builder.limit = selectFacet.getLimit();
        builder.termFacetResult = selectFacet.getTermFacetResult();
        builder.selectedValues = selectFacet.getSelectedValues();
        builder.matchingAll = selectFacet.isMatchingAll();
        return builder;
    }

    @Override
    protected MultiSelectFacetBuilder<T> getThis() {
        return this;
    }
}
