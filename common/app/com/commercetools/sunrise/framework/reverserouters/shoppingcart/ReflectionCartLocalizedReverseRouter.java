package com.commercetools.sunrise.framework.reverserouters.shoppingcart;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
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
    public Call cartDetailPageCall(final String languageTag) {
        return delegate.cartDetailPageCall(languageTag);
    }

    @Override
    public Call addLineItemProcessCall(final String languageTag) {
        return delegate.addLineItemProcessCall(languageTag);
    }

    @Override
    public Call removeLineItemProcessCall(final String languageTag) {
        return delegate.removeLineItemProcessCall(languageTag);
    }

    @Override
    public Call changeLineItemQuantityProcessCall(final String languageTag) {
        return delegate.changeLineItemQuantityProcessCall(languageTag);
    }
}
