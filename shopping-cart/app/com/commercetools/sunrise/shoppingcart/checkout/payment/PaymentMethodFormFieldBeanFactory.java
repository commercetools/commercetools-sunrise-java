package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.FormUtils;
import com.commercetools.sunrise.common.models.FormFieldFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class PaymentMethodFormFieldBeanFactory extends FormFieldFactory<PaymentMethodFormFieldBean, PaymentMethodFormFieldBeanFactory.Data> {

    private final PaymentFormSelectableOptionBeanFactory paymentFormSelectableOptionBeanFactory;

    @Inject
    public PaymentMethodFormFieldBeanFactory(final PaymentFormSelectableOptionBeanFactory paymentFormSelectableOptionBeanFactory) {
        this.paymentFormSelectableOptionBeanFactory = paymentFormSelectableOptionBeanFactory;
    }

    public final PaymentMethodFormFieldBean create(final Form<?> form, final String formFieldName, final Cart cart,
                                                   final List<PaymentMethodInfo> paymentMethodInfoList) {
        final Data data = new Data(form, formFieldName, cart, paymentMethodInfoList);
        return initializedViewModel(data);
    }

    @Override
    protected PaymentMethodFormFieldBean getViewModelInstance() {
        return new PaymentMethodFormFieldBean();
    }

    @Override
    protected final void initialize(final PaymentMethodFormFieldBean bean, final Data data) {
        fillList(bean, data);
    }

    protected void fillList(final PaymentMethodFormFieldBean bean, final Data data) {
        final String selectedPaymentMethodId = FormUtils.extractFormField(data.form, data.formFieldName);
        bean.setList(data.paymentMethodInfoList.stream()
                .map(paymentMethodInfo -> paymentFormSelectableOptionBeanFactory.create(paymentMethodInfo, selectedPaymentMethodId))
                .collect(toList()));
    }

    protected final static class Data extends Base {

        public final Form<?> form;
        public final String formFieldName;
        public final Cart cart;
        public final List<PaymentMethodInfo> paymentMethodInfoList;

        public Data(final Form<?> form, final String formFieldName, final Cart cart, final List<PaymentMethodInfo> paymentMethodInfoList) {
            this.form = form;
            this.formFieldName = formFieldName;
            this.cart = cart;
            this.paymentMethodInfoList = paymentMethodInfoList;
        }
    }
}
