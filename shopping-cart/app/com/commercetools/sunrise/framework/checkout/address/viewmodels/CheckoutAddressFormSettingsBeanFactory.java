package com.commercetools.sunrise.framework.checkout.address.viewmodels;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.FormViewModelFactory;
import com.commercetools.sunrise.common.models.addresses.CountryFormFieldBeanFactory;
import com.commercetools.sunrise.common.models.addresses.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.checkout.address.CheckoutAddressFormData;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

import static java.util.Collections.singletonList;

@RequestScoped
public class CheckoutAddressFormSettingsBeanFactory extends FormViewModelFactory<CheckoutAddressFormSettingsBean, Cart, CheckoutAddressFormData> {

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
    public final CheckoutAddressFormSettingsBean create(final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        return super.create(cart, form);
    }

    @Override
    protected final void initialize(final CheckoutAddressFormSettingsBean model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        fillCountriesShipping(model, cart, form);
        fillCountriesBilling(model, cart, form);
        fillTitleShipping(model, cart, form);
        fillTitleBilling(model, cart, form);
    }

    protected void fillTitleBilling(final CheckoutAddressFormSettingsBean model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setTitleBilling(titleFormFieldBeanFactory.createWithDefaultOptions(form.field("titleBilling")));
    }

    protected void fillTitleShipping(final CheckoutAddressFormSettingsBean model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setTitleShipping(titleFormFieldBeanFactory.createWithDefaultOptions(form.field("titleShipping")));
    }

    protected void fillCountriesBilling(final CheckoutAddressFormSettingsBean model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        model.setCountriesBilling(countryFormFieldBeanFactory.createWithDefaultOptions(form.field("countryBilling")));
    }

    protected void fillCountriesShipping(final CheckoutAddressFormSettingsBean model, final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        final FormFieldWithOptions<CountryCode> formFieldWithOptions = new FormFieldWithOptions<>(form.field("countryShipping"), singletonList(country));
        model.setCountriesShipping(countryFormFieldBeanFactory.create(formFieldWithOptions));
    }
}
