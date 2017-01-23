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
import java.util.function.Consumer;

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
        ifAddressPresent(data, address -> {
            final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(address.getTitle());
            final String title = i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
            bean.setTitle(title);
        });
    }

    protected void fillFirstName(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setFirstName(address.getFirstName()));
    }

    protected void fillLastName(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setLastName(address.getLastName()));
    }

    protected void fillStreetName(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setStreetName(address.getStreetName()));
    }

    protected void fillAdditionalStreetInfo(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setAdditionalStreetInfo(address.getAdditionalStreetInfo()));
    }

    protected void fillCity(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setCity(address.getCity()));
    }

    protected void fillRegion(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setRegion(address.getRegion()));
    }

    protected void fillPostalCode(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setPostalCode(address.getPostalCode()));
    }

    protected void fillEmail(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setEmail(address.getEmail()));
    }

    protected void fillPhone(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setPhone(address.getPhone()));
    }

    protected void fillCountry(final AddressBean bean, final Data data) {
        ifAddressPresent(data, address -> bean.setCountry(address.getCountry().toLocale().getDisplayCountry(locale)));
    }

    private void ifAddressPresent(final Data data, final Consumer<Address> consumer) {
        if (data.address != null) {
            consumer.accept(data.address);
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
