package com.commercetools.sunrise.myaccount.addressbook.addresslist;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestScoped
public class AddressBookPageContentFactory extends PageContentFactory<AddressBookPageContent, AddressBookControllerData> {

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
    public final AddressBookPageContent create(final AddressBookControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final AddressBookPageContent model, final AddressBookControllerData data) {
        super.initialize(model, data);
        fillDefaultShippingAddress(model, data);
        fillDefaultBillingAddress(model, data);
        fillAddresses(model, data);
    }

    @Override
    protected void fillTitle(final AddressBookPageContent model, final AddressBookControllerData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("myAccount:addressBookPage.title"));
    }

    protected void fillDefaultShippingAddress(final AddressBookPageContent model, final AddressBookControllerData data) {
        data.getCustomer().findDefaultShippingAddress()
                .ifPresent(address -> model.setDefaultShippingAddress(addressInfoBeanFactory.create(address)));
    }

    protected void fillDefaultBillingAddress(final AddressBookPageContent model, final AddressBookControllerData data) {
        data.getCustomer().findDefaultBillingAddress()
                .ifPresent(address -> model.setDefaultBillingAddress(addressInfoBeanFactory.create(address)));
    }

    protected void fillAddresses(final AddressBookPageContent model, final AddressBookControllerData data) {
        final List<AddressInfoBean> beanList = data.getCustomer().getAddresses().stream()
                .filter(address -> isNotAnyDefaultAddress(data.getCustomer(), address))
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