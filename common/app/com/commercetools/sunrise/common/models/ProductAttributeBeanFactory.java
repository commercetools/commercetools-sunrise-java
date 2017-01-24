package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;

@RequestScoped
public class ProductAttributeBeanFactory extends ViewModelFactory<ProductAttributeBean, ProductAttributeBeanFactory.Data> {

    private final AttributeFormatter attributeFormatter;

    @Inject
    public ProductAttributeBeanFactory(final AttributeFormatter attributeFormatter) {
        this.attributeFormatter = attributeFormatter;
    }

    public final ProductAttributeBean create(final Attribute attribute) {
        final Data data = new Data(attribute);
        return initializedViewModel(data);
    }

    @Override
    protected ProductAttributeBean getViewModelInstance() {
        return new ProductAttributeBean();
    }

    @Override
    protected final void initialize(final ProductAttributeBean bean, final Data data) {
        fillKey(bean, data);
        fillName(bean, data);
        fillValue(bean, data);
    }

    protected void fillKey(final ProductAttributeBean bean, final Data data) {
        bean.setKey(data.attribute.getName());
    }

    protected void fillName(final ProductAttributeBean bean, final Data data) {
        bean.setName(attributeFormatter.label(data.attribute));
    }

    protected void fillValue(final ProductAttributeBean bean, final Data data) {
        bean.setValue(attributeFormatter.value(data.attribute));
    }

    protected final static class Data extends Base {

        private final Attribute attribute;

        public Data(final Attribute attribute) {
            this.attribute = attribute;
        }
    }
}
