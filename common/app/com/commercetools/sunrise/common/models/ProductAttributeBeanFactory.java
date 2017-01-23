package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductAttributeBeanFactory extends ViewModelFactory<ProductAttributeBean, ProductAttributeBeanFactory.Data> {

    private final AttributeFormatter attributeFormatter;
    private final ProductDataConfig productDataConfig;

    @Inject
    public ProductAttributeBeanFactory(final AttributeFormatter attributeFormatter, final ProductDataConfig productDataConfig) {
        this.attributeFormatter = attributeFormatter;
        this.productDataConfig = productDataConfig;
    }

    public final ProductAttributeBean create(final Attribute attribute) {
        final Data data = new Data(attribute);
        return initializedViewModel(data);
    }

    public final List<ProductAttributeBean> createList(final ProductVariant variant) {
        return productDataConfig.getDisplayedAttributes().stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .map(this::create)
                .collect(toList());
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
