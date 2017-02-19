package com.commercetools.sunrise.shoppingcart.checkout.payment.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class PaymentMethodFormFieldBeanFactory extends FormFieldViewModelFactory<PaymentMethodFormFieldBean, PaymentMethodInfo> {

    private final PaymentFormSelectableOptionBeanFactory paymentFormSelectableOptionBeanFactory;

    @Inject
    public PaymentMethodFormFieldBeanFactory(final PaymentFormSelectableOptionBeanFactory paymentFormSelectableOptionBeanFactory) {
        this.paymentFormSelectableOptionBeanFactory = paymentFormSelectableOptionBeanFactory;
    }

    @Override
    protected List<PaymentMethodInfo> defaultOptions() {
        return Collections.emptyList();
    }

    @Override
    protected PaymentMethodFormFieldBean getViewModelInstance() {
        return new PaymentMethodFormFieldBean();
    }

    @Override
    public final PaymentMethodFormFieldBean create(final FormFieldWithOptions<PaymentMethodInfo> data) {
        return super.create(data);
    }

    @Override
    public final PaymentMethodFormFieldBean createWithDefaultOptions(final Form.Field formField) {
        return super.createWithDefaultOptions(formField);
    }

    @Override
    protected final void initialize(final PaymentMethodFormFieldBean model, final FormFieldWithOptions<PaymentMethodInfo> data) {
        fillList(model, data);
    }

    protected void fillList(final PaymentMethodFormFieldBean model, final FormFieldWithOptions<PaymentMethodInfo> data) {
        model.setList(data.getFormOptions().stream()
                .map(paymentMethodInfo -> paymentFormSelectableOptionBeanFactory.create(paymentMethodInfo, data.getFormField().value()))
                .collect(toList()));
    }
}
