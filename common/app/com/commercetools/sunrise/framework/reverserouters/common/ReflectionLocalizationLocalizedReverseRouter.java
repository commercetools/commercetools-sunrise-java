package com.commercetools.sunrise.framework.reverserouters.common;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionLocalizationLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements LocalizationReverseRouter {

    private final LocalizationSimpleReverseRouter delegate;

    @Inject
    private ReflectionLocalizationLocalizedReverseRouter(final Locale locale, final LocalizationSimpleReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call changeLanguageProcessCall() {
        return delegate.changeLanguageProcessCall();
    }

    @Override
    public Call changeCountryProcessCall(final String languageTag) {
        return delegate.changeCountryProcessCall(languageTag);
    }
}
