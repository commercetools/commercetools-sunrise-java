package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionLocalizationLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements LocalizationLocalizedReverseRouter {

    private final LocalizationReverseRouter delegate;

    @Inject
    private ReflectionLocalizationLocalizedReverseRouter(final Locale locale, final LocalizationReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call processChangeLanguageForm() {
        return delegate.processChangeLanguageForm();
    }

    @Override
    public Call processChangeCountryForm(final String languageTag) {
        return delegate.processChangeCountryForm(languageTag);
    }
}
