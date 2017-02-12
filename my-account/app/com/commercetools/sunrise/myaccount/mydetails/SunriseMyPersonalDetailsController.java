package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.SunriseFrameworkMyAccountController;
import com.commercetools.sunrise.myaccount.mydetails.view.MyPersonalDetailsPageContent;
import com.commercetools.sunrise.myaccount.mydetails.view.MyPersonalDetailsPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@IntroducingMultiControllerComponents(MyPersonalDetailsThemeLinksControllerComponent.class)
public abstract class SunriseMyPersonalDetailsController<F extends MyPersonalDetailsFormData> extends SunriseFrameworkMyAccountController implements WithTemplateName, WithFormFlow<F, Customer, Customer> {

    private final MyPersonalDetailsExecutor myPersonalDetailsExecutor;
    private final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory;

    protected SunriseMyPersonalDetailsController(final CustomerFinder customerFinder, final MyPersonalDetailsExecutor myPersonalDetailsExecutor,
                                                 final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory) {
        super(customerFinder);
        this.myPersonalDetailsExecutor = myPersonalDetailsExecutor;
        this.myPersonalDetailsPageContentFactory = myPersonalDetailsPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(singletonList("my-personal-details"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-personal-details";
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
    public CompletionStage<Customer> doAction(final F formData, final Customer customer) {
        return myPersonalDetailsExecutor.apply(customer, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Customer customer, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, customer, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Customer oldCustomer, final Customer updatedCustomer);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Customer oldCustomer, @Nullable final Customer updatedCustomer) {
        final MyPersonalDetailsPageContent pageContent = myPersonalDetailsPageContentFactory.create(firstNonNull(updatedCustomer, oldCustomer), form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final Customer customer) {
        formData.applyCustomerName(customer.getName());
        formData.setEmail(customer.getEmail());
    }
}
