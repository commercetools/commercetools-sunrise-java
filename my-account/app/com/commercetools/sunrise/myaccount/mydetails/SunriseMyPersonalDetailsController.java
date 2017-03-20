package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.mydetails.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.mydetails.viewmodels.MyPersonalDetailsPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyPersonalDetailsController extends SunriseTemplateFormController
        implements MyAccountController, WithTemplateFormFlow<Customer, Customer, MyPersonalDetailsFormData>, WithRequiredCustomer {

    private final MyPersonalDetailsFormData formData;
    private final CustomerFinder customerFinder;
    private final MyPersonalDetailsControllerAction controllerAction;
    private final MyPersonalDetailsPageContentFactory pageContentFactory;

    protected SunriseMyPersonalDetailsController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                                 final MyPersonalDetailsFormData formData, final CustomerFinder customerFinder,
                                                 final MyPersonalDetailsControllerAction controllerAction,
                                                 final MyPersonalDetailsPageContentFactory pageContentFactory) {
        super(templateRenderer, formFactory);
        this.formData = formData;
        this.customerFinder = customerFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends MyPersonalDetailsFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @EnableHooks
    @SunriseRoute(MyPersonalDetailsReverseRouter.MY_PERSONAL_DETAILS_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireCustomer(customer -> showFormPage(customer, formData));
    }

    @EnableHooks
    @SunriseRoute(MyPersonalDetailsReverseRouter.MY_PERSONAL_DETAILS_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireCustomer(this::processForm);
    }

    @Override
    public CompletionStage<Customer> executeAction(final Customer customer, final MyPersonalDetailsFormData formData) {
        return controllerAction.apply(customer, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final MyPersonalDetailsFormData formData);

    @Override
    public PageContent createPageContent(final Customer customer, final Form<? extends MyPersonalDetailsFormData> form) {
        return pageContentFactory.create(customer, form);
    }

    @Override
    public void preFillFormData(final Customer customer, final MyPersonalDetailsFormData formData) {
        formData.applyCustomerName(customer.getName());
        formData.applyEmail(customer.getEmail());
    }
}
