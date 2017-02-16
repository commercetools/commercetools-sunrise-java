package com.commercetools.sunrise.shoppingcart.checkout.payment.view;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
public class PaymentFormSelectableOptionBeanFactory extends SelectableViewModelFactory<PaymentFormSelectableOptionBean, PaymentMethodInfo, String> {

    private final Locale locale;

    @Inject
    public PaymentFormSelectableOptionBeanFactory(final Locale locale) {
        this.locale = locale;
    }

    @Override
    protected PaymentFormSelectableOptionBean getViewModelInstance() {
        return new PaymentFormSelectableOptionBean();
    }

    @Override
    public final PaymentFormSelectableOptionBean create(final PaymentMethodInfo option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final PaymentFormSelectableOptionBean model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final PaymentFormSelectableOptionBean model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        final String label = Optional.ofNullable(option.getName())
                .flatMap(name -> name.find(locale))
                .orElseGet(option::getMethod);
        model.setLabel(label);
    }

    protected void fillValue(final PaymentFormSelectableOptionBean model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        model.setValue(option.getMethod());
    }

    protected void fillSelected(final PaymentFormSelectableOptionBean model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        model.setSelected(option.getMethod() != null && option.getMethod().equals(selectedValue));
    }
}
