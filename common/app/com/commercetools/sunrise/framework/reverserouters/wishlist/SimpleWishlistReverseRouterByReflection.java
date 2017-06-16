package com.commercetools.sunrise.framework.reverserouters.wishlist;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class SimpleWishlistReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleWishlistReverseRouter {
    private final ReverseCaller addToWishlistCaller;
    private final ReverseCaller removeFromWishlistCaller;
    private final ReverseCaller clearWishlistCaller;
    private final ReverseCaller wishlistPageCaller;

    @Inject
    public SimpleWishlistReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        addToWishlistCaller = getReverseCallerForSunriseRoute(ADD_TO_WISHLIST_PROCESS, parsedRoutes);
        removeFromWishlistCaller = getReverseCallerForSunriseRoute(REMOVE_FROM_WISHLIST_PROCESS, parsedRoutes);
        clearWishlistCaller = getReverseCallerForSunriseRoute(CLEAR_WISHLIST_PROCESS, parsedRoutes);
        wishlistPageCaller = getReverseCallerForSunriseRoute(WISHLIST_PAGE, parsedRoutes);
    }

    @Override
    public Call addToWishlistProcessCall(final String languageTag) {
        return addToWishlistCaller.call(languageTag);
    }

    @Override
    public Call removeFromWishlistProcessCall(final String languageTag) {
        return removeFromWishlistCaller.call(languageTag);
    }

    @Override
    public Call clearWishlistProcessCall(final String languageTag) {
        return clearWishlistCaller.call(languageTag);
    }

    @Override
    public Call wishlistPageCall(final String languageTag) {
        return wishlistPageCaller.call(languageTag);
    }
}