package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.attributes.Attribute;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class ProductAttributeFormSelectableOptionBeanFactory extends ViewModelFactory<ProductAttributeFormSelectableOptionBean, ProductAttributeFormSelectableOptionBeanFactory.Data> {

    private final AttributeFormatter attributeFormatter;

    @Inject
    public ProductAttributeFormSelectableOptionBeanFactory(final AttributeFormatter attributeFormatter) {
        this.attributeFormatter = attributeFormatter;
    }

    public final ProductAttributeFormSelectableOptionBean create(final Attribute attribute, @Nullable final String selectedAttributeValue) {
        final Data data = new Data(attribute, selectedAttributeValue);
        return initializedViewModel(data);
    }

    @Override
    protected ProductAttributeFormSelectableOptionBean getViewModelInstance() {
        return new ProductAttributeFormSelectableOptionBean();
    }

    @Override
    protected final void initialize(final ProductAttributeFormSelectableOptionBean bean, final Data data) {
        fillLabel(bean, data);
        fillValue(bean, data);
        fillSelected(bean, data);
    }

    protected void fillLabel(final ProductAttributeFormSelectableOptionBean bean, final Data data) {
        bean.setLabel(attributeFormatter.value(data.attribute));
    }

    protected void fillValue(final ProductAttributeFormSelectableOptionBean bean, final Data data) {
        bean.setValue(attributeFormatter.valueAsKey(data.attribute));
    }

    protected void fillSelected(final ProductAttributeFormSelectableOptionBean bean, final Data data) {
        bean.setSelected(attributeFormatter.valueAsKey(data.attribute).equals(data.selectedAttributeValue));
    }

    protected final static class Data extends Base {

        public final Attribute attribute;
        @Nullable
        public final String selectedAttributeValue;

        public Data(final Attribute attribute, @Nullable final String selectedAttributeValue) {
            this.attribute = attribute;
            this.selectedAttributeValue = selectedAttributeValue;
        }
    }
}
