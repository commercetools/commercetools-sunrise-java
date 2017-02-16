package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.mydetails.view.MyPersonalDetailsPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

@IntroducingMultiControllerComponents(MyPersonalDetailsThemeLinksControllerComponent.class)
public abstract class SunriseMyPersonalDetailsController<F extends MyPersonalDetailsFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Customer, Customer>, WithRequiredCustomer {

    private final CustomerFinder customerFinder;
    private final MyPersonalDetailsExecutor myPersonalDetailsExecutor;
    private final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory;

    protected SunriseMyPersonalDetailsController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                                 final FormFactory formFactory, final CustomerFinder customerFinder,
                                                 final MyPersonalDetailsExecutor myPersonalDetailsExecutor,
                                                 final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory) {
        super(hookContext, templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.myPersonalDetailsExecutor = myPersonalDetailsExecutor;
        this.myPersonalDetailsPageContentFactory = myPersonalDetailsPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(singletonList("my-personal-details"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-personal-details";
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @SunriseRoute("myPersonalDetailsPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireCustomer(this::showFormPage));
    }

    @SunriseRoute("myPersonalDetailsProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireCustomer(this::processForm));
    }

    @Override
    public CompletionStage<Customer> executeAction(final Customer customer, final F formData) {
        return myPersonalDetailsExecutor.apply(customer, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Customer customer, final Form<F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(customer, form);
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
