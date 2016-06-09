package productcatalog.productoverview.search.pagination;

import common.contexts.RequestContext;
import common.controllers.SunrisePageData;
import common.hooks.SunrisePageDataHook;
import common.models.FormSelectableOptionBean;
import framework.ControllerComponent;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.Configuration;
import productcatalog.hooks.ProductProjectionPagedSearchResultHook;
import productcatalog.hooks.ProductProjectionSearchFilterHook;
import productcatalog.productoverview.PaginationBean;
import productcatalog.productoverview.ProductOverviewPageContent;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageOption;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageSelector;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorBean;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class PaginationComponent extends Base implements ControllerComponent, SunrisePageDataHook, ProductProjectionSearchFilterHook, ProductProjectionPagedSearchResultHook {

    @Inject
    private ProductsPerPageSelectorFactory productsPerPageSelectorFactory;
    @Inject
    private PaginationFactory paginationFactory;
    @Inject
    private Configuration configuration;
    @Inject
    private RequestContext requestContext;

    @Nullable
    private ProductsPerPageSelector productsPerPageSelector;
    @Nullable
    private Pagination pagination;
    @Nullable
    private PagedSearchResult<ProductProjection> pagedSearchResult;


    @Override
    public ProductProjectionSearch filterProductProjectionSearch(final ProductProjectionSearch search) {
        pagination = paginationFactory.create();
        productsPerPageSelector = productsPerPageSelectorFactory.create();
        final int pageSize = productsPerPageSelector.getSelectedPageSize();
        return search
                .withOffset((pagination.getPage() - 1) * pageSize)
                .withLimit(pageSize);
    }

    @Override
    public void acceptProductProjectionPagedSearchResult(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        this.pagedSearchResult = pagedSearchResult;
    }

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        if (pagination != null && productsPerPageSelector != null && pagedSearchResult != null && sunrisePageData.getContent() instanceof ProductOverviewPageContent) {
            final ProductOverviewPageContent content = (ProductOverviewPageContent) sunrisePageData.getContent();
            final Integer paginationDisplayedPages = configuration.getInt("pop.pagination.displayedPages", 6);
            content.setPagination(new PaginationBean(requestContext, pagedSearchResult, pagination.getPage(), productsPerPageSelector.getSelectedPageSize(), paginationDisplayedPages));
            content.setDisplaySelector(createProductsPerPageSelector(productsPerPageSelectorFactory.create()));
        }
    }

    private ProductsPerPageSelectorBean createProductsPerPageSelector(final ProductsPerPageSelector productsPerPageSelector) {
        final ProductsPerPageSelectorBean bean = new ProductsPerPageSelectorBean();
        bean.setKey(productsPerPageSelector.getKey());
        bean.setList(productsPerPageSelector.getOptions().stream()
                .map(option -> optionToSelectableData(option, productsPerPageSelector))
                .collect(toList()));
        return bean;
    }

    private static FormSelectableOptionBean optionToSelectableData(final ProductsPerPageOption option, final ProductsPerPageSelector productsPerPageSelector) {
        final FormSelectableOptionBean displayOption = new FormSelectableOptionBean(option.getLabel(), option.getValue());
        if (productsPerPageSelector.getSelectedPageSize() == option.getAmount()) {
            displayOption.setSelected(true);
        }
        return displayOption;
    }
}
