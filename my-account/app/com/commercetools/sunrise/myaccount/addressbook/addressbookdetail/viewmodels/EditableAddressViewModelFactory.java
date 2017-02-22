package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.models.addresses.AddressViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AddressBookReverseRouter;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;

@RequestScoped
public class EditableAddressViewModelFactory extends ViewModelFactory<EditableAddressViewModel, Address> {

    private final AddressViewModelFactory addressViewModelFactory;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public EditableAddressViewModelFactory(final AddressViewModelFactory addressViewModelFactory, final AddressBookReverseRouter addressBookReverseRouter) {
        this.addressViewModelFactory = addressViewModelFactory;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    @Override
    protected EditableAddressViewModel getViewModelInstance() {
        return new EditableAddressViewModel();
    }

    @Override
    public final EditableAddressViewModel create(final Address data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final EditableAddressViewModel model, final Address data) {
        fillAddress(model, data);
        fillAddressEditUrl(model, data);
        fillAddressDeleteUrl(model, data);
    }

    protected void fillAddress(final EditableAddressViewModel model, final Address address) {
        model.setAddress(addressViewModelFactory.create(address));
    }

    protected void fillAddressEditUrl(final EditableAddressViewModel model, final Address address) {
        model.setAddressEditUrl(addressBookReverseRouter.changeAddressPageCall(address.getId()).url());
    }

    protected void fillAddressDeleteUrl(final EditableAddressViewModel model, final Address address) {
        model.setAddressDeleteUrl(addressBookReverseRouter.removeAddressProcessCall(address.getId()).url());
    }
}
