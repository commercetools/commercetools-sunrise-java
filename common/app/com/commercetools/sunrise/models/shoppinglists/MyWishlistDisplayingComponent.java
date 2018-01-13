package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class MyWishlistDisplayingComponent implements ControllerComponent, PageDataHook {

    private final MyWishlistInCache myWishlistInCache;

    @Inject
    MyWishlistDisplayingComponent(final MyWishlistInCache myWishlistInCache) {
        this.myWishlistInCache = myWishlistInCache;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myWishlistInCache.get()
                .thenApply(wishlistOpt -> wishlistOpt
                        .map(wishlist -> pageData.put("wishlist", wishlist))
                        .orElse(pageData));
    }
}
