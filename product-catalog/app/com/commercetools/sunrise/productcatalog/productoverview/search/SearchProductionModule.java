package com.commercetools.sunrise.productcatalog.productoverview.search;

import com.commercetools.sunrise.productcatalog.productoverview.search.sort.SortConfigProvider;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.FacetedSearchConfigListProvider;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageConfigProvider;
import com.google.inject.AbstractModule;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.FacetedSearchConfigList;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageConfig;
import com.commercetools.sunrise.productcatalog.productoverview.search.sort.SortConfig;

import javax.inject.Singleton;

public class SearchProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProductsPerPageConfig.class).toProvider(ProductsPerPageConfigProvider.class).in(Singleton.class);
        bind(SortConfig.class).toProvider(SortConfigProvider.class).in(Singleton.class);
        bind(FacetedSearchConfigList.class).toProvider(FacetedSearchConfigListProvider.class).in(Singleton.class);
    }
}
