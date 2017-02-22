package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.products.AbstractProductVariantViewModelFactory;
import com.commercetools.sunrise.common.models.products.ProductVariantViewModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.ProductPriceUtils;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;

import javax.inject.Inject;

@RequestScoped
public class ProductVariantViewModelFactory extends AbstractProductVariantViewModelFactory<ProductWithVariant> {

    private final PriceFormatter priceFormatter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductVariantViewModelFactory(final PriceFormatter priceFormatter, final ProductReverseRouter productReverseRouter) {
        super();
        this.priceFormatter = priceFormatter;
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    public final ProductVariantViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected final void initialize(final ProductVariantViewModel viewModel, final ProductWithVariant productWithVariant) {
        super.initialize(viewModel, productWithVariant);
    }

    @Override
    protected void fillSku(final ProductVariantViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setSku(findSku(productWithVariant.getVariant()));
    }

    @Override
    protected void fillName(final ProductVariantViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setName(productWithVariant.getProduct().getName());
    }

    @Override
    protected void fillUrl(final ProductVariantViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(productWithVariant.getProduct(), productWithVariant.getVariant()));
    }

    @Override
    protected void fillImage(final ProductVariantViewModel viewModel, final ProductWithVariant productWithVariant) {
        findImageUrl(productWithVariant.getVariant()).ifPresent(viewModel::setImage);
    }

    @Override
    protected void fillPrice(final ProductVariantViewModel viewModel, final ProductWithVariant productWithVariant) {
        ProductPriceUtils.calculateAppliedProductPrice(productWithVariant.getVariant())
                .ifPresent(price -> viewModel.setPrice(priceFormatter.format(price)));
    }

    @Override
    protected void fillPriceOld(final ProductVariantViewModel viewModel, final ProductWithVariant productWithVariant) {
        ProductPriceUtils.calculatePreviousProductPrice(productWithVariant.getVariant())
                .ifPresent(oldPrice -> viewModel.setPriceOld(priceFormatter.format(oldPrice)));
    }
}