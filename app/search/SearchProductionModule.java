package search;

import com.google.inject.AbstractModule;
import productcatalog.productoverview.search.facetedsearch.FacetedSearchConfigList;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageConfig;
import productcatalog.productoverview.search.sort.SortConfig;

import javax.inject.Singleton;

public class SearchProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProductsPerPageConfig.class).toProvider(ProductsPerPageConfigProvider.class).in(Singleton.class);
        bind(SortConfig.class).toProvider(SortConfigProvider.class).in(Singleton.class);
        bind(FacetedSearchConfigList.class).toProvider(FacetedSearchConfigListProvider.class).in(Singleton.class);
    }
}
