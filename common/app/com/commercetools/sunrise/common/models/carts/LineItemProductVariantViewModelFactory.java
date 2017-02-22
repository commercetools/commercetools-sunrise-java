package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.products.AbstractProductVariantViewModelFactory;
import com.commercetools.sunrise.common.models.products.ProductVariantViewModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.ProductPriceUtils;
import io.sphere.sdk.carts.LineItem;

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

    @Override
    public final ProductVariantViewModel create(final LineItem lineItem) {
        return super.create(lineItem);
    }

    @Override
    protected final void initialize(final ProductVariantViewModel viewModel, final LineItem lineItem) {
        super.initialize(viewModel, lineItem);
    }

    @Override
    protected void fillSku(final ProductVariantViewModel model, final LineItem lineItem) {
        model.setSku(findSku(lineItem.getVariant()));
    }

    @Override
    protected void fillName(final ProductVariantViewModel model, final LineItem lineItem) {
        model.setName(lineItem.getName());
    }

    @Override
    protected void fillUrl(final ProductVariantViewModel model, final LineItem lineItem) {
        model.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(lineItem));
    }

    @Override
    protected void fillImage(final ProductVariantViewModel model, final LineItem lineItem) {
        findImageUrl(lineItem.getVariant()).ifPresent(model::setImage);
    }

    @Override
    protected void fillPrice(final ProductVariantViewModel model, final LineItem lineItem) {
        model.setPrice(priceFormatter.format(ProductPriceUtils.calculateAppliedProductPrice(lineItem)));
    }

    @Override
    protected void fillPriceOld(final ProductVariantViewModel model, final LineItem lineItem) {
        ProductPriceUtils.calculatePreviousProductPrice(lineItem)
                .ifPresent(oldPrice -> model.setPriceOld(priceFormatter.format(oldPrice)));
    }
}