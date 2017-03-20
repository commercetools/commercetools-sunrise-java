package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import io.sphere.sdk.models.Address;
import play.mvc.Call;

import javax.inject.Inject;

@RequestScoped
public class EditableAddressViewModelFactory extends SimpleViewModelFactory<EditableAddressViewModel, Address> {

    private final AddressViewModelFactory addressViewModelFactory;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    public EditableAddressViewModelFactory(final AddressViewModelFactory addressViewModelFactory, final AddressBookReverseRouter addressBookReverseRouter) {
        this.addressViewModelFactory = addressViewModelFactory;
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    protected final AddressViewModelFactory getAddressViewModelFactory() {
        return addressViewModelFactory;
    }

    protected final AddressBookReverseRouter getAddressBookReverseRouter() {
        return addressBookReverseRouter;
    }

    @Override
    protected EditableAddressViewModel newViewModelInstance(final Address address) {
        return new EditableAddressViewModel();
    }

    @Override
    public final EditableAddressViewModel create(final Address address) {
        return super.create(address);
    }

    @Override
    protected final void initialize(final EditableAddressViewModel viewModel, final Address address) {
        fillAddress(viewModel, address);
        fillAddressEditUrl(viewModel, address);
        fillAddressDeleteUrl(viewModel, address);
    }

    protected void fillAddress(final EditableAddressViewModel viewModel, final Address address) {
        viewModel.setAddress(addressViewModelFactory.create(address));
    }

    protected void fillAddressEditUrl(final EditableAddressViewModel viewModel, final Address address) {
        viewModel.setAddressEditUrl(addressBookReverseRouter.changeAddressPageCall(address).map(Call::url).orElse(""));
    }

    protected void fillAddressDeleteUrl(final EditableAddressViewModel viewModel, final Address address) {
        viewModel.setAddressDeleteUrl(addressBookReverseRouter.removeAddressProcessCall(address).map(Call::url).orElse(""));
    }
}
