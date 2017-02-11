package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.view.AddAddressPageContent;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.view.AddAddressPageContentFactory;
import com.commercetools.sunrise.myaccount.SunriseFrameworkMyAccountController;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@IntroducingMultiControllerComponents(AddAddressThemeLinksControllerComponent.class)
public abstract class SunriseAddAddressController<F extends AddressBookAddressFormData> extends SunriseFrameworkMyAccountController implements WithTemplateName, WithFormFlow<F, Customer, Customer> {

    private final AddAddressFunction addAddressFunction;
    private final AddAddressPageContentFactory addAddressPageContentFactory;
    private final CountryCode country;

    protected SunriseAddAddressController(final CustomerFinder customerFinder, final AddAddressFunction addAddressFunction,
                                          final AddAddressPageContentFactory addAddressPageContentFactory, final CountryCode country) {
        super(customerFinder);
        this.addAddressFunction = addAddressFunction;
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
    public CompletionStage<Customer> doAction(final F formData, final Customer customer) {
        return addAddressFunction.apply(customer, formData);
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
        final AddAddressPageContent pageContent = addAddressPageContentFactory.create(firstNonNull(updatedCustomer, customer), form);
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
