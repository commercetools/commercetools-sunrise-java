package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AuthenticationReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.authentication.signup.viewmodels.SignUpPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.SphereExceptionUtils.isDuplicatedEmailFieldError;

public abstract class SunriseSignUpController extends SunriseTemplateFormController
        implements MyAccountController, WithTemplateFormFlow<SignUpFormData, Void, CustomerSignInResult> {

    private final SignUpFormData formData;
    private final SignUpControllerAction controllerAction;
    private final SignUpPageContentFactory pageContentFactory;

    protected SunriseSignUpController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                      final SignUpFormData formData, final SignUpControllerAction controllerAction,
                                      final SignUpPageContentFactory pageContentFactory) {
        super(templateRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public Class<? extends SignUpFormData> getFormDataClass() {
        return formData.getClass();
    }

    @RunRequestStartedHook
    @SunriseRoute(AuthenticationReverseRouter.SIGN_UP_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return processForm(null);
    }

    @Override
    public CompletionStage<CustomerSignInResult> executeAction(final Void input, final SignUpFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<? extends SignUpFormData> form, final ClientErrorException clientErrorException) {
        if (isDuplicatedEmailFieldError(clientErrorException)) {
            saveFormError(form, "A user with this email already exists"); // TODO i18n
            return showFormPageWithErrors(input, form);
        } else {
            return WithTemplateFormFlow.super.handleClientErrorFailedAction(input, form, clientErrorException);
        }
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final CustomerSignInResult result, final SignUpFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends SignUpFormData> form) {
        return pageContentFactory.create(form);
    }

    @Override
    public void preFillFormData(final Void input, final SignUpFormData formData) {
        // Do not pre-fill anything
    }
}
