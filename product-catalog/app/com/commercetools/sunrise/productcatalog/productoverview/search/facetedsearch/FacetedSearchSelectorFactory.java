package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.products.ProductProjection;

public abstract class FacetedSearchSelectorFactory<T> {

    protected final FacetedSearchConfig<T> config;

    protected FacetedSearchSelectorFactory(final FacetedSearchConfig<T> config) {
        this.config = config;
    }

    public FacetedSearchSelector create() {
        return FacetedSearchSelector.of(initializeFacet(), config.getPosition());
    }

    protected String key() {
        return config.getFacetBuilder().getKey();
    }

    protected abstract Facet<ProductProjection> initializeFacet();
}
