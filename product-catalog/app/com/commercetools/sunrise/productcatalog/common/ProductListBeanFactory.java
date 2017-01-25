package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductListBeanFactory extends ViewModelFactory<ProductListBean, ProductListBeanFactory.Data> {

    private final ProductThumbnailFactory productThumbnailFactory;

    @Inject
    public ProductListBeanFactory(final ProductThumbnailFactory productThumbnailFactory) {
        this.productThumbnailFactory = productThumbnailFactory;
    }

    public final ProductListBean create(final Iterable<ProductProjection> productList) {
        final Data data = new Data(productList);
        return initializedViewModel(data);
    }

    @Override
    protected ProductListBean getViewModelInstance() {
        return new ProductListBean();
    }

    @Override
    protected final void initialize(final ProductListBean model, final Data data) {
        fillList(model, data);
    }

    protected void fillList(final ProductListBean bean, final Data data) {
        bean.setList(StreamSupport.stream(data.products.spliterator(), false)
                .map(product -> productThumbnailFactory.create(product, getSelectedVariant(product)))
                .collect(toList()));
    }

    private ProductVariant getSelectedVariant(final ProductProjection product) {
        return product.findFirstMatchingVariant()
                .orElseGet(product::getMasterVariant);
    }

    protected final static class Data extends Base {

        public final Iterable<ProductProjection> products;

        public Data(final Iterable<ProductProjection> products) {
            this.products = products;
        }
    }
}
