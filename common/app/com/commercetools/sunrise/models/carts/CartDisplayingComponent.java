package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class CartDisplayingComponent implements ControllerComponent, PageDataHook {

    private final MyCartInCache myCartInCache;

    @Inject
    CartDisplayingComponent(final MyCartInCache myCartInCache) {
        this.myCartInCache = myCartInCache;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myCartInCache.get()
                .thenApply(cartOpt -> cartOpt
                        .map(cart -> pageData.put("cart", cart))
                        .orElse(pageData));
    }
}
