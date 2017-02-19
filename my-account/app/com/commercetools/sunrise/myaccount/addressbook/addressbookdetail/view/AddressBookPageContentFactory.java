package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.view;

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
    private final AddressInfoBeanFactory addressInfoBeanFactory;

    @Inject
    public AddressBookPageContentFactory(final PageTitleResolver pageTitleResolver, final AddressInfoBeanFactory addressInfoBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.addressInfoBeanFactory = addressInfoBeanFactory;
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
    protected final void initialize(final AddressBookPageContent model, final Customer customer) {
        super.initialize(model, customer);
        fillDefaultShippingAddress(model, customer);
        fillDefaultBillingAddress(model, customer);
        fillAddresses(model, customer);
    }

    @Override
    protected void fillTitle(final AddressBookPageContent model, final Customer customer) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:addressBookPage.title"));
    }

    protected void fillDefaultShippingAddress(final AddressBookPageContent model, final Customer customer) {
        customer.findDefaultShippingAddress()
                .ifPresent(address -> model.setDefaultShippingAddress(addressInfoBeanFactory.create(address)));
    }

    protected void fillDefaultBillingAddress(final AddressBookPageContent model, final Customer customer) {
        customer.findDefaultBillingAddress()
                .ifPresent(address -> model.setDefaultBillingAddress(addressInfoBeanFactory.create(address)));
    }

    protected void fillAddresses(final AddressBookPageContent model, final Customer customer) {
        final List<AddressInfoBean> beanList = customer.getAddresses().stream()
                .filter(address -> isNotAnyDefaultAddress(customer, address))
                .map(addressInfoBeanFactory::create)
                .collect(Collectors.toList());
        model.setAddresses(beanList);
    }

    private boolean isNotAnyDefaultAddress(final Customer customer, final Address address) {
        final boolean isNotDefaultShipping = !Objects.equals(address.getId(), customer.getDefaultShippingAddressId());
        final boolean isNotDefaultBilling = !Objects.equals(address.getId(), customer.getDefaultBillingAddressId());
        return isNotDefaultShipping && isNotDefaultBilling;
    }

}