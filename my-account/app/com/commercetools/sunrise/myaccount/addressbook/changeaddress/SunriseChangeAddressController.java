package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.common.controllers.SunriseFrameworkFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithCustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.AddressFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import com.commercetools.sunrise.myaccount.addressbook.WithAddressFinder;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.view.ChangeAddressPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

public abstract class SunriseChangeAddressController<F extends AddressBookAddressFormData> extends SunriseFrameworkFormController implements WithFormFlow<F, AddressWithCustomer, Customer>, WithCustomerFinder, WithAddressFinder {

    private final CustomerFinder customerFinder;
    private final AddressFinder addressFinder;
    private final ChangeAddressExecutor changeAddressExecutor;
    private final ChangeAddressPageContentFactory changeAddressPageContentFactory;

    protected SunriseChangeAddressController(final TemplateRenderer templateRenderer, final RequestHookContext hookContext,
                                             final FormFactory formFactory, final CustomerFinder customerFinder,
                                             final AddressFinder addressFinder, final ChangeAddressExecutor changeAddressExecutor,
                                             final ChangeAddressPageContentFactory changeAddressPageContentFactory) {
        super(templateRenderer, hookContext, formFactory);
        this.customerFinder = customerFinder;
        this.addressFinder = addressFinder;
        this.changeAddressExecutor = changeAddressExecutor;
        this.changeAddressPageContentFactory = changeAddressPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("address-book", "change-address", "address"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-edit-address";
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public AddressFinder getAddressFinder() {
        return addressFinder;
    }

    @SunriseRoute("changeAddressInAddressBookCall")
    public CompletionStage<Result> show(final String languageTag, final String addressId) {
        return doRequest(() ->
                requireCustomer(customer ->
                        requireAddress(customer, addressId, address ->
                                showFormPage(AddressWithCustomer.of(address, customer)))));
    }

    @SunriseRoute("changeAddressInAddressBookProcessFormCall")
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return doRequest(() ->
                requireCustomer(customer ->
                        requireAddress(customer, addressId, address ->
                                processForm(AddressWithCustomer.of(address, customer)))));
    }

    @Override
    public CompletionStage<Customer> executeAction(final AddressWithCustomer addressWithCustomer, final F formData) {
        return changeAddressExecutor.apply(addressWithCustomer, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final AddressWithCustomer addressWithCustomer, final Form<F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(addressWithCustomer, form);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final F formData);

    @Override
    public void preFillFormData(final AddressWithCustomer addressWithCustomer, final F formData) {
        final Address address = addressWithCustomer.getAddress();
        final Customer customer = addressWithCustomer.getCustomer();
        formData.applyAddress(address);
        formData.setDefaultShippingAddress(isDefaultAddress(address, customer.getDefaultShippingAddressId()));
        formData.setDefaultBillingAddress(isDefaultAddress(address, customer.getDefaultBillingAddressId()));
    }

    @Override
    public PageContent createPageContent(final AddressWithCustomer addressWithCustomer, final Form<F> form) {
        return changeAddressPageContentFactory.create(addressWithCustomer, form);
    }

    private boolean isDefaultAddress(final Address address, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, address.getId());
    }
}