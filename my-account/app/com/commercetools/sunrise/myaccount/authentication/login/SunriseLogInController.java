package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationControllerData;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationPageContent;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationPageContentFactory;
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

    private final LogInExecutor logInExecutor;
    private final AuthenticationPageContentFactory authenticationPageContentFactory;

    protected SunriseLogInController(final LogInExecutor logInExecutor, final AuthenticationPageContentFactory authenticationPageContentFactory) {
        this.logInExecutor = logInExecutor;
        this.authenticationPageContentFactory = authenticationPageContentFactory;
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
    public CompletionStage<CustomerSignInResult> doAction(final F formData, final Void context) {
        return logInExecutor.logIn(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Void context, final ClientErrorException clientErrorException) {
        if (isInvalidCredentialsError(clientErrorException)) {
            saveFormError(form, "Invalid credentials"); // TODO i18n
        } else {
            saveUnexpectedFormError(form, clientErrorException);
        }
        return asyncBadRequest(renderPage(form, context, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Void context, final CustomerSignInResult result);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Void context, @Nullable final CustomerSignInResult result) {
        final AuthenticationControllerData authenticationControllerData = new AuthenticationControllerData(null, form, result);
        final AuthenticationPageContent pageContent = authenticationPageContentFactory.create(authenticationControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void fillFormData(final F formData, final Void context) {
        // Do nothing
    }

    protected final boolean isInvalidCredentialsError(final ClientErrorException clientErrorException) {
        return clientErrorException instanceof ErrorResponseException
                && ((ErrorResponseException) clientErrorException).hasErrorCode(CustomerInvalidCredentials.CODE);
    }
}
