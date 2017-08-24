package com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword;

import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

public class DefaultRecoverPasswordReverseRouter extends AbstractLocalizedReverseRouter implements RecoverPasswordReverseRouter {
    private final SimpleRecoverPasswordReverseRouter delegate;

    @Inject
    protected DefaultRecoverPasswordReverseRouter(final Locale locale, final SimpleRecoverPasswordReverseRouter delegate) {
        super(locale);
        this.delegate = delegate;
    }

    @Override
    public Call resetPasswordPageCall(final String languageTag, final String token) {
        return delegate.resetPasswordPageCall(languageTag, token);
    }

    @Override
    public Call resetPasswordProcessCall(final String languageTag, final String token) {
        return delegate.resetPasswordProcessCall(languageTag, token);
    }

    @Override
    public Call requestRecoveryEmailPageCall(final String languageTag) {
        return delegate.requestRecoveryEmailPageCall(languageTag);
    }

    @Override
    public Call requestRecoveryEmailProcessCall(final String languageTag) {
        return delegate.requestRecoveryEmailProcessCall(languageTag);
    }
}
