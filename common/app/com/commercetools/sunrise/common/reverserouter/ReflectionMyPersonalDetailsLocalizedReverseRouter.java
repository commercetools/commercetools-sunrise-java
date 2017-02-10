package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionMyPersonalDetailsLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements MyPersonalDetailsLocalizedReverseRouter {

    private final MyPersonalDetailsReverseRouter delegate;

    @Inject
    private ReflectionMyPersonalDetailsLocalizedReverseRouter(final Locale locale, final MyPersonalDetailsReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call myPersonalDetailsPageCall(final String languageTag) {
        return delegate.myPersonalDetailsPageCall(languageTag);
    }

    @Override
    public Call myPersonalDetailsProcessFormCall(final String languageTag) {
        return delegate.myPersonalDetailsProcessFormCall(languageTag);
    }
}
