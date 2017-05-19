package com.commercetools.sunrise.myaccount.authentication.changepassword;

import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.changepassword.ChangePasswordReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.authentication.changepassword.viemodels.ChangePasswordPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.ctp.CtpExceptionUtils.isCustomerInvalidCurrentPasswordError;

public abstract class SunriseChangePasswordController extends SunriseContentFormController
        implements MyAccountController, WithContentFormFlow<Customer, Customer, ChangePasswordFormData>, WithRequiredCustomer {

    private final ChangePasswordFormData formData;
    private final ChangePasswordControllerAction controllerAction;
    private final ChangePasswordPageContentFactory pageContentFactory;
    private final CustomerFinder customerFinder;

    protected SunriseChangePasswordController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                              final ChangePasswordFormData formData, final CustomerFinder customerFinder,
                                              final ChangePasswordControllerAction controllerAction,
                                              final ChangePasswordPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.customerFinder = customerFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @EnableHooks
    @SunriseRoute(ChangePasswordReverseRouter.CHANGE_PASSWORD_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return showFormPage(null, formData);
    }

    @EnableHooks
    @SunriseRoute(ChangePasswordReverseRouter.CHANGE_PASSWORD_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireCustomer(this::processForm);
    }

    @Override
    public final CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public final Class<? extends ChangePasswordFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public CompletionStage<Customer> executeAction(final Customer input, final ChangePasswordFormData formData) {
        return controllerAction.apply(input, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Customer input, final Form<? extends ChangePasswordFormData> form, final ClientErrorException clientErrorException) {
        if (isCustomerInvalidCurrentPasswordError(clientErrorException)) {
            saveFormError(form, "Invalid current password"); // TODO i18n
            return showFormPageWithErrors(input, form);
        } else {
            return WithContentFormFlow.super.handleClientErrorFailedAction(input, form, clientErrorException);
        }
    }

    @Override
    public PageContent createPageContent(final Customer input, final Form<? extends ChangePasswordFormData> form) {
        return pageContentFactory.create(form);
    }

    @Override
    public void preFillFormData(final Customer input, final ChangePasswordFormData formData) {
        // Do nothing
    }
}
