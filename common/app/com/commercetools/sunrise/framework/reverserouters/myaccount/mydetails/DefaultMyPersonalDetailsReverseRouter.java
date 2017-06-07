package com.commercetools.sunrise.framework.reverserouters.myaccount.mydetails;

import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

public class DefaultMyPersonalDetailsReverseRouter extends AbstractLocalizedReverseRouter implements MyPersonalDetailsReverseRouter {

    private final SimpleMyPersonalDetailsReverseRouter delegate;

    @Inject
    protected DefaultMyPersonalDetailsReverseRouter(final Locale locale, final SimpleMyPersonalDetailsReverseRouter reverseRouter) {
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
