package com.commercetools.sunrise.common.reverserouter;

import io.sphere.sdk.models.Base;

import java.util.Locale;

public abstract class AbstractLocalizedReverseRouter extends Base {

    private final String languageTag;

    protected AbstractLocalizedReverseRouter(final Locale locale) {
        this.languageTag = locale.toLanguageTag();
    }

    public String languageTag() {
        return languageTag;
    }
}
