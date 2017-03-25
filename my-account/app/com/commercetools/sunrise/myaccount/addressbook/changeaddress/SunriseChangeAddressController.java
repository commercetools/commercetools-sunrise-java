package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressWithCustomer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.WithRequiredCustomer;
import com.commercetools.sunrise.myaccount.addressbook.AddressFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.WithRequiredAddress;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.viewmodels.ChangeAddressPageContentFactory;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

public abstract class SunriseChangeAddressController extends SunriseContentFormController
        implements MyAccountController, WithContentFormFlow<AddressWithCustomer, Customer, AddressFormData>, WithRequiredCustomer, WithRequiredAddress {

    private final AddressFormData formData;
    private final CustomerFinder customerFinder;
    private final AddressFinder addressFinder;
    private final ChangeAddressControllerAction controllerAction;
    private final ChangeAddressPageContentFactory pageContentFactory;

    protected SunriseChangeAddressController(final ContentRenderer contentRenderer,
                                             final FormFactory formFactory, final AddressFormData formData,
                                             final CustomerFinder customerFinder, final AddressFinder addressFinder,
                                             final ChangeAddressControllerAction controllerAction,
                                             final ChangeAddressPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.customerFinder = customerFinder;
        this.addressFinder = addressFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends AddressFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    @Override
    public final AddressFinder getAddressFinder() {
        return addressFinder;
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.CHANGE_ADDRESS_PAGE)
    public CompletionStage<Result> show(final String languageTag, final String addressIdentifier) {
        return requireCustomer(customer ->
                requireAddress(customer, addressIdentifier, address ->
                        showFormPage(AddressWithCustomer.of(address, customer), formData)));
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.CHANGE_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String languageTag, final String addressIdentifier) {
        return requireCustomer(customer ->
                requireAddress(customer, addressIdentifier, address ->
                        processForm(AddressWithCustomer.of(address, customer))));
    }

    @Override
    public CompletionStage<Customer> executeAction(final AddressWithCustomer addressWithCustomer, final AddressFormData formData) {
        return controllerAction.apply(addressWithCustomer, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final AddressFormData formData);

    @Override
    public void preFillFormData(final AddressWithCustomer addressWithCustomer, final AddressFormData formData) {
        final Address address = addressWithCustomer.getAddress();
        final Customer customer = addressWithCustomer.getCustomer();
        formData.applyAddress(address);
        formData.applyDefaultShippingAddress(isDefaultAddress(address, customer.getDefaultShippingAddressId()));
        formData.applyDefaultBillingAddress(isDefaultAddress(address, customer.getDefaultBillingAddressId()));
    }

    @Override
    public PageContent createPageContent(final AddressWithCustomer addressWithCustomer, final Form<? extends AddressFormData> form) {
        return pageContentFactory.create(addressWithCustomer, form);
    }

    private boolean isDefaultAddress(final Address address, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, address.getId());
    }
}