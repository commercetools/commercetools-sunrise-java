package com.commercetools.sunrise.myaccount.addressbook.addresslist.view;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AddressBookReverseRouter;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;

@RequestScoped
public class AddressInfoBeanFactory extends ViewModelFactory<AddressInfoBean, Address> {

    private final AddressBeanFactory addressBeanFactory;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public AddressInfoBeanFactory(final AddressBeanFactory addressBeanFactory, final AddressBookReverseRouter addressBookReverseRouter) {
        this.addressBeanFactory = addressBeanFactory;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    protected AddressInfoBean getViewModelInstance() {
        return new AddressInfoBean();
    }

    @Override
    public final AddressInfoBean create(final Address data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final AddressInfoBean model, final Address data) {
        fillAddress(model, data);
        fillAddressEditUrl(model, data);
        fillAddressDeleteUrl(model, data);
    }

    protected void fillAddress(final AddressInfoBean model, final Address address) {
        model.setAddress(addressBeanFactory.create(address));
    }

    protected void fillAddressEditUrl(final AddressInfoBean model, final Address address) {
        model.setAddressEditUrl(addressBookReverseRouter.changeAddressPageCall(address.getId()).url());
    }

    protected void fillAddressDeleteUrl(final AddressInfoBean model, final Address address) {
        model.setAddressDeleteUrl(addressBookReverseRouter.removeAddressProcessCall(address.getId()).url());
    }
}
