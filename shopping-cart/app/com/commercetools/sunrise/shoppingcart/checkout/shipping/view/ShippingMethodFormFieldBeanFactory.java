package com.commercetools.sunrise.shoppingcart.checkout.shipping.view;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ShippingMethodFormFieldBeanFactory extends FormFieldViewModelFactory<ShippingMethodFormFieldBean, ShippingMethod> {

    private final ShippingFormSelectableOptionBeanFactory shippingFormSelectableOptionBeanFactory;

    @Inject
    public ShippingMethodFormFieldBeanFactory(final ShippingFormSelectableOptionBeanFactory shippingFormSelectableOptionBeanFactory) {
        this.shippingFormSelectableOptionBeanFactory = shippingFormSelectableOptionBeanFactory;
    }

    @Override
    protected ShippingMethodFormFieldBean getViewModelInstance() {
        return new ShippingMethodFormFieldBean();
    }

    @Override
    protected List<ShippingMethod> defaultOptions() {
        return Collections.emptyList();
    }

    @Override
    public final ShippingMethodFormFieldBean create(final FormFieldWithOptions<ShippingMethod> data) {
        return super.create(data);
    }

    @Override
    public final ShippingMethodFormFieldBean createWithDefaultOptions(final Form.Field formField) {
        return super.createWithDefaultOptions(formField);
    }

    @Override
    protected final void initialize(final ShippingMethodFormFieldBean model, final FormFieldWithOptions<ShippingMethod> data) {
        fillList(model, data);
    }

    protected void fillList(final ShippingMethodFormFieldBean model, final FormFieldWithOptions<ShippingMethod> formFieldWithOptions) {
        model.setList(formFieldWithOptions.getFormOptions().stream()
                .map(shippingMethod -> shippingFormSelectableOptionBeanFactory.create(shippingMethod, formFieldWithOptions.getFormField().value()))
                .collect(toList()));
    }
}
