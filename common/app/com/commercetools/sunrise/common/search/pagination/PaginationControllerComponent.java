package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.framework.hooks.requests.ProductProjectionSearchHook;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.forms.QueryStringUtils.findSelectedValueFromQueryString;
import static java.util.concurrent.CompletableFuture.completedFuture;

public final class PaginationControllerComponent extends Base implements ControllerComponent, PageDataReadyHook, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook {

    private static final int CTP_DEFAULT_PAGE_SIZE = 20;
    private final int currentPage;
    private final int pageSize;
    private final PaginationViewModelFactory paginationViewModelFactory;
    private final ProductsPerPageSelectorViewModelFactory productsPerPageSelectorViewModelFactory;

    @Nullable
    private PagedSearchResult<ProductProjection> pagedSearchResult;

    @Inject
    public PaginationControllerComponent(final Http.Request httpRequest, final PaginationSettings paginationSettings,
                                         final ProductsPerPageFormSettings productsPerPageFormSettings,
                                         final PaginationViewModelFactory paginationViewModelFactory, final ProductsPerPageSelectorViewModelFactory productsPerPageSelectorViewModelFactory) {
        this.currentPage = findSelectedValueFromQueryString(paginationSettings, httpRequest);
        this.pageSize = findSelectedValueFromQueryString(productsPerPageFormSettings, httpRequest)
                .map(ProductsPerPageFormOption::getValue)
                .orElse(CTP_DEFAULT_PAGE_SIZE);
        this.paginationViewModelFactory = paginationViewModelFactory;
        this.productsPerPageSelectorViewModelFactory = productsPerPageSelectorViewModelFactory;
    }

    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        return search
                .withOffset((currentPage - 1) * pageSize)
                .withLimit(pageSize);
    }

    @Override
    public CompletionStage<?> onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        this.pagedSearchResult = pagedSearchResult;
        return completedFuture(null);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (pagedSearchResult != null && pageData.getContent() instanceof WithPaginationViewModel) {
            final WithPaginationViewModel content = (WithPaginationViewModel) pageData.getContent();
            content.setPagination(paginationViewModelFactory.create(pagedSearchResult));
            content.setDisplaySelector(productsPerPageSelectorViewModelFactory.create(pagedSearchResult));
        }
    }
}
