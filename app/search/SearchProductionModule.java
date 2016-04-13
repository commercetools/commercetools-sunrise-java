package search;

import com.google.inject.AbstractModule;
import productcatalog.productoverview.search.FacetConfigList;
import productcatalog.productoverview.search.ProductsPerPageConfig;
import productcatalog.productoverview.search.SearchConfig;
import productcatalog.productoverview.search.SortConfig;

import javax.inject.Singleton;

public class SearchProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProductsPerPageConfig.class).toProvider(ProductsPerPageConfigProvider.class);
        bind(SortConfig.class).toProvider(SortConfigProvider.class);
        bind(FacetConfigList.class).toProvider(FacetConfigListProvider.class);
        bind(SearchConfig.class).toProvider(SearchConfigProvider.class).in(Singleton.class);
    }
}
