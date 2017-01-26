package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.neovisionaries.i18n.CountryCode;
import play.data.Form;

import javax.inject.Inject;

import static java.util.Collections.singletonList;

@RequestScoped
public class CheckoutAddressFormSettingsBeanFactory extends ViewModelFactory<CheckoutAddressFormSettingsBean, Form<?>> {

    private final CountryCode country;
    private final CountryFormFieldBeanFactory countryFormFieldBeanFactory;
    private final TitleFormFieldBeanFactory titleFormFieldBeanFactory;

    @Inject
    public CheckoutAddressFormSettingsBeanFactory(final CountryCode country, final CountryFormFieldBeanFactory countryFormFieldBeanFactory,
                                                  final TitleFormFieldBeanFactory titleFormFieldBeanFactory) {
        this.country = country;
        this.countryFormFieldBeanFactory = countryFormFieldBeanFactory;
        this.titleFormFieldBeanFactory = titleFormFieldBeanFactory;
    }

    @Override
    protected CheckoutAddressFormSettingsBean getViewModelInstance() {
        return new CheckoutAddressFormSettingsBean();
    }

    @Override
    public final CheckoutAddressFormSettingsBean create(final Form<?> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CheckoutAddressFormSettingsBean bean, final Form<?> data) {
        fillCountriesShipping(bean, data);
        fillCountriesBilling(bean, data);
        fillTitleShipping(bean, data);
        fillTitleBilling(bean, data);
    }

    protected void fillTitleBilling(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        bean.setTitleBilling(titleFormFieldBeanFactory.createWithDefaultOptions(form.field("titleBilling")));
    }

    protected void fillTitleShipping(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        bean.setTitleShipping(titleFormFieldBeanFactory.createWithDefaultOptions(form.field("titleShipping")));
    }

    protected void fillCountriesBilling(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        bean.setCountriesBilling(countryFormFieldBeanFactory.createWithDefaultOptions(form.field("countryBilling")));
    }

    protected void fillCountriesShipping(final CheckoutAddressFormSettingsBean bean, final Form<?> form) {
        final FormFieldWithOptions<CountryCode> formFieldWithOptions = new FormFieldWithOptions<>(form.field("countryShipping"), singletonList(country));
        bean.setCountriesShipping(countryFormFieldBeanFactory.create(formFieldWithOptions));
    }
}
