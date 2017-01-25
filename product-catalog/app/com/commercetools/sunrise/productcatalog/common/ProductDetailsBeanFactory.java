package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductAttributeBean;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductDetailsBeanFactory extends ViewModelFactory<ProductDetailsBean, ProductWithVariant> {

    private final ProductDataConfig productDataConfig;
    private final ProductAttributeBeanFactory productAttributeBeanFactory;

    @Inject
    public ProductDetailsBeanFactory(final ProductDataConfig productDataConfig, final ProductAttributeBeanFactory productAttributeBeanFactory) {
        this.productDataConfig = productDataConfig;
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
        final List<ProductAttributeBean> attributes = productDataConfig.getDisplayedAttributes().stream()
                .map(productWithVariant.getVariant()::getAttribute)
                .filter(Objects::nonNull)
                .map(productAttributeBeanFactory::create)
                .collect(toList());
        bean.setFeatures(attributes);
    }
}
