package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.common.models.products.ProductAttributeBean;
import com.commercetools.sunrise.common.models.products.ProductAttributeBeanFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.*;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductDetailsBeanFactory extends ViewModelFactory<ProductDetailsBean, ProductWithVariant> {

    private final ProductAttributeSettings productAttributeSettings;
    private final ProductAttributeBeanFactory productAttributeBeanFactory;

    @Inject
    public ProductDetailsBeanFactory(final ProductAttributeSettings productAttributeSettings, final ProductAttributeBeanFactory productAttributeBeanFactory) {
        this.productAttributeSettings = productAttributeSettings;
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
        final List<ProductAttributeBean> attributes = productAttributeSettings.getDisplayedAttributes().stream()
                .map(productWithVariant.getVariant()::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> createProductAttributeBean(productWithVariant, attribute))
                .collect(toList());
        bean.setFeatures(attributes);
    }

    private ProductAttributeBean createProductAttributeBean(final ProductWithVariant productWithVariant, final Attribute attribute) {
        final Reference<ProductType> productTypeRef = productWithVariant.getProduct().getProductType();
        return productAttributeBeanFactory.create(AttributeWithProductType.of(attribute, productTypeRef));
    }
}
