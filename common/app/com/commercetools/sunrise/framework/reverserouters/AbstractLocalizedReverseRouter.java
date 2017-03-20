package com.commercetools.sunrise.framework.reverserouters;

import io.sphere.sdk.models.Base;

import java.util.Locale;

public abstract class AbstractLocalizedReverseRouter extends Base implements LocalizedReverseRouter {

    private final Locale locale;

    protected AbstractLocalizedReverseRouter(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public final Locale locale() {
        return locale;
    }
}
