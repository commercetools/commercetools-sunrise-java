package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.template.engine.TemplateEngine;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordEmailPageContent;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordEmailPageContentFactory;
import io.commercetools.sunrise.email.EmailSender;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.CustomerToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DefaultRecoverPasswordControllerAction}.
 */
public class DefaultRecoverPasswordControllerActionTest {
    @Mock
    private SphereClient sphereClient;
    @Mock
    private HookRunner hookRunner;
    @Mock
    private RecoverPasswordEmailPageContentFactory recoverPasswordEmailPageContentFactory;
    @Mock
    private TemplateEngine templateEngine;
    @Mock
    private EmailSender emailSender;
    @Mock
    private CustomerToken customerToken;

    @InjectMocks
    private DefaultRecoverPasswordControllerAction defaultPasswordRecoveryControllerAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute() throws Exception {
        mockCustomerCreatePasswordTokenCommandRequest();
        mockRecoverPasswordEmailPageCreation();
        mockRenderEmailTemplate();
        mockSendEmail();

        final CompletableFuture<CustomerToken> passwordRecoveryActionResult =
                defaultPasswordRecoveryControllerAction.apply(formDataWithValidEmailOfExistingCustomer()).toCompletableFuture();

        assertThat(passwordRecoveryActionResult.get()).isEqualTo(customerToken);

        verify(recoverPasswordEmailPageContentFactory).create(eq(customerToken));
        verify(templateEngine).render(notNull(), notNull());
        verify(emailSender).send(notNull());
    }

    private void mockSendEmail() {
        when(emailSender.send(notNull())).thenReturn(CompletableFuture.completedFuture(null));
    }

    private void mockRenderEmailTemplate() {
        final String emailContent = "email-content";
        when(templateEngine.render(notNull(), notNull())).thenReturn(emailContent);
    }

    private void mockRecoverPasswordEmailPageCreation() {
        final RecoverPasswordEmailPageContent recoverPasswordEmailPageContent = new RecoverPasswordEmailPageContent();
        when(recoverPasswordEmailPageContentFactory.create(customerToken)).thenReturn(recoverPasswordEmailPageContent);
    }

    private void mockCustomerCreatePasswordTokenCommandRequest() {
        when(sphereClient.execute(any()))
                .thenReturn(CompletableFuture.completedFuture(customerToken));
    }

    private DefaultRecoverPasswordFormData formDataWithValidEmailOfExistingCustomer() {
        final DefaultRecoverPasswordFormData recoveryEmailFormData = new DefaultRecoverPasswordFormData();
        recoveryEmailFormData.setEmail("test@example.com");
        return recoveryEmailFormData;
    }
}
