package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.products.AbstractProductVariantViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductVariantViewModel;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductPriceUtils;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import play.mvc.Call;

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

    protected final PriceFormatter getPriceFormatter() {
        return priceFormatter;
    }

    protected final ProductReverseRouter getProductReverseRouter() {
        return productReverseRouter;
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
        viewModel.setUrl(productReverseRouter
                .productDetailPageCall(productWithVariant.getProduct(), productWithVariant.getVariant())
                .map(Call::url)
                .orElse(""));
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