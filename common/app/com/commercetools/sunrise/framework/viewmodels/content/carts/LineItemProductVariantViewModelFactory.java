package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.viewmodels.content.products.AbstractProductVariantViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductVariantViewModel;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductPriceUtils;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import io.sphere.sdk.carts.LineItem;
import play.mvc.Call;

import javax.inject.Inject;

@RequestScoped
public class LineItemProductVariantViewModelFactory extends AbstractProductVariantViewModelFactory<LineItem> {

    private final PriceFormatter priceFormatter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public LineItemProductVariantViewModelFactory(final PriceFormatter priceFormatter,
                                                  final ProductReverseRouter productReverseRouter) {
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
    public final ProductVariantViewModel create(final LineItem lineItem) {
        return super.create(lineItem);
    }

    @Override
    protected final void initialize(final ProductVariantViewModel viewModel, final LineItem lineItem) {
        super.initialize(viewModel, lineItem);
    }

    @Override
    protected void fillSku(final ProductVariantViewModel viewModel, final LineItem lineItem) {
        viewModel.setSku(findSku(lineItem.getVariant()));
    }

    @Override
    protected void fillName(final ProductVariantViewModel viewModel, final LineItem lineItem) {
        viewModel.setName(lineItem.getName());
    }

    @Override
    protected void fillUrl(final ProductVariantViewModel viewModel, final LineItem lineItem) {
        viewModel.setUrl(productReverseRouter
                .productDetailPageCall(lineItem)
                .map(Call::url)
                .orElse(""));
    }

    @Override
    protected void fillImage(final ProductVariantViewModel viewModel, final LineItem lineItem) {
        findImageUrl(lineItem.getVariant()).ifPresent(viewModel::setImage);
    }

    @Override
    protected void fillPrice(final ProductVariantViewModel viewModel, final LineItem lineItem) {
        viewModel.setPrice(priceFormatter.format(ProductPriceUtils.calculateAppliedProductPrice(lineItem)));
    }

    @Override
    protected void fillPriceOld(final ProductVariantViewModel viewModel, final LineItem lineItem) {
        ProductPriceUtils.calculatePreviousProductPrice(lineItem)
                .ifPresent(oldPrice -> viewModel.setPriceOld(priceFormatter.format(oldPrice)));
    }
}