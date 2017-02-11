package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

@IntroducingMultiControllerComponents(MyPersonalDetailsThemeLinksControllerComponent.class)
public abstract class SunriseMyPersonalDetailsController<F extends MyPersonalDetailsFormData> extends SunriseFrameworkMyAccountController implements WithTemplateName, WithFormFlow<F, Customer, Customer> {

    private final MyPersonalDetailsUpdater myPersonalDetailsUpdater;
    private final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory;

    protected SunriseMyPersonalDetailsController(final CustomerFinder customerFinder, final MyPersonalDetailsUpdater myPersonalDetailsUpdater,
                                                 final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory) {
        super(customerFinder);
        this.myPersonalDetailsUpdater = myPersonalDetailsUpdater;
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
        return doRequest(() -> requireCustomer(this::showForm));
    }

    @SunriseRoute("myPersonalDetailsProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireCustomer(this::validateForm));
    }

    @Override
    public CompletionStage<? extends Customer> doAction(final F formData, final Customer customer) {
        return myPersonalDetailsUpdater.updateCustomer(customer, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Customer customer, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, customer, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Customer customer, final Customer updatedCustomer);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Customer customer, @Nullable final Customer updatedCustomer) {
        final MyPersonalDetailsControllerData myPersonalDetailsControllerData = new MyPersonalDetailsControllerData(form, customer, updatedCustomer);
        final MyPersonalDetailsPageContent pageContent = myPersonalDetailsPageContentFactory.create(myPersonalDetailsControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final Customer customer) {
        formData.applyCustomerName(customer.getName());
        formData.setEmail(customer.getEmail());
    }
}
