package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.authentication.login.viewmodels.LogInPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.ctp.CtpExceptionUtils.isCustomerInvalidCredentialsError;

public abstract class SunriseLogInController extends SunriseTemplateFormController
        implements MyAccountController, WithTemplateFormFlow<Void, CustomerSignInResult, LogInFormData> {

    private final LogInFormData formData;
    private final LogInControllerAction controllerAction;
    private final LogInPageContentFactory pageContentFactory;

    protected SunriseLogInController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                     final LogInFormData formData, final LogInControllerAction controllerAction,
                                     final LogInPageContentFactory pageContentFactory) {
        super(templateRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends LogInFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(AuthenticationReverseRouter.LOG_IN_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return showFormPage(null, formData);
    }

    @EnableHooks
    @SunriseRoute(AuthenticationReverseRouter.LOG_IN_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return processForm(null);
    }

    @Override
    public CompletionStage<CustomerSignInResult> executeAction(final Void input, final LogInFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<? extends LogInFormData> form, final ClientErrorException clientErrorException) {
        if (isCustomerInvalidCredentialsError(clientErrorException)) {
            saveFormError(form, "Invalid credentials"); // TODO i18n
            return showFormPageWithErrors(input, form);
        } else {
            return WithTemplateFormFlow.super.handleClientErrorFailedAction(input, form, clientErrorException);
        }
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final CustomerSignInResult result, final LogInFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends LogInFormData> form) {
        return pageContentFactory.create(form);
    }

    @Override
    public void preFillFormData(final Void input, final LogInFormData formData) {
        // Do nothing
    }
}
