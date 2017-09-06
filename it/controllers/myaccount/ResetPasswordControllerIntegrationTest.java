package controllers.myaccount;

import com.commercetools.sunrise.it.WithSphereClient;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static com.commercetools.sunrise.it.CustomerTestFixtures.customerDraft;
import static com.commercetools.sunrise.it.CustomerTestFixtures.withCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class ResetPasswordControllerIntegrationTest extends WithSphereClient {

    @Test
    public void showsForm() throws Exception {
        final Result result = route(new Http.RequestBuilder()
                .uri("/en/password/reset/any-token"));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(contentAsString(result))
                .containsOnlyOnce("id=\"form-change-password\"")
                .contains("action=\"/en/password/reset/any-token\"")
                .contains("name=\"newPassword\"")
                .contains("name=\"confirmPassword\"");
    }

    @Test
    public void successfulExecution() throws Exception {
        withCustomer(sphereClient, customerDraft(), customerSignInResult -> {
            final String email = customerSignInResult.getCustomer().getEmail();
            final CustomerToken customerToken = sphereClient.executeBlocking(CustomerCreatePasswordTokenCommand.of(email));

            final Map<String, String> bodyForm = new HashMap<>();
            bodyForm.put("newPassword", "1234");
            bodyForm.put("confirmPassword", "1234");
            final Result result = route(new Http.RequestBuilder()
                    .uri("/en/password/reset/" + customerToken.getValue())
                    .method(POST)
                    .bodyForm(bodyForm));

            assertThat(result.status()).isEqualTo(SEE_OTHER);
            assertThat(result.header(LOCATION)).contains("/en/user/login");

            return customerSignInResult.getCustomer();
        });
    }

    @Test
    public void showsErrorOnNonExistentToken() throws Exception {
        final Map<String, String> bodyForm = new HashMap<>();
        bodyForm.put("newPassword", "1234");
        bodyForm.put("confirmPassword", "1234");
        final Result result = route(new Http.RequestBuilder()
                .uri("/en/password/reset/non-existing-token")
                .method(POST)
                .bodyForm(bodyForm));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).contains("Reset token is not valid");
    }
}
