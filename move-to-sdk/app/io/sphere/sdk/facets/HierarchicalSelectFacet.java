package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.UnaryOperator;

public final class HierarchicalSelectFacet<T> extends BaseSelectFacet<T> {
    private UnaryOperator<List<FacetOption>> mapperFunction;

    HierarchicalSelectFacet(final String key, final String label, final FacetType type, final UntypedSearchModel<T> searchModel,
                            final boolean multiSelect, final boolean matchingAll, final List<String> selectedValues,
                            @Nullable final TermFacetResult termFacetResult, @Nullable final Long threshold, @Nullable final Long limit,
                            final UnaryOperator<List<FacetOption>> mapperFunction) {
        super(key, label, type, searchModel, multiSelect, matchingAll, selectedValues, termFacetResult, threshold, limit);
        this.mapperFunction = mapperFunction;
    }

    @Override
    public List<FacetOption> getAllOptions() {
        return mapperFunction.apply(super.getAllOptions());
    }

    @Override
    public List<FacetOption> getLimitedOptions() {
        return super.getLimitedOptions();
    }

    @Override
    public boolean isAvailable() {
        return super.isAvailable();
    }

    public UnaryOperator<List<FacetOption>> getMapperFunction() {
        return mapperFunction;
    }

    @Override
    public HierarchicalSelectFacet<T> withSelectedValues(final List<String> selectedValues) {
        return HierarchicalSelectFacetBuilder.of(this).selectedValues(selectedValues).build();
    }

    @Override
    public HierarchicalSelectFacet<T> withTermFacetResult(final TermFacetResult termFacetResult) {
        return HierarchicalSelectFacetBuilder.of(this).termFacetResult(termFacetResult).build();
    }

    public HierarchicalSelectFacet<T> withMapperFunction(final UnaryOperator<List<FacetOption>> mapperFunction) {
        return HierarchicalSelectFacetBuilder.of(this).mapperFunction(mapperFunction).build();
    }
}
