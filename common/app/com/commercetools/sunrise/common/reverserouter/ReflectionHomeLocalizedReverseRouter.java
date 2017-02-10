package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionHomeLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements HomeLocalizedReverseRouter {

    private final HomeReverseRouter delegate;

    @Inject
    private ReflectionHomeLocalizedReverseRouter(final Locale locale, final HomeReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return delegate.homePageCall(languageTag);
    }
}
