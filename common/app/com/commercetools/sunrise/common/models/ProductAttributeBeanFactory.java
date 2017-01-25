package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;

@RequestScoped
public class ProductAttributeBeanFactory extends ViewModelFactory<ProductAttributeBean, Attribute> {

    private final AttributeFormatter attributeFormatter;

    @Inject
    public ProductAttributeBeanFactory(final AttributeFormatter attributeFormatter) {
        this.attributeFormatter = attributeFormatter;
    }

    @Override
    protected ProductAttributeBean getViewModelInstance() {
        return new ProductAttributeBean();
    }

    @Override
    public final ProductAttributeBean create(final Attribute data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductAttributeBean model, final Attribute data) {
        fillKey(model, data);
        fillName(model, data);
        fillValue(model, data);
    }

    protected void fillKey(final ProductAttributeBean bean, final Attribute attribute) {
        bean.setKey(attribute.getName());
    }

    protected void fillName(final ProductAttributeBean bean, final Attribute attribute) {
        bean.setName(attributeFormatter.label(attribute));
    }

    protected void fillValue(final ProductAttributeBean bean, final Attribute attribute) {
        bean.setValue(attributeFormatter.value(attribute));
    }
}
