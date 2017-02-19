package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionMyPersonalDetailsLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements MyPersonalDetailsReverseRouter {

    private final MyPersonalDetailsSimpleReverseRouter delegate;

    @Inject
    private ReflectionMyPersonalDetailsLocalizedReverseRouter(final Locale locale, final MyPersonalDetailsSimpleReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call myPersonalDetailsPageCall(final String languageTag) {
        return delegate.myPersonalDetailsPageCall(languageTag);
    }

    @Override
    public Call myPersonalDetailsProcessCall(final String languageTag) {
        return delegate.myPersonalDetailsProcessCall(languageTag);
    }
}
