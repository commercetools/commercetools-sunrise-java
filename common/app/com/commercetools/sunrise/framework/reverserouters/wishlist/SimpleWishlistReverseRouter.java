package com.commercetools.sunrise.framework.reverserouters.wishlist;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleWishlistReverseRouterByReflection.class)
public interface SimpleWishlistReverseRouter {
    String ADD_TO_WISHLIST_PROCESS = "addToWishlistProcessCall";

    String REMOVE_FROM_WISHLIST_PROCESS = "removeFromWishlistProcessCall";

    String CLEAR_WISHLIST_PROCESS = "clearWishlistProcessCall";

    String WISHLIST_PAGE = "wishlistPageCall";

    Call addToWishlistProcessCall(String languageTag);

    Call removeFromWishlistProcessCall(String languageTag);

    Call clearWishlistProcessCall(String languageTag);

    Call wishlistPageCall(String languageTag);
}
