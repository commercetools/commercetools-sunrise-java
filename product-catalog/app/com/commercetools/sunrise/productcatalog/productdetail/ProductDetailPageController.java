package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;

import javax.inject.Inject;

@RequestScoped
public final class ProductDetailPageController extends SunriseProductDetailPageController {

    @Inject
    public void setSuggestionsComponent(final ProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }
}