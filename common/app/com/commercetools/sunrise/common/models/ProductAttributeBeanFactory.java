package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductAttributeBeanFactory extends ViewModelFactory {

    private final AttributeFormatter attributeFormatter;
    private final ProductDataConfig productDataConfig;

    @Inject
    public ProductAttributeBeanFactory(final AttributeFormatter attributeFormatter, final ProductDataConfig productDataConfig) {
        this.attributeFormatter = attributeFormatter;
        this.productDataConfig = productDataConfig;
    }

    public ProductAttributeBean create(final Attribute attribute) {
        final ProductAttributeBean bean = new ProductAttributeBean();
        initialize(bean, attribute);
        return bean;
    }

    public List<ProductAttributeBean> createList(final ProductVariant variant) {
        return productDataConfig.getDisplayedAttributes().stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .map(this::create)
                .collect(toList());
    }

    protected final void initialize(final ProductAttributeBean bean, final Attribute attribute) {
        fillAttributeInfo(bean, attribute);
    }

    protected void fillAttributeInfo(final ProductAttributeBean bean, final Attribute attribute) {
        bean.setKey(attribute.getName());
        bean.setName(attributeFormatter.label(attribute));
        bean.setValue(attributeFormatter.value(attribute));
    }
}
