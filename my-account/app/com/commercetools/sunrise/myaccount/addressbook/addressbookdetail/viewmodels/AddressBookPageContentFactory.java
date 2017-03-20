package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
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

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    protected final EditableAddressViewModelFactory getEditableAddressViewModelFactory() {
        return editableAddressViewModelFactory;
    }

    @Override
    protected AddressBookPageContent newViewModelInstance(final Customer customer) {
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
    protected void fillTitle(final AddressBookPageContent viewModel, final Customer customer) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("myAccount:addressBookPage.title"));
    }

    protected void fillDefaultShippingAddress(final AddressBookPageContent viewModel, final Customer customer) {
        customer.findDefaultShippingAddress()
                .ifPresent(address -> viewModel.setDefaultShippingAddress(editableAddressViewModelFactory.create(address)));
    }

    protected void fillDefaultBillingAddress(final AddressBookPageContent viewModel, final Customer customer) {
        customer.findDefaultBillingAddress()
                .ifPresent(address -> viewModel.setDefaultBillingAddress(editableAddressViewModelFactory.create(address)));
    }

    protected void fillAddresses(final AddressBookPageContent viewModel, final Customer customer) {
        viewModel.setAddresses(customer.getAddresses().stream()
                .filter(address -> isNotAnyDefaultAddress(customer, address))
                .map(editableAddressViewModelFactory::create)
                .collect(Collectors.toList()));
    }

    private boolean isNotAnyDefaultAddress(final Customer customer, final Address address) {
        final boolean isNotDefaultShipping = !Objects.equals(address.getId(), customer.getDefaultShippingAddressId());
        final boolean isNotDefaultBilling = !Objects.equals(address.getId(), customer.getDefaultBillingAddressId());
        return isNotDefaultShipping && isNotDefaultBilling;
    }

}