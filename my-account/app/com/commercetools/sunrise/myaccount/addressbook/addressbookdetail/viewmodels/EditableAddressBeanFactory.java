package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.models.addresses.AddressBeanFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AddressBookReverseRouter;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;

@RequestScoped
public class EditableAddressBeanFactory extends ViewModelFactory<EditableAddressBean, Address> {

    private final AddressBeanFactory addressBeanFactory;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public EditableAddressBeanFactory(final AddressBeanFactory addressBeanFactory, final AddressBookReverseRouter addressBookReverseRouter) {
        this.addressBeanFactory = addressBeanFactory;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    protected EditableAddressBean getViewModelInstance() {
        return new EditableAddressBean();
    }

    @Override
    public final EditableAddressBean create(final Address data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final EditableAddressBean model, final Address data) {
        fillAddress(model, data);
        fillAddressEditUrl(model, data);
        fillAddressDeleteUrl(model, data);
    }

    protected void fillAddress(final EditableAddressBean model, final Address address) {
        model.setAddress(addressBeanFactory.create(address));
    }

    protected void fillAddressEditUrl(final EditableAddressBean model, final Address address) {
        model.setAddressEditUrl(addressBookReverseRouter.changeAddressPageCall(address.getId()).url());
    }

    protected void fillAddressDeleteUrl(final EditableAddressBean model, final Address address) {
        model.setAddressDeleteUrl(addressBookReverseRouter.removeAddressProcessCall(address.getId()).url());
    }
}
