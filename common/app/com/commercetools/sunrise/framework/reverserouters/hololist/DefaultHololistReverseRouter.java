package com.commercetools.sunrise.framework.reverserouters.hololist;

import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

public class DefaultHololistReverseRouter extends AbstractLocalizedReverseRouter implements HololistReverseRouter {
    private final SimpleHololistReverseRouter delegate;

    @Inject
    protected DefaultHololistReverseRouter(final Locale locale, final SimpleHololistReverseRouter delegate) {
        super(locale);
        this.delegate = delegate;
    }

    @Override
    public Call addToHololistProcessCall(final String languageTag) {
        return delegate.addToHololistProcessCall(languageTag);
    }

    @Override
    public Call removeFromHololistProcessCall(final String languageTag) {
        return delegate.removeFromHololistProcessCall(languageTag);
    }

    @Override
    public Call clearHololistProcessCall(final String languageTag) {
        return delegate.clearHololistProcessCall(languageTag);
    }

    @Override
    public Call hololistPageCall(final String languageTag) {
        return delegate.hololistPageCall(languageTag);
    }
}
