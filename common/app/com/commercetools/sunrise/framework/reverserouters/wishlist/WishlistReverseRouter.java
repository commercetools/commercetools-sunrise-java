package com.commercetools.sunrise.framework.reverserouters.wishlist;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultWishlistReverseRouter.class)
public interface WishlistReverseRouter extends ReverseRouter {

    String ADD_TO_WISHLIST_PROCESS = "addToWishlistProcessCall";

    String REMOVE_FROM_WISHLIST_PROCESS = "removeFromWishlistProcessCall";

    String CLEAR_WISHLIST_PROCESS = "clearWishlistProcessCall";

    String WISHLIST_PAGE = "wishlistPageCall";

    Call addToWishlistProcessCall();

    Call removeFromWishlistProcessCall();

    Call clearWishlistProcessCall();

    Call wishlistPageCall();
}
