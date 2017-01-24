package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

@RequestScoped
public class AddressBeanFactory extends ViewModelFactory<AddressBean, AddressBeanFactory.Data> {

    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    public AddressBeanFactory(final Locale locale, final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    public final AddressBean create(@Nullable final Address address) {
        final Data data = new Data(address);
        return initializedViewModel(data);
    }

    @Override
    protected AddressBean getViewModelInstance() {
        return new AddressBean();
    }

    @Override
    protected final void initialize(final AddressBean bean, final Data data) {
        fillTitle(bean, data);
        fillFirstName(bean, data);
        fillLastName(bean, data);
        fillStreetName(bean, data);
        fillAdditionalStreetInfo(bean, data);
        fillCity(bean, data);
        fillRegion(bean, data);
        fillPostalCode(bean, data);
        fillCountry(bean, data);
        fillPhone(bean, data);
        fillEmail(bean, data);
    }

    protected void fillTitle(final AddressBean bean, final Data data) {
        if (data.address != null) {
            final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(data.address.getTitle());
            final String title = i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
            bean.setTitle(title);
        }
    }

    protected void fillFirstName(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setFirstName(data.address.getFirstName());
        }
    }

    protected void fillLastName(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setLastName(data.address.getLastName());
        }
    }

    protected void fillStreetName(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setStreetName(data.address.getStreetName());
        }
    }

    protected void fillAdditionalStreetInfo(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setAdditionalStreetInfo(data.address.getAdditionalStreetInfo());
        }
    }

    protected void fillCity(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setCity(data.address.getCity());
        }
    }

    protected void fillRegion(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setRegion(data.address.getRegion());
        }
    }

    protected void fillPostalCode(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setPostalCode(data.address.getPostalCode());
        }
    }

    protected void fillEmail(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setEmail(data.address.getEmail());
        }
    }

    protected void fillPhone(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setPhone(data.address.getPhone());
        }
    }

    protected void fillCountry(final AddressBean bean, final Data data) {
        if (data.address != null) {
            bean.setCountry(data.address.getCountry().toLocale().getDisplayCountry(locale));
        }
    }

    protected final static class Data extends Base {

        @Nullable
        public final Address address;

        public Data(final Address address) {
            this.address = address;
        }
    }
}
