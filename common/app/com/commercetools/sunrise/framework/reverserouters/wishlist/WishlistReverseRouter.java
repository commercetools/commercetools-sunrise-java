package com.commercetools.sunrise.framework.reverserouters.wishlist;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultWishlistReverseRouter.class)
public interface WishlistReverseRouter extends SimpleWishlistReverseRouter, LocalizedReverseRouter {

    default Call addToWishlistProcess() {
        return addToWishlistProcessCall(locale().toLanguageTag());
    }

    default Call removeFromWishlistProcess() {
        return removeFromWishlistProcessCall(locale().toLanguageTag());
    }

    default Call clearWishlistProcess() {
        return clearWishlistProcessCall(locale().toLanguageTag());
    }

    default Call wishlistPageCall() {
        return wishlistPageCall(locale().toLanguageTag());
    }
}
