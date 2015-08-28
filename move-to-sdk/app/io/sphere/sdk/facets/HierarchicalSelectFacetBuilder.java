package io.sphere.sdk.facets;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Builder;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermModel;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.UnaryOperator;

public final class HierarchicalSelectFacetBuilder<T> extends BaseSelectFacetBuilder<HierarchicalSelectFacetBuilder<T>> implements Builder<HierarchicalSelectFacet<T>> {
    private final UntypedSearchModel<T> searchModel;
    private UnaryOperator<List<FacetOption>> mapperFunction;

    private HierarchicalSelectFacetBuilder(final String key, final String label, final UntypedSearchModel<T> searchModel,
                                           final UnaryOperator<List<FacetOption>> mapperFunction) {
        super(key, label, FacetType.HIERARCHICAL_SELECT);
        this.searchModel = searchModel;
        this.mapperFunction = mapperFunction;
    }

    @Override
    public HierarchicalSelectFacet<T> build() {
        return new HierarchicalSelectFacet<>(getKey(), getLabel(), getType(), searchModel, multiSelect, matchingAll, selectedValues,
                termFacetResult.orElse(null), threshold.orElse(null), limit.orElse(null), mapperFunction);
    }

    @Override
    public HierarchicalSelectFacetBuilder<T> selectedValues(final List<String> selectedValues) {
        return super.selectedValues(selectedValues);
    }

    @Override
    public HierarchicalSelectFacetBuilder<T> termFacetResult(@Nullable final TermFacetResult termFacetResult) {
        return super.termFacetResult(termFacetResult);
    }

    @Override
    public HierarchicalSelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        return super.threshold(threshold);
    }

    @Override
    public HierarchicalSelectFacetBuilder<T> limit(@Nullable final Long limit) {
        return super.limit(limit);
    }

    public HierarchicalSelectFacetBuilder<T> mapperFunction(final UnaryOperator<List<FacetOption>> mapperFunction) {
        this.mapperFunction = mapperFunction;
        return this;
    }

    public UnaryOperator<List<FacetOption>> getMapperFunction() {
        return mapperFunction;
    }

    public static <T> HierarchicalSelectFacetBuilder<T> of(final String key, final String label, final TermModel<T, ?> searchModel,
                                                           final UnaryOperator<List<FacetOption>> mapperFunction) {
        return new HierarchicalSelectFacetBuilder<>(key, label, searchModel.untyped(), mapperFunction);
    }

    public static HierarchicalSelectFacetBuilder<ProductProjection> ofCategories(final String key, final String label, final CategoryTree categoryTree,
                                                                                 final List<Category> parentCategories, final List<Locale> locales) {
        final UnaryOperator<List<FacetOption>> mapperFunction = options -> CategoryHierarchyMapper.of(options, categoryTree, parentCategories, locales).build();
        return of(key, label, ProductProjectionSearchModel.of().categories().id(), mapperFunction);
    }

    public static <T> HierarchicalSelectFacetBuilder<T> of(final HierarchicalSelectFacet<T> facet) {
        final HierarchicalSelectFacetBuilder<T> builder = new HierarchicalSelectFacetBuilder<>(facet.getKey(), facet.getLabel(),
                facet.getSearchModel(), facet.getMapperFunction());
        builder.matchingAll = facet.isMatchingAll();
        builder.multiSelect = facet.isMultiSelect();
        builder.threshold = facet.getThreshold();
        builder.limit = facet.getLimit();
        builder.termFacetResult = facet.getTermFacetResult();
        builder.selectedValues = facet.getSelectedValues();
        return builder;
    }
    @Override
    protected HierarchicalSelectFacetBuilder<T> getThis() {
        return this;
    }

}
