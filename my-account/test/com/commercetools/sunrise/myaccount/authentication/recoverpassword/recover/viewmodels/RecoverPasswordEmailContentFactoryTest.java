package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels;

import com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword.RecoverPasswordReverseRouter;
import io.sphere.sdk.customers.CustomerToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.mvc.Call;
import play.mvc.Http;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecoverPasswordEmailContentFactoryTest {

    private static final String TOKEN_VALUE = "some-token-value";
    private static final String RESET_PASSWORD_URL = "https://some-url/recover";
    private static final String OTHER_RESET_PASSWORD_URL = "some-other-url";

    @Mock
    private RecoverPasswordReverseRouter fakeRecoverPasswordReverseRouter;

    @InjectMocks
    private RecoverPasswordEmailContentFactory defaultEmailContentFactory;
    @InjectMocks
    private CustomRecoverPasswordEmailContentFactory customEmailContentFactory;

    @Mock
    private CustomerToken fakeResetPasswordToken;

    @Before
    public void setUp() throws Exception {
        final Call callToResetPasswordUrl = mock(Call.class);
        when(callToResetPasswordUrl.absoluteURL(any())).thenReturn(RESET_PASSWORD_URL);
        when(fakeRecoverPasswordReverseRouter.resetPasswordPageCall(TOKEN_VALUE)).thenReturn(callToResetPasswordUrl);
        when(fakeResetPasswordToken.getValue()).thenReturn(TOKEN_VALUE);
        Http.Context.current.set(mock(Http.Context.class));
    }

    @Test
    public void createsEmailContent() throws Exception {
        final RecoverPasswordEmailContent emailContent = defaultEmailContentFactory.create(fakeResetPasswordToken);
        assertThat(emailContent.getPasswordResetUrl()).isEqualTo(RESET_PASSWORD_URL);
        assertThat(emailContent.getTitle()).isNull();
    }

    @Test
    public void allowsCustomEmailContent() throws Exception {
        final RecoverPasswordEmailContent emailContent = customEmailContentFactory.create(fakeResetPasswordToken);
        assertThat(emailContent.getPasswordResetUrl())
                .isNotEqualTo(RESET_PASSWORD_URL)
                .isEqualTo(OTHER_RESET_PASSWORD_URL);
        assertThat(emailContent.getTitle()).isNull();
        assertThat(emailContent.get("some-other-field")).isEqualTo("some-value");
    }

    private static class CustomRecoverPasswordEmailContentFactory extends RecoverPasswordEmailContentFactory {

        @Inject
        public CustomRecoverPasswordEmailContentFactory(final RecoverPasswordReverseRouter recoverPasswordReverseRouter) {
            super(recoverPasswordReverseRouter);
        }

        @Override
        protected void fillPasswordResetUrl(final RecoverPasswordEmailContent viewModel, final CustomerToken resetPasswordToken) {
            // Sets another password URL
            viewModel.setPasswordResetUrl(OTHER_RESET_PASSWORD_URL);
        }

        @Override
        protected RecoverPasswordEmailContent newViewModelInstance(final CustomerToken resetPasswordToken) {
            final RecoverPasswordEmailContent emailContent = super.newViewModelInstance(resetPasswordToken);
            fillAnotherField(emailContent, resetPasswordToken);
            return emailContent;
        }

        private void fillAnotherField(final RecoverPasswordEmailContent viewModel, final CustomerToken resetPasswordToken) {
            // Sets a new field
            viewModel.put("some-other-field", "some-value");
        }
    }
}
