package com.commercetools.sunrise.framework.reverserouters.wishlist;

import com.commercetools.sunrise.framework.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMeta;

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
        meta.addHalLink(reverseRouter.addToWishlistProcess(), "addToWishlist");
        meta.addHalLink(reverseRouter.removeFromWishlistProcess(), "removeFromWishlist");
        meta.addHalLink(reverseRouter.clearWishlistProcess(), "clearWishlist");
        meta.addHalLink(reverseRouter.wishlistPageCall(), "myWishlist");
    }
}
