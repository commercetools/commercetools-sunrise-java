package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.framework.ControllerComponent;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import com.commercetools.sunrise.productcatalog.hooks.ProductProjectionPagedSearchResultHook;
import com.commercetools.sunrise.productcatalog.hooks.ProductProjectionSearchFilterHook;
import com.commercetools.sunrise.productcatalog.productoverview.PaginationBeanFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewPageContent;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageOption;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageSelector;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorBean;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class PaginationComponent extends Base implements ControllerComponent, SunrisePageDataHook, ProductProjectionSearchFilterHook, ProductProjectionPagedSearchResultHook {

    @Inject
    private ProductsPerPageSelectorFactory productsPerPageSelectorFactory;
    @Inject
    private PaginationFactory paginationFactory;
    @Inject
    private PaginationBeanFactory paginationBeanFactory;

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
            content.setPagination(paginationBeanFactory.create(pagedSearchResult, pagination, productsPerPageSelector.getSelectedPageSize()));
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
