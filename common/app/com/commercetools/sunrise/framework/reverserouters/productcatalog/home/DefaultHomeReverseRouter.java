package com.commercetools.sunrise.framework.reverserouters.productcatalog.home;

import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

public class DefaultHomeReverseRouter extends AbstractLocalizedReverseRouter implements HomeReverseRouter {

    private final SimpleHomeReverseRouter delegate;

    @Inject
    protected DefaultHomeReverseRouter(final Locale locale, final SimpleHomeReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return delegate.homePageCall(languageTag);
    }
}
