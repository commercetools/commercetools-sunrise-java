package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableOptionViewModelFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class PaymentFormSelectableOptionBeanFactory extends SelectableOptionViewModelFactory<PaymentFormSelectableOptionBean, PaymentMethodInfo> {

    private final LocalizedStringResolver localizedStringResolver;

    @Inject
    public PaymentFormSelectableOptionBeanFactory(final LocalizedStringResolver localizedStringResolver) {
        this.localizedStringResolver = localizedStringResolver;
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
        model.setLabel(Optional.ofNullable(option.getName())
                .flatMap(localizedStringResolver::find)
                .orElse("-"));
    }

    protected void fillValue(final PaymentFormSelectableOptionBean model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        model.setValue(option.getMethod());
    }

    protected void fillSelected(final PaymentFormSelectableOptionBean model, final PaymentMethodInfo option, @Nullable final String selectedValue) {
        model.setSelected(option.getMethod() != null && option.getMethod().equals(selectedValue));
    }
}
