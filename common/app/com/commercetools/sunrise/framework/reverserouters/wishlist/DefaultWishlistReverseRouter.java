package com.commercetools.sunrise.framework.reverserouters.wishlist;

import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

public class DefaultWishlistReverseRouter extends AbstractLocalizedReverseRouter implements WishlistReverseRouter {
    private final SimpleWishlistReverseRouter delegate;

    @Inject
    protected DefaultWishlistReverseRouter(final Locale locale, final SimpleWishlistReverseRouter delegate) {
        super(locale);
        this.delegate = delegate;
    }

    @Override
    public Call addToWishlistProcessCall(final String languageTag) {
        return delegate.addToWishlistProcessCall(languageTag);
    }

    @Override
    public Call removeFromWishlistProcessCall(final String languageTag) {
        return delegate.removeFromWishlistProcessCall(languageTag);
    }

    @Override
    public Call clearWishlistProcessCall(final String languageTag) {
        return delegate.clearWishlistProcessCall(languageTag);
    }

    @Override
    public Call wishlistPageCall(final String languageTag) {
        return delegate.wishlistPageCall(languageTag);
    }
}
