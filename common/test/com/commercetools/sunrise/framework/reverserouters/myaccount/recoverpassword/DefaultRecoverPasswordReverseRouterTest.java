package com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DefaultRecoverPasswordReverseRouter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultRecoverPasswordReverseRouterTest {
    @Mock
    private SimpleRecoverPasswordReverseRouter simpleRecoverPasswordReverseRouter;

    private Locale locale = Locale.ENGLISH;
    private DefaultRecoverPasswordReverseRouter defaultResetPasswordReverseRouter;

    @Before
    public void setup() {
        defaultResetPasswordReverseRouter = new DefaultRecoverPasswordReverseRouter(locale, simpleRecoverPasswordReverseRouter);
    }

    @Test
    public void requestRecoveryEmailPageCall() {
        defaultResetPasswordReverseRouter.requestRecoveryEmailPageCall(locale.toLanguageTag());

        verify(simpleRecoverPasswordReverseRouter)
                .requestRecoveryEmailPageCall(locale.toLanguageTag());
    }

    @Test
    public void requestRecoveryEmailProcessCall() {
        defaultResetPasswordReverseRouter.requestRecoveryEmailProcessCall(locale.toLanguageTag());

        verify(simpleRecoverPasswordReverseRouter)
                .requestRecoveryEmailProcessCall(locale.toLanguageTag());
    }

    @Test
    public void resetPasswordPageCall() {
        final String tokenValue = "taken-value";
        defaultResetPasswordReverseRouter.resetPasswordPageCall(locale.toLanguageTag(), tokenValue);

        verify(simpleRecoverPasswordReverseRouter)
                .resetPasswordPageCall(locale.toLanguageTag(), tokenValue);
    }

    @Test
    public void resetPasswordProcessCall() {
        final String tokenValue = "taken-value";
        defaultResetPasswordReverseRouter.resetPasswordProcessCall(locale.toLanguageTag(), tokenValue);

        verify(simpleRecoverPasswordReverseRouter)
                .resetPasswordProcessCall(locale.toLanguageTag(), tokenValue);
    }
}
