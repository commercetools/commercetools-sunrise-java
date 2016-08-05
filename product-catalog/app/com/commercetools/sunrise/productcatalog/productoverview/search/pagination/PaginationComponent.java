package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.pagination.Pagination;
import com.commercetools.sunrise.common.pagination.PaginationBeanFactory;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataHook;
import com.commercetools.sunrise.hooks.events.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewPageContent;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageOption;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageSelector;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorBean;
import com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public class PaginationComponent extends Base implements ControllerComponent, PageDataHook, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook {

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
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        pagination = paginationFactory.create();
        productsPerPageSelector = productsPerPageSelectorFactory.create();
        final int pageSize = productsPerPageSelector.getSelectedPageSize();
        final int page = pagination != null ? pagination.getPage() : PaginationFactory.DEFAULT_PAGE;
        return search
                .withOffset((page - 1) * pageSize)
                .withLimit(pageSize);
    }

    @Override
    public CompletionStage<?> onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        this.pagedSearchResult = pagedSearchResult;
        return completedFuture(null);
    }

    @Override
    public void onPageDataCreated(final PageData pageData) {
        if (pagination != null && productsPerPageSelector != null && pagedSearchResult != null && pageData.getContent() instanceof ProductOverviewPageContent) {
            final ProductOverviewPageContent content = (ProductOverviewPageContent) pageData.getContent();
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
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        bean.setLabel(option.getLabel());
        bean.setValue(option.getValue());
        bean.setSelected(productsPerPageSelector.getSelectedPageSize() == option.getAmount());
        return bean;
    }
}
