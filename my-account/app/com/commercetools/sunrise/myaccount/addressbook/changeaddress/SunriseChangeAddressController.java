package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AddressBookReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.AddressFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import com.commercetools.sunrise.myaccount.addressbook.WithRequiredAddress;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.view.ChangeAddressPageContentFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

public abstract class SunriseChangeAddressController<F extends AddressBookAddressFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, AddressWithCustomer, Customer>, WithRequiredCustomer, WithRequiredAddress {

    private final CustomerFinder customerFinder;
    private final AddressFinder addressFinder;
    private final ChangeAddressExecutor changeAddressExecutor;
    private final ChangeAddressPageContentFactory changeAddressPageContentFactory;

    protected SunriseChangeAddressController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                             final CustomerFinder customerFinder, final AddressFinder addressFinder,
                                             final ChangeAddressExecutor changeAddressExecutor,
                                             final ChangeAddressPageContentFactory changeAddressPageContentFactory) {
        super(templateRenderer, formFactory);
        this.customerFinder = customerFinder;
        this.addressFinder = addressFinder;
        this.changeAddressExecutor = changeAddressExecutor;
        this.changeAddressPageContentFactory = changeAddressPageContentFactory;
    }

    @Override
    public CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public AddressFinder getAddressFinder() {
        return addressFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(AddressBookReverseRouter.CHANGE_ADDRESS_PAGE)
    public CompletionStage<Result> show(final String languageTag, final String addressId) {
        return requireCustomer(customer ->
                requireAddress(customer, addressId, address ->
                        showFormPage(AddressWithCustomer.of(address, customer))));
    }

    @RunRequestStartedHook
    @SunriseRoute(AddressBookReverseRouter.CHANGE_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return requireCustomer(customer ->
                requireAddress(customer, addressId, address ->
                        processForm(AddressWithCustomer.of(address, customer))));
    }

    @Override
    public CompletionStage<Customer> executeAction(final AddressWithCustomer addressWithCustomer, final F formData) {
        return changeAddressExecutor.apply(addressWithCustomer, formData);
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