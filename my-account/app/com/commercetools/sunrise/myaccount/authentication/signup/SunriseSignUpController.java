package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.controllers.SunriseFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.myaccount.authentication.signup.view.SignUpPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.models.errors.DuplicateFieldError;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(SignUpThemeLinksControllerComponent.class)
public abstract class SunriseSignUpController<F extends SignUpFormData> extends SunriseFormController implements WithFormFlow<F, Void, CustomerSignInResult> {

    private final SignUpExecutor signUpExecutor;
    private final SignUpPageContentFactory signUpPageContentFactory;

    protected SunriseSignUpController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                      final FormFactory formFactory, final SignUpExecutor signUpExecutor,
                                      final SignUpPageContentFactory signUpPageContentFactory) {
        super(hookContext, templateRenderer, formFactory);
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
    public CompletionStage<CustomerSignInResult> executeAction(final Void input, final F formData) {
        return signUpExecutor.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<F> form, final ClientErrorException clientErrorException) {
        if (isDuplicatedEmailFieldError(clientErrorException)) {
            saveFormError(form, "A user with this email already exists"); // TODO i18n
        } else {
            saveUnexpectedFormError(form, clientErrorException);
        }
        return showFormPageWithErrors(input, form);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final CustomerSignInResult result, final F formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<F> form) {
        return signUpPageContentFactory.create(form);
    }

    @Override
    public void preFillFormData(final Void input, final F formData) {
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
