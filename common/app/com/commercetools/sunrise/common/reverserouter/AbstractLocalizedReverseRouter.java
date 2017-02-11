package com.commercetools.sunrise.common.reverserouter;

import io.sphere.sdk.models.Base;

import java.util.Locale;

public abstract class AbstractLocalizedReverseRouter extends Base implements LocalizedReverseRouter {

    private final Locale locale;

    protected AbstractLocalizedReverseRouter(final Locale locale) {
        this.locale = locale;
    }

    public String languageTag() {
        return locale.toLanguageTag();
    }

    @Override
    public Locale locale() {
        return locale;
    }
}
