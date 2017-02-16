package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.injection.RequestScoped;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionCartLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements CartReverseRouter {

    private final CartSimpleReverseRouter delegate;

    @Inject
    private ReflectionCartLocalizedReverseRouter(final Locale locale, final CartSimpleReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call showCart(final String languageTag) {
        return delegate.showCart(languageTag);
    }

    @Override
    public Call processAddProductToCartForm(final String languageTag) {
        return delegate.processAddProductToCartForm(languageTag);
    }

    @Override
    public Call processDeleteLineItemForm(final String languageTag) {
        return delegate.processDeleteLineItemForm(languageTag);
    }

    @Override
    public Call processChangeLineItemQuantityForm(final String languageTag) {
        return delegate.processChangeLineItemQuantityForm(languageTag);
    }
}
