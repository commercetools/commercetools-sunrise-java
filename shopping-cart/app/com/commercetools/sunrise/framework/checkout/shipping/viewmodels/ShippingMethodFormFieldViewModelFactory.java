package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldWithOptions;
import com.commercetools.sunrise.framework.viewmodels.forms.FormFieldViewModelFactory;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ShippingMethodFormFieldViewModelFactory extends FormFieldViewModelFactory<ShippingMethodFormFieldViewModel, ShippingMethod> {

    private final ShippingFormSelectableOptionViewModelFactory shippingFormSelectableOptionViewModelFactory;

    @Inject
    public ShippingMethodFormFieldViewModelFactory(final ShippingFormSelectableOptionViewModelFactory shippingFormSelectableOptionViewModelFactory) {
        this.shippingFormSelectableOptionViewModelFactory = shippingFormSelectableOptionViewModelFactory;
    }

    protected final ShippingFormSelectableOptionViewModelFactory getShippingFormSelectableOptionViewModelFactory() {
        return shippingFormSelectableOptionViewModelFactory;
    }

    @Override
    protected ShippingMethodFormFieldViewModel newViewModelInstance(final FormFieldWithOptions<ShippingMethod> formFieldWithOptions) {
        return new ShippingMethodFormFieldViewModel();
    }

    @Override
    protected List<ShippingMethod> defaultOptions() {
        return Collections.emptyList();
    }

    @Override
    public final ShippingMethodFormFieldViewModel create(final FormFieldWithOptions<ShippingMethod> formFieldWithOptions) {
        return super.create(formFieldWithOptions);
    }

    @Override
    public final ShippingMethodFormFieldViewModel createWithDefaultOptions(final Form.Field formField) {
        return super.createWithDefaultOptions(formField);
    }

    @Override
    protected final void initialize(final ShippingMethodFormFieldViewModel viewModel, final FormFieldWithOptions<ShippingMethod> formFieldWithOptions) {
        fillList(viewModel, formFieldWithOptions);
    }

    protected void fillList(final ShippingMethodFormFieldViewModel viewModel, final FormFieldWithOptions<ShippingMethod> formFieldWithOptions) {
        viewModel.setList(formFieldWithOptions.getFormOptions().stream()
                .map(shippingMethod -> shippingFormSelectableOptionViewModelFactory.create(shippingMethod, formFieldWithOptions.getFormField().value()))
                .collect(toList()));
    }
}
