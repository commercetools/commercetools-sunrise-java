package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.stream.Collectors.toList;

@Singleton
public class ProductGalleryBeanFactory extends ViewModelFactory<ProductGalleryBean, ProductWithVariant> {

    private final ProductImageBeanFactory productImageBeanFactory;

    @Inject
    public ProductGalleryBeanFactory(final ProductImageBeanFactory productImageBeanFactory) {
        this.productImageBeanFactory = productImageBeanFactory;
    }

    @Override
    public final ProductGalleryBean create(final ProductWithVariant data) {
        return super.create(data);
    }

    @Override
    protected ProductGalleryBean getViewModelInstance() {
        return new ProductGalleryBean();
    }

    @Override
    protected final void initialize(final ProductGalleryBean model, final ProductWithVariant data) {
        fillList(model, data);
    }

    protected void fillList(final ProductGalleryBean bean, final ProductWithVariant productWithVariant) {
        bean.setList(productWithVariant.getVariant().getImages().stream()
                .map(productImageBeanFactory::create)
                .collect(toList()));
    }
}
