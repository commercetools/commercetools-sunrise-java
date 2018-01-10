package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.changepassword.ChangePasswordReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import com.commercetools.sunrise.myaccount.MyAccountController;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sdk.errors.ErrorResponseExceptionUtils.isCustomerInvalidCurrentPasswordError;

public abstract class SunriseChangePasswordController extends SunriseContentFormController
        implements MyAccountController, WithContentFormFlow<Void, Customer, ChangePasswordFormData> {

    private final ChangePasswordFormData formData;
    private final ChangePasswordControllerAction controllerAction;

    protected SunriseChangePasswordController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                              final ChangePasswordFormData formData,
                                              final ChangePasswordControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(ChangePasswordReverseRouter.CHANGE_PASSWORD_PAGE)
    public CompletionStage<Result> show() {
        return showFormPage(null, formData);
    }

    @EnableHooks
    @SunriseRoute(ChangePasswordReverseRouter.CHANGE_PASSWORD_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public final Class<? extends ChangePasswordFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public CompletionStage<Customer> executeAction(final Void input, final ChangePasswordFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<? extends ChangePasswordFormData> form, final ClientErrorException clientErrorException) {
        if (isCustomerInvalidCurrentPasswordError(clientErrorException)) {
            saveFormError(form, "Invalid current password"); // TODO i18n
            return showFormPageWithErrors(input, form);
        } else {
            return WithContentFormFlow.super.handleClientErrorFailedAction(input, form, clientErrorException);
        }
    }

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends ChangePasswordFormData> form) {
        return new BlankPageContent();
    }

    @Override
    public void preFillFormData(final Void input, final ChangePasswordFormData formData) {
        // Do nothing
    }
}
