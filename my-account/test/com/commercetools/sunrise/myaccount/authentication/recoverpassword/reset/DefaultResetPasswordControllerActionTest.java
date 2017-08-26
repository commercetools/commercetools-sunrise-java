package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctprequests.CustomerPasswordResetCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerPasswordResetCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DefaultResetPasswordControllerAction}.
 */
public class DefaultResetPasswordControllerActionTest {
    @Mock
    private SphereClient sphereClient;
    @Mock
    private HookRunner hookRunner;
    @Mock
    private Customer customer;
    @Captor
    private ArgumentCaptor<CustomerPasswordResetCommand> customerPasswordResetCommandCaptor;

    @InjectMocks
    private DefaultResetPasswordControllerAction defaultResetPasswordControllerAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute() throws ExecutionException, InterruptedException {
        final String password = "password";

        final DefaultResetPasswordFormData resetPasswordFormData = new DefaultResetPasswordFormData();
        resetPasswordFormData.setConfirmPassword(password);
        resetPasswordFormData.setNewPassword(password);

        final String resetTokenValue = "reset-token-value";

        when:
        {
            when(hookRunner.runUnaryOperatorHook(eq(CustomerPasswordResetCommandHook.class), any(), customerPasswordResetCommandCaptor.capture()))
                    .thenAnswer(invocation -> invocation.getArgument(2));
            when(sphereClient.execute(any()))
                    .thenReturn(CompletableFuture.completedFuture(customer));
        }

        final Customer updatedCustomer = defaultResetPasswordControllerAction.apply(resetTokenValue, resetPasswordFormData)
                .toCompletableFuture().get();

        then: {
            assertThat(updatedCustomer).isNotNull();

            final CustomerPasswordResetCommand customerPasswordResetCommand = customerPasswordResetCommandCaptor.getValue();
            assertThat(customerPasswordResetCommand.getTokenValue()).isEqualTo(resetTokenValue);
            assertThat(customerPasswordResetCommand.getNewPassword()).isEqualTo(password);
        }
    }
}
