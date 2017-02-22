package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddressBookPageContentFactory extends PageContentFactory<AddressBookPageContent, Customer> {

    private final PageTitleResolver pageTitleResolver;
    private final EditableAddressViewModelFactory editableAddressViewModelFactory;

    @Inject
    public AddressBookPageContentFactory(final PageTitleResolver pageTitleResolver, final EditableAddressViewModelFactory editableAddressViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.editableAddressViewModelFactory = editableAddressViewModelFactory;
    }

    @Override
    protected AddressBookPageContent getViewModelInstance() {
        return new AddressBookPageContent();
    }

    @Override
    public final AddressBookPageContent create(final Customer customer) {
        return super.create(customer);
    }

    @Override
    protected final void initialize(final AddressBookPageContent viewModel, final Customer customer) {
        super.initialize(viewModel, customer);
        fillDefaultShippingAddress(viewModel, customer);
        fillDefaultBillingAddress(viewModel, customer);
        fillAddresses(viewModel, customer);
    }

    @Override
    protected void fillTitle(final AddressBookPageContent model, final Customer customer) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:addressBookPage.title"));
    }

    protected void fillDefaultShippingAddress(final AddressBookPageContent model, final Customer customer) {
        customer.findDefaultShippingAddress()
                .ifPresent(address -> model.setDefaultShippingAddress(editableAddressViewModelFactory.create(address)));
    }

    protected void fillDefaultBillingAddress(final AddressBookPageContent model, final Customer customer) {
        customer.findDefaultBillingAddress()
                .ifPresent(address -> model.setDefaultBillingAddress(editableAddressViewModelFactory.create(address)));
    }

    protected void fillAddresses(final AddressBookPageContent model, final Customer customer) {
        final List<EditableAddressViewModel> modelList = customer.getAddresses().stream()
                .filter(address -> isNotAnyDefaultAddress(customer, address))
                .map(editableAddressViewModelFactory::create)
                .collect(Collectors.toList());
        model.setAddresses(modelList);
    }

    private boolean isNotAnyDefaultAddress(final Customer customer, final Address address) {
        final boolean isNotDefaultShipping = !Objects.equals(address.getId(), customer.getDefaultShippingAddressId());
        final boolean isNotDefaultBilling = !Objects.equals(address.getId(), customer.getDefaultBillingAddressId());
        return isNotDefaultShipping && isNotDefaultBilling;
    }

}