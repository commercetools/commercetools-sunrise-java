package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

@RequestScoped
public class PaymentFormSelectableOptionBeanFactory extends FormFieldViewModelFactory<PaymentFormSelectableOptionBean, PaymentFormSelectableOptionBeanFactory.Data> {

    private final LocalizedStringResolver localizedStringResolver;

    @Inject
    public PaymentFormSelectableOptionBeanFactory(final LocalizedStringResolver localizedStringResolver) {
        this.localizedStringResolver = localizedStringResolver;
    }

    public final PaymentFormSelectableOptionBean create(final PaymentMethodInfo paymentMethod, @Nullable final String selectedPaymentMethodId) {
        final Data data = new Data(paymentMethod, selectedPaymentMethodId);
        return initializedViewModel(data);
    }

    @Override
    protected PaymentFormSelectableOptionBean getViewModelInstance() {
        return new PaymentFormSelectableOptionBean();
    }

    @Override
    protected final void initialize(final PaymentFormSelectableOptionBean bean, final Data data) {
        fillLabel(bean, data);
        fillValue(bean, data);
        fillSelected(bean, data);
    }

    protected void fillLabel(final PaymentFormSelectableOptionBean bean, final Data data) {
        bean.setLabel(Optional.ofNullable(data.paymentMethodInfo.getName())
                .flatMap(localizedStringResolver::find)
                .orElse("-"));
    }

    protected void fillValue(final PaymentFormSelectableOptionBean bean, final Data data) {
        bean.setValue(data.paymentMethodInfo.getMethod());
    }

    protected void fillSelected(final PaymentFormSelectableOptionBean bean, final Data data) {
        bean.setSelected(Objects.equals(data.paymentMethodInfo.getMethod(), data.selectedPaymentMethodId));
    }

    protected final static class Data extends Base {

        public final PaymentMethodInfo paymentMethodInfo;
        @Nullable
        public final String selectedPaymentMethodId;

        public Data(final PaymentMethodInfo paymentMethodInfo, final String selectedPaymentMethodId) {
            this.paymentMethodInfo = paymentMethodInfo;
            this.selectedPaymentMethodId = selectedPaymentMethodId;
        }
    }
}
