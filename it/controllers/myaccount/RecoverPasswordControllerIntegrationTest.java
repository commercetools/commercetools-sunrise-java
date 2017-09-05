package controllers.myaccount;

import com.commercetools.sunrise.it.WithSphereClient;
import com.google.inject.AbstractModule;
import com.commercetools.sunrise.email.EmailDeliveryException;
import com.commercetools.sunrise.email.EmailSender;
import com.commercetools.sunrise.email.MessageEditor;
import io.sphere.sdk.client.SphereClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;

import javax.mail.internet.MimeMessage;

import static com.commercetools.sunrise.it.CustomerTestFixtures.customerDraft;
import static com.commercetools.sunrise.it.CustomerTestFixtures.withCustomer;
import static com.commercetools.sunrise.it.EmailTestFixtures.addressOf;
import static com.commercetools.sunrise.it.EmailTestFixtures.blankMimeMessage;
import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static java.util.Collections.singletonMap;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

@RunWith(MockitoJUnitRunner.class)
public class RecoverPasswordControllerIntegrationTest extends WithSphereClient {

    @Mock
    private EmailSender emailSender;

    @Captor
    private ArgumentCaptor<MessageEditor> messageEditorCaptor;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .overrides(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(SphereClient.class).toInstance(sphereClient);
                        bind(EmailSender.class).toInstance(emailSender);
                    }
                }).build();
    }

    @Test
    public void showsForm() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/en/password/recovery"));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result))
                .containsOnlyOnce("id=\"form-forgot-password\"")
                .contains("action=\"/en/password/recovery\"")
                .contains("name=\"email\"");
    }

    @Test
    public void successfulExecution() throws Exception {
        mockEmailSenderWithSuccessfulOutcome();

        withCustomer(sphereClient, customerDraft(), customerSignInResult -> {
            final String email = customerSignInResult.getCustomer().getEmail();

            final Result result = route(new Http.RequestBuilder()
                    .uri("/en/password/recovery")
                    .method(POST)
                    .bodyForm(singletonMap("email", email)));

            assertThat(result.status()).isEqualTo(SEE_OTHER);
            assertThat(result.header(LOCATION)).contains("/en/password/recovery");

            try {
                final MimeMessage message = blankMimeMessage();
                messageEditorCaptor.getValue().edit(message);

                assertThat(message.getAllRecipients()).containsOnly(addressOf(email));
                assertThat((String) message.getContent()).containsPattern("en\\/password\\/reset\\/[\\w]+");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return customerSignInResult.getCustomer();
        });
    }

    @Test
    public void showsErrorOnNonExistentEmail() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/en/password/recovery")
                .method(POST)
                .bodyForm(singletonMap("email", "non-existent-email@wrong.com")));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Email not found");
    }

    @Test
    public void showsErrorOnEmailDeliveryException() throws Exception {
        mockEmailSenderWithFailedDelivery();

        withCustomer(sphereClient, customerDraft(), customerSignInResult -> {
            final String email = customerSignInResult.getCustomer().getEmail();

            final Result result = route(new Http.RequestBuilder()
                    .uri("/en/password/recovery")
                    .method(POST)
                    .bodyForm(singletonMap("email", email)));

            assertThat(result.status()).isEqualTo(INTERNAL_SERVER_ERROR);
            assertThat(contentAsString(result)).contains("Email delivery error");

            return customerSignInResult.getCustomer();
        });
    }

    private void mockEmailSenderWithSuccessfulOutcome() {
        when(emailSender.send(messageEditorCaptor.capture())).thenReturn(completedFuture("some-email-id"));
    }

    private void mockEmailSenderWithFailedDelivery() {
        when(emailSender.send(messageEditorCaptor.capture())).thenReturn(exceptionallyCompletedFuture(new EmailDeliveryException("Failed sending")));
    }
}
