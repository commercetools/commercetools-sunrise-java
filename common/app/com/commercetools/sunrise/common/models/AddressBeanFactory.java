package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

@RequestScoped
public class AddressBeanFactory extends ViewModelFactory<AddressBean, Address> {

    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    public AddressBeanFactory(final Locale locale, final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    @Override
    protected AddressBean getViewModelInstance() {
        return new AddressBean();
    }

    @Override
    public final AddressBean create(final Address data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final AddressBean model, final Address data) {
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

    protected void fillTitle(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(address.getTitle());
            final String title = i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
            model.setTitle(title);
        }
    }

    protected void fillFirstName(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setFirstName(address.getFirstName());
        }
    }

    protected void fillLastName(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setLastName(address.getLastName());
        }
    }

    protected void fillStreetName(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setStreetName(address.getStreetName());
        }
    }

    protected void fillAdditionalStreetInfo(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setAdditionalStreetInfo(address.getAdditionalStreetInfo());
        }
    }

    protected void fillCity(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setCity(address.getCity());
        }
    }

    protected void fillRegion(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setRegion(address.getRegion());
        }
    }

    protected void fillPostalCode(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setPostalCode(address.getPostalCode());
        }
    }

    protected void fillEmail(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setEmail(address.getEmail());
        }
    }

    protected void fillPhone(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setPhone(address.getPhone());
        }
    }

    protected void fillCountry(final AddressBean model, @Nullable final Address address) {
        if (address != null) {
            model.setCountry(address.getCountry().toLocale().getDisplayCountry(locale));
        }
    }
}
