package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
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
import play.twirl.api.Content;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(LogInThemeLinksControllerComponent.class)
public abstract class SunriseLogInController<F extends LogInFormData> extends SunriseFrameworkController implements WithTemplateName, WithFormFlow<F, Void, CustomerSignInResult> {

    private final LogInExecutor logInExecutor;
    private final LogInPageContentFactory logInPageContentFactory;

    protected SunriseLogInController(final LogInExecutor logInExecutor, final LogInPageContentFactory logInPageContentFactory) {
        this.logInExecutor = logInExecutor;
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
        return doRequest(() -> showFormPage(null));
    }

    @SunriseRoute("processLogInForm")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> processForm(null));
    }

    @Override
    public CompletionStage<CustomerSignInResult> executeAction(final Void input, final F formData) {
        return logInExecutor.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<F> form, final ClientErrorException clientErrorException) {
        if (isInvalidCredentialsError(clientErrorException)) {
            saveFormError(form, "Invalid credentials"); // TODO i18n
        } else {
            saveUnexpectedFormError(form, clientErrorException);
        }
        return asyncBadRequest(renderPage(form, input));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Void input, final F formData, final CustomerSignInResult result);

    @Override
    public CompletionStage<Content> renderPage(final Form<F> form, final Void input) {
        final AuthenticationPageContent pageContent = logInPageContentFactory.create(form);
        return renderContent(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final Void input, final F formData) {
        // Do nothing
    }

    protected final boolean isInvalidCredentialsError(final ClientErrorException clientErrorException) {
        return clientErrorException instanceof ErrorResponseException
                && ((ErrorResponseException) clientErrorException).hasErrorCode(CustomerInvalidCredentials.CODE);
    }
}
