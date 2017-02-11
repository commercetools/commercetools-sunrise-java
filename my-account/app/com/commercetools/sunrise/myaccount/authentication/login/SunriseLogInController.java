package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationPageContent;
import com.commercetools.sunrise.myaccount.authentication.login.view.LogInPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.errors.CustomerInvalidCredentials;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(LogInThemeLinksControllerComponent.class)
public abstract class SunriseLogInController<F extends LogInFormData> extends SunriseFrameworkController implements WithTemplateName, WithFormFlow<F, Void, CustomerSignInResult> {

    private final LogInFunction logInFunction;
    private final LogInPageContentFactory logInPageContentFactory;

    protected SunriseLogInController(final LogInFunction logInFunction, final LogInPageContentFactory logInPageContentFactory) {
        this.logInFunction = logInFunction;
        this.logInPageContentFactory = logInPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account", "log-in", "authentication", "customer", "user"));
    }

    @Override
    public String getTemplateName() {
        return "my-account-login";
    }

    @SunriseRoute("showLogInForm")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> showForm(null));
    }

    @SunriseRoute("processLogInForm")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> validateForm(null));
    }

    @Override
    public CompletionStage<CustomerSignInResult> doAction(final F formData, final Void input) {
        return logInFunction.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Void input, final ClientErrorException clientErrorException) {
        if (isInvalidCredentialsError(clientErrorException)) {
            saveFormError(form, "Invalid credentials"); // TODO i18n
        } else {
            saveUnexpectedFormError(form, clientErrorException);
        }
        return asyncBadRequest(renderPage(form, input, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Void input, final CustomerSignInResult result);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Void input, @Nullable final CustomerSignInResult result) {
        final AuthenticationPageContent pageContent = logInPageContentFactory.create(result, form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final Void input) {
        // Do nothing
    }

    protected final boolean isInvalidCredentialsError(final ClientErrorException clientErrorException) {
        return clientErrorException instanceof ErrorResponseException
                && ((ErrorResponseException) clientErrorException).hasErrorCode(CustomerInvalidCredentials.CODE);
    }
}
