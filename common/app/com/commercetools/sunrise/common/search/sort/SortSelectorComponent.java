package com.commercetools.sunrise.common.search.sort;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.forms.FormUtils;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.events.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.SortExpression;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.forms.FormUtils.findSelectedValueFromRequest;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public final class SortSelectorComponent extends Base implements ControllerComponent, PageDataReadyHook, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook {

    private final List<SortExpression<ProductProjection>> selectedSortExpressions;
    private final SortSelectorBeanFactory sortSelectorBeanFactory;

    @Nullable
    private PagedSearchResult<ProductProjection> pagedSearchResult;

    @Inject
    public SortSelectorComponent(final Locale locale, final SortFormSettings settings, final RequestContext requestContext,
                                 final SortSelectorBeanFactory sortSelectorBeanFactory) {
        this.selectedSortExpressions = findSelectedValueFromRequest(settings, requestContext)
                .map(option -> option.getLocalizedValue(locale))
                .orElse(emptyList());
        this.sortSelectorBeanFactory = sortSelectorBeanFactory;
    }

    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        if (!selectedSortExpressions.isEmpty()) {
            return search.plusSort(selectedSortExpressions);
        } else {
            return search;
        }
    }

    @Override
    public CompletionStage<?> onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        this.pagedSearchResult = pagedSearchResult;
        return completedFuture(null);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (pagedSearchResult != null && pageData.getContent() instanceof WithSortSelectorViewModel) {
            final WithSortSelectorViewModel content = (WithSortSelectorViewModel) pageData.getContent();
            content.setSortSelector(sortSelectorBeanFactory.create(pagedSearchResult));
        }
    }
}
