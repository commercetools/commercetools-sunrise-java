package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.contexts.RequestScoped;
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
import io.sphere.sdk.models.errors.DuplicateFieldError;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@RequestScoped
@IntroducingMultiControllerComponents(SignUpThemeLinksControllerComponent.class)
public abstract class SunriseSignUpController<F extends SignUpFormData> extends SunriseFrameworkController implements WithTemplateName, WithFormFlow<F, Void, CustomerSignInResult> {

    private final CustomerCreator customerCreator;
    private final AuthenticationPageContentFactory authenticationPageContentFactory;

    protected SunriseSignUpController(final CustomerCreator customerCreator, final AuthenticationPageContentFactory authenticationPageContentFactory) {
        this.customerCreator = customerCreator;
        this.authenticationPageContentFactory = authenticationPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account", "sign-up", "authentication", "customer", "user"));
    }

    @Override
    public String getTemplateName() {
        return "my-account-login";
    }

    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> showForm(null));
    }

    @SunriseRoute("processSignUpForm")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> validateForm(null));
    }

    @Override
    public CompletionStage<? extends CustomerSignInResult> doAction(final F formData, final Void context) {
        return customerCreator.createCustomer(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Void context, final ClientErrorException clientErrorException) {
        if (isDuplicatedEmailFieldError(clientErrorException)) {
            saveFormError(form, "A user with this email already exists"); // TODO i18n
        } else {
            saveUnexpectedFormError(form, clientErrorException);
        }
        return asyncBadRequest(renderPage(form, context, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Void context, final CustomerSignInResult result);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Void context, @Nullable final CustomerSignInResult result) {
        final AuthenticationControllerData authenticationControllerData = new AuthenticationControllerData(form, null, result);
        final AuthenticationPageContent pageContent = authenticationPageContentFactory.create(authenticationControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void fillFormData(final F formData, final Void context) {
        // Do not pre-fill anything
    }

    protected final boolean isDuplicatedEmailFieldError(final ClientErrorException clientErrorException) {
        return clientErrorException instanceof ErrorResponseException
                && ((ErrorResponseException) clientErrorException).getErrors().stream()
                    .filter(error -> error.getCode().equals(DuplicateFieldError.CODE))
                    .map(error -> error.as(DuplicateFieldError.class).getField())
                    .anyMatch(duplicatedField -> duplicatedField != null && duplicatedField.equals("email"));
    }
}
