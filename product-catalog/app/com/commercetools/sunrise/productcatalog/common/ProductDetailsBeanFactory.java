package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductAttributeBean;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductDetailsBeanFactory extends ViewModelFactory<ProductDetailsBean, ProductDetailsBeanFactory.Data> {

    private final ProductDataConfig productDataConfig;
    private final ProductAttributeBeanFactory productAttributeBeanFactory;

    @Inject
    public ProductDetailsBeanFactory(final ProductDataConfig productDataConfig, final ProductAttributeBeanFactory productAttributeBeanFactory) {
        this.productDataConfig = productDataConfig;
        this.productAttributeBeanFactory = productAttributeBeanFactory;
    }

    public final ProductDetailsBean create(final ProductProjection product, final ProductVariant variant) {
        final Data data = new Data(product, variant);
        return initializedViewModel(data);
    }

    @Override
    protected ProductDetailsBean getViewModelInstance() {
        return new ProductDetailsBean();
    }

    @Override
    protected final void initialize(final ProductDetailsBean bean, final Data data) {
        fillList(bean, data);
    }

    protected void fillList(final ProductDetailsBean bean, final Data data) {
        final List<ProductAttributeBean> attributes = productDataConfig.getDisplayedAttributes().stream()
                .map(data.variant::getAttribute)
                .filter(Objects::nonNull)
                .map(productAttributeBeanFactory::create)
                .collect(toList());
        bean.setFeatures(attributes);
    }

    protected final static class Data extends Base {

        public final ProductProjection product;
        public final ProductVariant variant;

        public Data(final ProductProjection product, final ProductVariant variant) {
            this.product = product;
            this.variant = variant;
        }
    }
}
