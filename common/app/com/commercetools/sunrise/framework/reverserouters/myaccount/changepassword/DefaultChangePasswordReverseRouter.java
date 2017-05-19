package com.commercetools.sunrise.framework.reverserouters.myaccount.changepassword;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class DefaultChangePasswordReverseRouter extends AbstractLocalizedReverseRouter implements ChangePasswordReverseRouter {

    private final SimpleChangePasswordReverseRouter delegate;

    @Inject
    protected DefaultChangePasswordReverseRouter(final Locale locale, final SimpleChangePasswordReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call changePasswordProcessCall(final String languageTag) {
        return delegate.changePasswordProcessCall(languageTag);
    }
}
