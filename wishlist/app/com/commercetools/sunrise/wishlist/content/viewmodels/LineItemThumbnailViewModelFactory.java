package com.commercetools.sunrise.wishlist.content.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductThumbnailViewModel;
import io.sphere.sdk.shoppinglists.LineItem;

import javax.inject.Inject;

public class LineItemThumbnailViewModelFactory extends SimpleViewModelFactory<ProductThumbnailViewModel, LineItem> {
    private final LineItemProductViewModelFactory lineItemProductViewModelFactory;

    @Inject
    protected LineItemThumbnailViewModelFactory(final LineItemProductViewModelFactory lineItemProductViewModelFactory) {
        this.lineItemProductViewModelFactory = lineItemProductViewModelFactory;
    }

    @Override
    public final ProductThumbnailViewModel create(final LineItem lineItem) {
        return super.create(lineItem);
    }

    @Override
    protected ProductThumbnailViewModel newViewModelInstance(final LineItem lineItem) {
        return new ProductThumbnailViewModel();
    }

    @Override
    protected final void initialize(final ProductThumbnailViewModel viewModel, final LineItem lineItem) {
        fillProduct(viewModel, lineItem);
    }

    protected void fillProduct(final ProductThumbnailViewModel viewModel, final LineItem lineItem) {
        viewModel.setProduct(lineItemProductViewModelFactory.create(lineItem));
    }
}
