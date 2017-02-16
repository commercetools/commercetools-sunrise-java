package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;

import javax.inject.Inject;

@RequestScoped
public class ProductAttributeBeanFactory extends ViewModelFactory<ProductAttributeBean, AttributeWithProductType> {

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
    public final ProductAttributeBean create(final AttributeWithProductType data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductAttributeBean model, final AttributeWithProductType data) {
        fillKey(model, data);
        fillName(model, data);
        fillValue(model, data);
    }

    protected void fillKey(final ProductAttributeBean bean, final AttributeWithProductType attributeWithProductType) {
        bean.setKey(attributeWithProductType.getAttribute().getName());
    }

    protected void fillName(final ProductAttributeBean bean, final AttributeWithProductType attributeWithProductType) {
        bean.setName(attributeFormatter.label(attributeWithProductType));
    }

    protected void fillValue(final ProductAttributeBean bean, final AttributeWithProductType attributeWithProductType) {
        bean.setValue(attributeFormatter.value(attributeWithProductType));
    }
}
