package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import play.mvc.Call;

import javax.inject.Inject;

@RequestScoped
public class ProductVariantReferenceViewModelFactory extends SimpleViewModelFactory<ProductVariantReferenceViewModel, ProductWithVariant> {

    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductVariantReferenceViewModelFactory(final ProductReverseRouter productReverseRouter) {
        this.productReverseRouter = productReverseRouter;
    }

    protected final ProductReverseRouter getProductReverseRouter() {
        return productReverseRouter;
    }

    @Override
    protected ProductVariantReferenceViewModel newViewModelInstance(final ProductWithVariant productWithVariant) {
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

    protected void fillId(final ProductVariantReferenceViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setId(productWithVariant.getVariant().getId());
    }

    protected void fillUrl(final ProductVariantReferenceViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setUrl(productReverseRouter
                .productDetailPageCall(productWithVariant.getProduct(), productWithVariant.getVariant())
                .map(Call::url)
                .orElse(""));
    }
}
