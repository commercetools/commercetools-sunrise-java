package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.AttributeSettings;
import com.commercetools.sunrise.common.models.*;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductDetailsBeanFactory extends ViewModelFactory<ProductDetailsBean, ProductWithVariant> {

    private final AttributeSettings attributeSettings;
    private final ProductAttributeBeanFactory productAttributeBeanFactory;

    @Inject
    public ProductDetailsBeanFactory(final AttributeSettings attributeSettings, final ProductAttributeBeanFactory productAttributeBeanFactory) {
        this.attributeSettings = attributeSettings;
        this.productAttributeBeanFactory = productAttributeBeanFactory;
    }

    @Override
    public final ProductDetailsBean create(final ProductWithVariant data) {
        return super.create(data);
    }

    @Override
    protected ProductDetailsBean getViewModelInstance() {
        return new ProductDetailsBean();
    }

    @Override
    protected final void initialize(final ProductDetailsBean model, final ProductWithVariant data) {
        fillList(model, data);
    }

    protected void fillList(final ProductDetailsBean bean, final ProductWithVariant productWithVariant) {
        final List<ProductAttributeBean> attributes = attributeSettings.getDisplayedAttributes().stream()
                .map(productWithVariant.getVariant()::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> createProductAttributeBean(productWithVariant, attribute))
                .collect(toList());
        bean.setFeatures(attributes);
    }

    private ProductAttributeBean createProductAttributeBean(final ProductWithVariant productWithVariant, final Attribute attribute) {
        final Reference<ProductType> productTypeRef = productWithVariant.getProduct().getProductType();
        return productAttributeBeanFactory.create(new AttributeWithProductType(attribute, productTypeRef));
    }
}
