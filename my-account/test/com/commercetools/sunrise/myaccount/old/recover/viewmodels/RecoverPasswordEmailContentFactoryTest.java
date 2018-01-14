package com.commercetools.sunrise.myaccount.old.recover.viewmodels;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RecoverPasswordEmailContentFactoryTest {

//    private static final String TOKEN_VALUE = "some-token-value";
//    private static final String RESET_PASSWORD_URL = "https://some-url/recover";
//    private static final String OTHER_RESET_PASSWORD_URL = "some-other-url";
//
//    @InjectMocks
//    private RecoverPasswordEmailContentFactory defaultEmailContentFactory;
//    @InjectMocks
//    private CustomRecoverPasswordEmailContentFactory customEmailContentFactory;
//
//    @Mock
//    private CustomerToken fakeResetPasswordToken;
//
//    @Before
//    public void setUp() throws Exception {
//        final Call callToResetPasswordUrl = mock(Call.class);
//        when(callToResetPasswordUrl.absoluteURL(any())).thenReturn(RESET_PASSWORD_URL);
//        when(fakeRecoverPasswordReverseRouter.resetPasswordPageCall(TOKEN_VALUE)).thenReturn(callToResetPasswordUrl);
//        when(fakeResetPasswordToken.getValue()).thenReturn(TOKEN_VALUE);
//        Http.Context.current.set(mock(Http.Context.class));
//    }
//
//    @Test
//    public void createsEmailContent() throws Exception {
//        final RecoverPasswordEmailContent emailContent = defaultEmailContentFactory.create(fakeResetPasswordToken);
//        assertThat(emailContent.getPasswordResetUrl()).isEqualTo(RESET_PASSWORD_URL);
//        assertThat(emailContent.getTitle()).isNull();
//    }
//
//    @Test
//    public void allowsCustomEmailContent() throws Exception {
//        final RecoverPasswordEmailContent emailContent = customEmailContentFactory.create(fakeResetPasswordToken);
//        assertThat(emailContent.getPasswordResetUrl())
//                .isNotEqualTo(RESET_PASSWORD_URL)
//                .isEqualTo(OTHER_RESET_PASSWORD_URL);
//        assertThat(emailContent.getTitle()).isNull();
//        assertThat(emailContent.get("some-other-field")).isEqualTo("some-value");
//    }
//
//    private static class CustomRecoverPasswordEmailContentFactory extends RecoverPasswordEmailContentFactory {
//
//        @Override
//        protected void fillPasswordResetUrl(final RecoverPasswordEmailContent viewModel, final CustomerToken resetPasswordToken) {
//            // Sets another password URL
//            viewModel.setPasswordResetUrl(OTHER_RESET_PASSWORD_URL);
//        }
//
//        @Override
//        protected RecoverPasswordEmailContent newViewModelInstance(final CustomerToken resetPasswordToken) {
//            final RecoverPasswordEmailContent emailContent = super.newViewModelInstance(resetPasswordToken);
//            fillAnotherField(emailContent, resetPasswordToken);
//            return emailContent;
//        }
//
//        private void fillAnotherField(final RecoverPasswordEmailContent viewModel, final CustomerToken resetPasswordToken) {
//            // Sets a new field
//            viewModel.put("some-other-field", "some-value");
//        }
//    }
}
