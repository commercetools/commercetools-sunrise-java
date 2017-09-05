package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerCreatePasswordTokenCommandHook;
import com.commercetools.sunrise.email.EmailDeliveryException;
import com.commercetools.sunrise.email.EmailSender;
import com.commercetools.sunrise.email.MessageEditor;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;

import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DefaultRecoverPasswordControllerAction}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultRecoverPasswordControllerActionTest {

    private static final String CUSTOMER_EMAIL = "someone@mail.com";
    private static final String ANOTHER_CUSTOMER_EMAIL = "another@email.com";

    @Mock
    private SphereClient sphereClient;
    @Mock
    private HookRunner dummyHookRunner;
    @Mock
    private EmailSender emailSender;
    @Mock
    private RecoverPasswordMessageEditorProvider dummyMessageEditorProvider;

    @InjectMocks
    private DefaultRecoverPasswordControllerAction defaultPasswordRecoveryControllerAction;
    @InjectMocks
    private CustomRecoverPasswordControllerAction customPasswordRecoveryControllerAction;

    @Mock
    private RecoverPasswordFormData formDataWithValidEmail;
    @Mock
    private CustomerToken dummyForgetPasswordToken;
    private MessageEditor dummyMessageEditor = msg -> {};

    @Before
    public void setup() {
        when(formDataWithValidEmail.email()).thenReturn(CUSTOMER_EMAIL);
        when(dummyMessageEditorProvider.get(any(), any())).thenReturn(completedFuture(dummyMessageEditor));
        when(dummyHookRunner.runUnaryOperatorHook(any(), any(), any())).thenAnswer(invocation -> invocation.getArgument(2));
    }

    @Test
    public void sendsEmailOnValidCustomerToken() throws Exception {
        mockSphereClientThatReturnsCustomerToken();
        mockEmailSenderWithSuccessfulOutcome();

        final CustomerToken resetPasswordToken = executeDefaultControllerAction();

        assertThat(resetPasswordToken).isEqualTo(dummyForgetPasswordToken);

        verify(dummyHookRunner).runUnaryOperatorHook(eq(CustomerCreatePasswordTokenCommandHook.class), any(), any());
        verify(sphereClient).execute(CustomerCreatePasswordTokenCommand.of(CUSTOMER_EMAIL));
        verify(emailSender).send(dummyMessageEditor);
    }

    @Test
    public void returnsBadRequestOnInvalidEmail() throws Exception {
        mockSphereClientThatCannotFindEmailToRecover();

        assertThatThrownBy(this::executeDefaultControllerAction).hasCauseInstanceOf(NotFoundException.class);

        verify(dummyHookRunner).runUnaryOperatorHook(eq(CustomerCreatePasswordTokenCommandHook.class), any(), any());
        verify(sphereClient).execute(CustomerCreatePasswordTokenCommand.of(CUSTOMER_EMAIL));
        verify(emailSender, never()).send(dummyMessageEditor);
    }

    @Test
    public void returnsEmailDeliveryExceptionOnSendFailure() throws Exception {
        mockSphereClientThatReturnsCustomerToken();
        mockEmailSenderWithFailedDelivery();

        assertThatThrownBy(this::executeDefaultControllerAction).hasCauseInstanceOf(EmailDeliveryException.class);

        verify(dummyHookRunner).runUnaryOperatorHook(eq(CustomerCreatePasswordTokenCommandHook.class), any(), any());
        verify(sphereClient).execute(CustomerCreatePasswordTokenCommand.of(CUSTOMER_EMAIL));
        verify(emailSender).send(dummyMessageEditor);
    }

    @Test
    public void allowsToAlterRequestAndBehaviour() throws Exception {
        mockSphereClientThatReturnsCustomerToken();
        mockEmailSenderWithSuccessfulOutcome();

        final CustomerToken resetPasswordToken = executeCustomControllerAction();

        assertThat(resetPasswordToken).isEqualTo(dummyForgetPasswordToken);

        verify(dummyHookRunner).runUnaryOperatorHook(eq(CustomerCreatePasswordTokenCommandHook.class), any(), any());
        verify(sphereClient).execute(CustomerCreatePasswordTokenCommand.of(ANOTHER_CUSTOMER_EMAIL));
        verify(emailSender, never()).send(dummyMessageEditor);
    }

    private CustomerToken executeDefaultControllerAction() throws Exception {
        return defaultPasswordRecoveryControllerAction.apply(formDataWithValidEmail).toCompletableFuture().get();
    }

    private CustomerToken executeCustomControllerAction() throws Exception {
        return customPasswordRecoveryControllerAction.apply(formDataWithValidEmail).toCompletableFuture().get();
    }

    private void mockEmailSenderWithSuccessfulOutcome() {
        when(emailSender.send(notNull())).thenReturn(completedFuture("email-id"));
    }

    private void mockEmailSenderWithFailedDelivery() {
        when(emailSender.send(notNull())).thenReturn(exceptionallyCompletedFuture(new EmailDeliveryException("Failed to send")));
    }

    private void mockSphereClientThatReturnsCustomerToken() {
        when(sphereClient.execute(notNull())).thenReturn(completedFuture(dummyForgetPasswordToken));
    }

    private void mockSphereClientThatCannotFindEmailToRecover() {
        when(sphereClient.execute(notNull())).thenReturn(exceptionallyCompletedFuture(new NotFoundException()));
    }

    private static class CustomRecoverPasswordControllerAction extends DefaultRecoverPasswordControllerAction {

        @Inject
        public CustomRecoverPasswordControllerAction(final SphereClient sphereClient, final HookRunner hookRunner,
                                                     final EmailSender emailSender, final RecoverPasswordMessageEditorProvider recoverPasswordMessageEditorProvider) {
            super(sphereClient, hookRunner, emailSender, recoverPasswordMessageEditorProvider);
        }

        @Override
        protected CustomerCreatePasswordTokenCommand buildRequest(final RecoverPasswordFormData formData) {
            // Replaces request for another email
            return CustomerCreatePasswordTokenCommand.of(ANOTHER_CUSTOMER_EMAIL);
        }

        @Override
        protected CompletionStage<CustomerToken> onResetPasswordTokenCreated(final CustomerToken resetPasswordToken, final RecoverPasswordFormData recoveryEmailFormData) {
            // Does not send email
            return completedFuture(resetPasswordToken);
        }
    }
}
