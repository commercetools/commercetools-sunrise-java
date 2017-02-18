package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.myaccount.authentication.signup.view.SignUpPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.SphereExceptionUtils.isDuplicatedEmailFieldError;

public abstract class SunriseSignUpController<F extends SignUpFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Void, CustomerSignInResult> {

    private final SignUpActionExecutor signUpActionExecutor;
    private final SignUpPageContentFactory signUpPageContentFactory;

    protected SunriseSignUpController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                      final SignUpActionExecutor signUpActionExecutor,
                                      final SignUpPageContentFactory signUpPageContentFactory) {
        super(templateRenderer, formFactory);
        this.signUpActionExecutor = signUpActionExecutor;
        this.signUpPageContentFactory = signUpPageContentFactory;
    }

    @RunRequestStartedHook
    public CompletionStage<Result> show(final String languageTag) {
        return showFormPage(null);
    }

    @RunRequestStartedHook
    @SunriseRoute("processSignUpForm")
    public CompletionStage<Result> process(final String languageTag) {
        return processForm(null);
    }

    @Override
    public CompletionStage<CustomerSignInResult> executeAction(final Void input, final F formData) {
        return signUpActionExecutor.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<F> form, final ClientErrorException clientErrorException) {
        if (isDuplicatedEmailFieldError(clientErrorException)) {
            saveFormError(form, "A user with this email already exists"); // TODO i18n
            return showFormPageWithErrors(input, form);
        } else {
            return WithTemplateFormFlow.super.handleClientErrorFailedAction(input, form, clientErrorException);
        }
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
}
