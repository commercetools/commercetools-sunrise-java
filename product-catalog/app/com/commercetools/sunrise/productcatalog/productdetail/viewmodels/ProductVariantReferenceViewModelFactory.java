package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;

import javax.inject.Inject;

@RequestScoped
public class ProductVariantReferenceViewModelFactory extends ViewModelFactory<ProductVariantReferenceViewModel, ProductWithVariant> {

    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductVariantReferenceViewModelFactory(final ProductReverseRouter productReverseRouter) {
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    protected ProductVariantReferenceViewModel getViewModelInstance() {
        return new ProductVariantReferenceViewModel();
    }

    @Override
    public final ProductVariantReferenceViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected final void initialize(final ProductVariantReferenceViewModel viewModel, final ProductWithVariant productWithVariant) {
        fillId(viewModel, productWithVariant);
        fillUrl(viewModel, productWithVariant);
    }

    protected void fillId(final ProductVariantReferenceViewModel model, final ProductWithVariant productWithVariant) {
        model.setId(productWithVariant.getVariant().getId());
    }

    protected void fillUrl(final ProductVariantReferenceViewModel model, final ProductWithVariant productWithVariant) {
        model.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(productWithVariant.getProduct(), productWithVariant.getVariant()));
    }
}
