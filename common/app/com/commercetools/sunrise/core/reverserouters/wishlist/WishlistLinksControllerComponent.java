package com.commercetools.sunrise.core.reverserouters.wishlist;

import com.commercetools.sunrise.core.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;

import javax.inject.Inject;

public class WishlistLinksControllerComponent  extends AbstractLinksControllerComponent<WishlistReverseRouter> {

    private final WishlistReverseRouter wishlistReverseRouter;

    @Inject
    protected WishlistLinksControllerComponent(final WishlistReverseRouter wishlistReverseRouter) {
        this.wishlistReverseRouter = wishlistReverseRouter;
    }

    @Override
    public WishlistReverseRouter getReverseRouter() {
        return wishlistReverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final WishlistReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.addToWishlistProcessCall(), "addToWishlist");
        meta.addHalLink(reverseRouter.removeFromWishlistProcessCall(), "removeFromWishlist");
        meta.addHalLink(reverseRouter.clearWishlistProcessCall(), "clearWishlist");
        meta.addHalLink(reverseRouter.wishlistPageCall(), "myWishlist");
    }
}
