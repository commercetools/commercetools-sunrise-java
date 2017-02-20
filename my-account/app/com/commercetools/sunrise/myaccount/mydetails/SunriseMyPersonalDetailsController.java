package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.MyPersonalDetailsReverseRouter;
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

public abstract class SunriseMyPersonalDetailsController<F extends MyPersonalDetailsFormData> extends SunriseTemplateFormController
        implements MyAccountController, WithTemplateFormFlow<F, Customer, Customer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final MyPersonalDetailsControllerAction myPersonalDetailsControllerAction;
    private final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory;

    protected SunriseMyPersonalDetailsController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                                 final CustomerFinder customerFinder,
                                                 final MyPersonalDetailsControllerAction myPersonalDetailsControllerAction,
                                                 final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory) {
        super(templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.myPersonalDetailsControllerAction = myPersonalDetailsControllerAction;
        this.myPersonalDetailsPageContentFactory = myPersonalDetailsPageContentFactory;
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(MyPersonalDetailsReverseRouter.MY_PERSONAL_DETAILS_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireCustomer(this::showFormPage);
    }

    @RunRequestStartedHook
    @SunriseRoute(MyPersonalDetailsReverseRouter.MY_PERSONAL_DETAILS_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireCustomer(this::processForm);
    }

    @Override
    public CompletionStage<Customer> executeAction(final Customer customer, final F formData) {
        return myPersonalDetailsControllerAction.apply(customer, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final F formData);

    @Override
    public PageContent createPageContent(final Customer customer, final Form<F> form) {
        return myPersonalDetailsPageContentFactory.create(customer, form);
    }

    @Override
    public void preFillFormData(final Customer customer, final F formData) {
        formData.applyCustomerName(customer.getName());
        formData.setEmail(customer.getEmail());
    }
}
