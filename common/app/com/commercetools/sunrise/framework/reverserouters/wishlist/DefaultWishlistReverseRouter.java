package com.commercetools.sunrise.framework.reverserouters.wishlist;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultWishlistReverseRouter extends AbstractReflectionReverseRouter implements WishlistReverseRouter {

    private final ReverseCaller addToWishlistCaller;
    private final ReverseCaller removeFromWishlistCaller;
    private final ReverseCaller clearWishlistCaller;
    private final ReverseCaller wishlistPageCaller;

    @Inject
    protected DefaultWishlistReverseRouter(final ParsedRoutes parsedRoutes) {
        addToWishlistCaller = getReverseCallerForSunriseRoute(ADD_TO_WISHLIST_PROCESS, parsedRoutes);
        removeFromWishlistCaller = getReverseCallerForSunriseRoute(REMOVE_FROM_WISHLIST_PROCESS, parsedRoutes);
        clearWishlistCaller = getReverseCallerForSunriseRoute(CLEAR_WISHLIST_PROCESS, parsedRoutes);
        wishlistPageCaller = getReverseCallerForSunriseRoute(WISHLIST_PAGE, parsedRoutes);
    }

    @Override
    public Call addToWishlistProcessCall() {
        return addToWishlistCaller.call();
    }

    @Override
    public Call removeFromWishlistProcessCall() {
        return removeFromWishlistCaller.call();
    }

    @Override
    public Call clearWishlistProcessCall() {
        return clearWishlistCaller.call();
    }

    @Override
    public Call wishlistPageCall() {
        return wishlistPageCaller.call();
    }
}
