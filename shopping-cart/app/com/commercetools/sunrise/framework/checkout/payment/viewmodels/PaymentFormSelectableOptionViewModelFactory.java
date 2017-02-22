package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
public class PaymentFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<PaymentFormSelectableOptionViewModel, PaymentMethodInfo, String> {

    private final Locale locale;

    @Inject
    public PaymentFormSelectableOptionViewModelFactory(final Locale locale) {
        this.locale = locale;
    }

    @Override
    protected PaymentFormSelectableOptionViewModel getViewModelInstance() {
        return new PaymentFormSelectableOptionViewModel();
    }

    @Override
    public final PaymentFormSelectableOptionViewModel create(final PaymentMethodInfo option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final PaymentFormSelectableOptionViewModel model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final PaymentFormSelectableOptionViewModel model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        final String label = Optional.ofNullable(option.getName())
                .flatMap(name -> name.find(locale))
                .orElseGet(option::getMethod);
        model.setLabel(label);
    }

    protected void fillValue(final PaymentFormSelectableOptionViewModel model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        model.setValue(option.getMethod());
    }

    protected void fillSelected(final PaymentFormSelectableOptionViewModel model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        model.setSelected(option.getMethod() != null && option.getMethod().equals(selectedValue));
    }
}
