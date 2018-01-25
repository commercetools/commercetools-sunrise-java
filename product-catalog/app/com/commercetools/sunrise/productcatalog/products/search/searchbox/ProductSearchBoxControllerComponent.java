package com.commercetools.sunrise.productcatalog.products.search.searchbox;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.models.products.ProductListFetcherHook;
import com.commercetools.sunrise.models.search.searchbox.AbstractSearchBoxControllerComponent;
import io.sphere.sdk.models.LocalizedStringEntry;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class ProductSearchBoxControllerComponent extends AbstractSearchBoxControllerComponent implements ControllerComponent, ProductListFetcherHook {

    @Nullable
    private final LocalizedStringEntry searchText;

    @Inject
    public ProductSearchBoxControllerComponent(final ProductSearchBoxSettings productSearchBoxSettings, final Locale locale) {
        super(productSearchBoxSettings);
        this.searchText = productSearchBoxSettings.getSearchText(Http.Context.current(), locale).orElse(null);
    }

    @Override
    protected Optional<LocalizedStringEntry> getSearchText() {
        return Optional.ofNullable(searchText);
    }

    @Override
    public CompletionStage<PagedSearchResult<ProductProjection>> on(final ProductProjectionSearch request, final Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> nextComponent) {
        return nextComponent.apply(requestWithSearchText(request));
    }

    private ProductProjectionSearch requestWithSearchText(final ProductProjectionSearch search) {
        return searchText != null ? search.withText(searchText) : search;
    }
}
