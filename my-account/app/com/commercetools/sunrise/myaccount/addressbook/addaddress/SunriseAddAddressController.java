package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.SunriseAddressBookManagementController;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@IntroducingMultiControllerComponents(AddAddressThemeLinksControllerComponent.class)
public abstract class SunriseAddAddressController<F extends AddressBookAddressFormData> extends SunriseFrameworkMyAccountController implements WithTemplateName, WithFormFlow<F, Customer, Customer> {

    private final AddAddressExecutor addAddressExecutor;
    private final AddAddressPageContentFactory addAddressPageContentFactory;
    private final CountryCode country;

    protected SunriseAddAddressController(final CustomerFinder customerFinder, final AddAddressExecutor addAddressExecutor,
                                          final AddAddressPageContentFactory addAddressPageContentFactory, final CountryCode country) {
        super(customerFinder);
        this.addAddressExecutor = addAddressExecutor;
        this.addAddressPageContentFactory = addAddressPageContentFactory;
        this.country = country;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("address-book", "add-address", "address"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-new-address";
    }

    @SunriseRoute("addAddressToAddressBookCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireCustomer(this::showForm));
    }

    @SunriseRoute("addAddressToAddressBookProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireCustomer(this::validateForm));
    }

    @Override
    public CompletionStage<? extends Customer> doAction(final F formData, final Customer customer) {
        return addAddressExecutor.addAddress(customer, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Customer customer, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, customer, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Customer oldCustomer, final Customer updatedCustomer);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Customer customer, @Nullable final Customer updatedCustomer) {
        final AddAddressControllerData addAddressControllerData = new AddAddressControllerData(form, customer, updatedCustomer);
        final AddAddressPageContent pageContent = addAddressPageContentFactory.create(addAddressControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final Customer customer) {
        final Address address = Address.of(country)
                .withTitle(customer.getTitle())
                .withFirstName(customer.getFirstName())
                .withLastName(customer.getLastName())
                .withEmail(customer.getEmail());
        formData.applyAddress(address);
    }
}
