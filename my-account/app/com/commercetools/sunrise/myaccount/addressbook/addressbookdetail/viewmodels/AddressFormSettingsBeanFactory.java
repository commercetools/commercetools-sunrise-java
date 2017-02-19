package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.models.FormViewModelFactory;
import com.commercetools.sunrise.common.models.addresses.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import io.sphere.sdk.customers.Customer;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;

@RequestScoped
public class AddressFormSettingsBeanFactory extends FormViewModelFactory<AddressFormSettingsBean, Customer, AddressFormData> {

    private final String titleFormFieldName;
    private final String countryFormFieldName;
    private final TitleFormFieldBeanFactory titleFormFieldBeanFactory;
    private final CountryFormFieldBeanFactory countryFormFieldBeanFactory;

    @Inject
    public AddressFormSettingsBeanFactory(final Configuration configuration, final TitleFormFieldBeanFactory titleFormFieldBeanFactory,
                                          final CountryFormFieldBeanFactory countryFormFieldBeanFactory) {
        this.titleFormFieldName = configuration.getString("form.address.titleFormFieldName", "title");
        this.countryFormFieldName = configuration.getString("form.address.countryFormFieldName", "country");
        this.titleFormFieldBeanFactory = titleFormFieldBeanFactory;
        this.countryFormFieldBeanFactory = countryFormFieldBeanFactory;
    }

    @Override
    protected AddressFormSettingsBean getViewModelInstance() {
        return new AddressFormSettingsBean();
    }

    @Override
    public final AddressFormSettingsBean create(final Customer customer, final Form<? extends AddressFormData> form) {
        return super.create(customer, form);
    }

    @Override
    protected final void initialize(final AddressFormSettingsBean model, final Customer customer, final Form<? extends AddressFormData> form) {
        fillTitle(model, customer, form);
        fillCountries(model, customer, form);
    }

    protected void fillTitle(final AddressFormSettingsBean model, final Customer customer, final Form<? extends AddressFormData> form) {
        model.setTitle(titleFormFieldBeanFactory.createWithDefaultOptions(form.field(titleFormFieldName)));
    }

    protected void fillCountries(final AddressFormSettingsBean model, final Customer customer, final Form<? extends AddressFormData> form) {
        model.setCountries(countryFormFieldBeanFactory.createWithDefaultOptions(form.field(countryFormFieldName)));
    }
}
