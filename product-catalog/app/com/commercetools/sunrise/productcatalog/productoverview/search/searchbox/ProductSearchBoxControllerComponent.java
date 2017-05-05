package com.commercetools.sunrise.productcatalog.productoverview.search.searchbox;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctprequests.ProductProjectionSearchHook;
import com.commercetools.sunrise.search.searchbox.AbstractSearchBoxControllerComponent;
import io.sphere.sdk.models.LocalizedStringEntry;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

public final class ProductSearchBoxControllerComponent extends AbstractSearchBoxControllerComponent implements ControllerComponent, ProductProjectionSearchHook {

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
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        if (searchText != null) {
            return search.withText(searchText);
        } else {
            return search;
        }
    }
}
