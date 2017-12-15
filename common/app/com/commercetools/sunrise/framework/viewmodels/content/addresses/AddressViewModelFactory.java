package com.commercetools.sunrise.framework.viewmodels.content.addresses;

import com.commercetools.sunrise.framework.i18n.I18nResolver;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddressViewModelFactory extends SimpleViewModelFactory<AddressViewModel, Address> {

    private final I18nResolver i18nResolver;

    @Inject
    public AddressViewModelFactory(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    protected final I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    @Override
    protected AddressViewModel newViewModelInstance(final Address address) {
        return new AddressViewModel();
    }

    @Override
    public final AddressViewModel create(final Address address) {
        return super.create(address);
    }

    @Override
    protected final void initialize(final AddressViewModel viewModel, final Address address) {
        fillTitle(viewModel, address);
        fillFirstName(viewModel, address);
        fillLastName(viewModel, address);
        fillStreetName(viewModel, address);
        fillAdditionalStreetInfo(viewModel, address);
        fillCity(viewModel, address);
        fillRegion(viewModel, address);
        fillPostalCode(viewModel, address);
        fillCountry(viewModel, address);
        fillPhone(viewModel, address);
        fillEmail(viewModel, address);
    }

    protected void fillTitle(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null && address.getTitle() != null) {
            viewModel.setTitle(i18nResolver.getOrKey(address.getTitle()));
        }
    }

    protected void fillFirstName(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setFirstName(address.getFirstName());
        }
    }

    protected void fillLastName(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setLastName(address.getLastName());
        }
    }

    protected void fillStreetName(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setStreetName(address.getStreetName());
        }
    }

    protected void fillAdditionalStreetInfo(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setAdditionalStreetInfo(address.getAdditionalStreetInfo());
        }
    }

    protected void fillCity(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setCity(address.getCity());
        }
    }

    protected void fillRegion(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setRegion(address.getRegion());
        }
    }

    protected void fillPostalCode(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setPostalCode(address.getPostalCode());
        }
    }

    protected void fillEmail(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setEmail(address.getEmail());
        }
    }

    protected void fillPhone(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setPhone(address.getPhone());
        }
    }

    protected void fillCountry(final AddressViewModel viewModel, @Nullable final Address address) {
        if (address != null) {
            viewModel.setCountry(address.getCountry().toLocale().getDisplayCountry(i18nResolver.currentLanguage()));
        }
    }
}
