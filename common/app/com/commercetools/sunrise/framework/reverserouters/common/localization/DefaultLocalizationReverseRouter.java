package com.commercetools.sunrise.framework.reverserouters.common.localization;

import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

public class DefaultLocalizationReverseRouter extends AbstractLocalizedReverseRouter implements LocalizationReverseRouter {

    private final SimpleLocalizationReverseRouter delegate;

    @Inject
    protected DefaultLocalizationReverseRouter(final Locale locale, final SimpleLocalizationReverseRouter reverseRouter) {
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
