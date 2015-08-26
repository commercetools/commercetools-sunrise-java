package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;
import io.sphere.sdk.search.RangeTermModel;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermModel;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;

public final class SingleSelectFacetBuilder<T> extends BaseSelectFacetBuilder<SingleSelectFacetBuilder<T>> implements Builder<SingleSelectFacet> {
    private final UntypedSearchModel<T> searchModel;

    private SingleSelectFacetBuilder(final String key, final String label, final UntypedSearchModel<T> searchModel) {
        super(key, label);
        this.searchModel = searchModel;
    }

    @Override
    public SingleSelectFacet<T> build() {
        return new SingleSelectFacet<>(getKey(), getLabel(), searchModel, type, selectedValues,
                termFacetResult.orElse(null), threshold.orElse(null), limit.orElse(null));
    }

    @Override
    public SingleSelectFacetBuilder<T> selectedValues(final List<String> selectedValues) {
        return super.selectedValues(selectedValues);
    }

    @Override
    public SingleSelectFacetBuilder<T> termFacetResult(final TermFacetResult termFacetResult) {
        return super.termFacetResult(termFacetResult);
    }

    @Override
    public SingleSelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        return super.threshold(threshold);
    }

    @Override
    public SingleSelectFacetBuilder<T> limit(@Nullable final Long limit) {
        return super.limit(limit);
    }

    public static <T> SingleSelectFacetBuilder<T> of(final String key, final String label, final TermModel<T, ?> searchModel) {
        return new SingleSelectFacetBuilder<>(key, label, searchModel.untyped());
    }

    public static <T> SingleSelectFacetBuilder<T> of(final String key, final String label, final RangeTermModel<T, ?> searchModel) {
        return new SingleSelectFacetBuilder<>(key, label, searchModel.untyped());
    }
    public static <T> SingleSelectFacetBuilder<T> of(final SingleSelectFacet<T> selectFacet) {
        final SingleSelectFacetBuilder<T> builder = new SingleSelectFacetBuilder<>(selectFacet.getKey(), selectFacet.getLabel(),
                selectFacet.getSearchModel());
        builder.type = selectFacet.getType();
        builder.threshold = selectFacet.getThreshold();
        builder.limit = selectFacet.getLimit();
        builder.termFacetResult = selectFacet.getTermFacetResult();
        builder.selectedValues = selectFacet.getSelectedValues();
        return builder;
    }

    @Override
    protected SingleSelectFacetBuilder<T> getThis() {
        return this;
    }
}
