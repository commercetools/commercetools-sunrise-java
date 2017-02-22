package com.commercetools.sunrise.common.models.addresses;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

@RequestScoped
public class AddressViewModelFactory extends ViewModelFactory<AddressViewModel, Address> {

    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    public AddressViewModelFactory(final Locale locale, final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    @Override
    protected AddressViewModel getViewModelInstance() {
        return new AddressViewModel();
    }

    @Override
    public final AddressViewModel create(final Address data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final AddressViewModel model, final Address data) {
        fillTitle(model, data);
        fillFirstName(model, data);
        fillLastName(model, data);
        fillStreetName(model, data);
        fillAdditionalStreetInfo(model, data);
        fillCity(model, data);
        fillRegion(model, data);
        fillPostalCode(model, data);
        fillCountry(model, data);
        fillPhone(model, data);
        fillEmail(model, data);
    }

    protected void fillTitle(final AddressViewModel model, @Nullable final Address address) {
        if (address != null && address.getTitle() != null) {
            final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(address.getTitle());
            final String title = i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
            model.setTitle(title);
        }
    }

    protected void fillFirstName(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setFirstName(address.getFirstName());
        }
    }

    protected void fillLastName(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setLastName(address.getLastName());
        }
    }

    protected void fillStreetName(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setStreetName(address.getStreetName());
        }
    }

    protected void fillAdditionalStreetInfo(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setAdditionalStreetInfo(address.getAdditionalStreetInfo());
        }
    }

    protected void fillCity(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setCity(address.getCity());
        }
    }

    protected void fillRegion(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setRegion(address.getRegion());
        }
    }

    protected void fillPostalCode(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setPostalCode(address.getPostalCode());
        }
    }

    protected void fillEmail(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setEmail(address.getEmail());
        }
    }

    protected void fillPhone(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setPhone(address.getPhone());
        }
    }

    protected void fillCountry(final AddressViewModel model, @Nullable final Address address) {
        if (address != null) {
            model.setCountry(address.getCountry().toLocale().getDisplayCountry(locale));
        }
    }
}
