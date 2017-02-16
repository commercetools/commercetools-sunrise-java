package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.injection.RequestScoped;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionHomeLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements HomeReverseRouter {

    private final HomeSimpleReverseRouter delegate;

    @Inject
    private ReflectionHomeLocalizedReverseRouter(final Locale locale, final HomeSimpleReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return delegate.homePageCall(languageTag);
    }
}
