package com.commercetools.sunrise.productcatalog.productoverview.search.searchbox;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctprequests.ProductProjectionSearchHook;
import com.commercetools.sunrise.search.searchbox.AbstractSearchBoxControllerComponent;
import io.sphere.sdk.models.LocalizedStringEntry;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Locale;

public final class ProductSearchBoxControllerComponent extends AbstractSearchBoxControllerComponent implements ControllerComponent, ProductProjectionSearchHook {

    private final LocalizedStringEntry searchText;

    @Inject
    public ProductSearchBoxControllerComponent(final ProductSearchBoxSettings productSearchBoxSettings,
                                               final Http.Context httpContext, final Locale locale) {
        super(productSearchBoxSettings);
        this.searchText = productSearchBoxSettings.getSearchText(httpContext, locale);
    }

    @Override
    protected LocalizedStringEntry getSearchText() {
        return searchText;
    }

    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        if (!searchText.getValue().isEmpty()) {
            return search.withText(searchText);
        } else {
            return search;
        }
    }
}
