package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermModel;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;

public final class FlexibleSelectFacetBuilder<T> extends BaseSelectFacetBuilder<FlexibleSelectFacetBuilder<T>> implements Builder<FlexibleSelectFacet<T>> {
    private final UntypedSearchModel<T> searchModel;

    private FlexibleSelectFacetBuilder(final String key, final String label, final UntypedSearchModel<T> searchModel) {
        super(key, label, FacetType.FLEXIBLE_SELECT);
        this.searchModel = searchModel;
    }

    @Override
    public FlexibleSelectFacet<T> build() {
        return new FlexibleSelectFacet<>(getKey(), getLabel(), getType(), searchModel, multiSelect, matchingAll, selectedValues,
                termFacetResult.orElse(null), threshold.orElse(null), limit.orElse(null));
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
    public FlexibleSelectFacetBuilder<T> termFacetResult(@Nullable final TermFacetResult termFacetResult) {
        return super.termFacetResult(termFacetResult);
    }

    @Override
    public FlexibleSelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        return super.threshold(threshold);
    }

    @Override
    public FlexibleSelectFacetBuilder<T> limit(@Nullable final Long limit) {
        return super.limit(limit);
    }

    public static <T> FlexibleSelectFacetBuilder<T> of(final String key, final String label, final TermModel<T, ?> searchModel) {
        return new FlexibleSelectFacetBuilder<>(key, label, searchModel.untyped());
    }

    public static <T> FlexibleSelectFacetBuilder<T> of(final FlexibleSelectFacet<T> facet) {
        final FlexibleSelectFacetBuilder<T> builder = new FlexibleSelectFacetBuilder<>(facet.getKey(), facet.getLabel(), facet.getSearchModel());
        builder.multiSelect = facet.isMultiSelect();
        builder.matchingAll = facet.isMatchingAll();
        builder.threshold = facet.getThreshold();
        builder.limit = facet.getLimit();
        builder.termFacetResult = facet.getTermFacetResult();
        builder.selectedValues = facet.getSelectedValues();
        builder.matchingAll = facet.isMatchingAll();
        return builder;
    }

    @Override
    protected FlexibleSelectFacetBuilder<T> getThis() {
        return this;
    }
}
