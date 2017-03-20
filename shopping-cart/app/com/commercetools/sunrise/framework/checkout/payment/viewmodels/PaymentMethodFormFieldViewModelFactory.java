package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldWithOptions;
import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class PaymentMethodFormFieldViewModelFactory extends FormFieldViewModelFactory<PaymentMethodFormFieldViewModel, PaymentMethodInfo> {

    private final PaymentFormSelectableOptionViewModelFactory paymentFormSelectableOptionViewModelFactory;

    @Inject
    public PaymentMethodFormFieldViewModelFactory(final PaymentFormSelectableOptionViewModelFactory paymentFormSelectableOptionViewModelFactory) {
        this.paymentFormSelectableOptionViewModelFactory = paymentFormSelectableOptionViewModelFactory;
    }

    protected final PaymentFormSelectableOptionViewModelFactory getPaymentFormSelectableOptionViewModelFactory() {
        return paymentFormSelectableOptionViewModelFactory;
    }

    @Override
    protected List<PaymentMethodInfo> defaultOptions() {
        return Collections.emptyList();
    }

    @Override
    protected PaymentMethodFormFieldViewModel newViewModelInstance(final FormFieldWithOptions<PaymentMethodInfo> formFieldWithOptions) {
        return new PaymentMethodFormFieldViewModel();
    }

    @Override
    public final PaymentMethodFormFieldViewModel create(final FormFieldWithOptions<PaymentMethodInfo> formFieldWithOptions) {
        return super.create(formFieldWithOptions);
    }

    @Override
    public final PaymentMethodFormFieldViewModel createWithDefaultOptions(final Form.Field formField) {
        return super.createWithDefaultOptions(formField);
    }

    @Override
    protected final void initialize(final PaymentMethodFormFieldViewModel viewModel, final FormFieldWithOptions<PaymentMethodInfo> formFieldWithOptions) {
        fillList(viewModel, formFieldWithOptions);
    }

    protected void fillList(final PaymentMethodFormFieldViewModel viewModel, final FormFieldWithOptions<PaymentMethodInfo> formFieldWithOptions) {
        viewModel.setList(formFieldWithOptions.getFormOptions().stream()
                .map(paymentMethodInfo -> paymentFormSelectableOptionViewModelFactory.create(paymentMethodInfo, formFieldWithOptions.getFormField().value()))
                .collect(toList()));
    }
}
