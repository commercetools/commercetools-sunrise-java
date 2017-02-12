package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationPageContent;
import com.commercetools.sunrise.myaccount.authentication.signup.view.SignUpPageContentFactory;
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

@IntroducingMultiControllerComponents(SignUpThemeLinksControllerComponent.class)
public abstract class SunriseSignUpController<F extends SignUpFormData> extends SunriseFrameworkController implements WithTemplateName, WithFormFlow<F, Void, CustomerSignInResult> {

    private final SignUpExecutor signUpExecutor;
    private final SignUpPageContentFactory signUpPageContentFactory;

    protected SunriseSignUpController(final SignUpExecutor signUpExecutor, final SignUpPageContentFactory signUpPageContentFactory) {
        this.signUpExecutor = signUpExecutor;
        this.signUpPageContentFactory = signUpPageContentFactory;
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
        return doRequest(() -> showFormPage(null));
    }

    @SunriseRoute("processSignUpForm")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> processForm(null));
    }

    @Override
    public CompletionStage<CustomerSignInResult> doAction(final F formData, final Void input) {
        return signUpExecutor.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Void input, final ClientErrorException clientErrorException) {
        if (isDuplicatedEmailFieldError(clientErrorException)) {
            saveFormError(form, "A user with this email already exists"); // TODO i18n
        } else {
            saveUnexpectedFormError(form, clientErrorException);
        }
        return asyncBadRequest(renderPage(form, input, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Void input, final CustomerSignInResult result);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Void input, @Nullable final CustomerSignInResult result) {
        final AuthenticationPageContent pageContent = signUpPageContentFactory.create(result, form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final Void input) {
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
