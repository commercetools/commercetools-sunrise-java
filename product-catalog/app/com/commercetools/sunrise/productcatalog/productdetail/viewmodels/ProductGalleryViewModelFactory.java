package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.stream.Collectors.toList;

@Singleton
public class ProductGalleryViewModelFactory extends ViewModelFactory<ProductGalleryViewModel, ProductWithVariant> {

    private final ProductImageViewModelFactory productImageViewModelFactory;

    @Inject
    public ProductGalleryViewModelFactory(final ProductImageViewModelFactory productImageViewModelFactory) {
        this.productImageViewModelFactory = productImageViewModelFactory;
    }

    @Override
    public final ProductGalleryViewModel create(final ProductWithVariant data) {
        return super.create(data);
    }

    @Override
    protected ProductGalleryViewModel getViewModelInstance() {
        return new ProductGalleryViewModel();
    }

    @Override
    protected final void initialize(final ProductGalleryViewModel model, final ProductWithVariant data) {
        fillList(model, data);
    }

    protected void fillList(final ProductGalleryViewModel model, final ProductWithVariant productWithVariant) {
        model.setList(productWithVariant.getVariant().getImages().stream()
                .map(productImageViewModelFactory::create)
                .collect(toList()));
    }
}
