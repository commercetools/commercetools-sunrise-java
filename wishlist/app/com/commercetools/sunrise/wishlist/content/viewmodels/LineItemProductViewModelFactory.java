package com.commercetools.sunrise.wishlist.content.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductGalleryViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductViewModel;
import io.sphere.sdk.shoppinglists.LineItem;

import javax.inject.Inject;

public class LineItemProductViewModelFactory extends SimpleViewModelFactory<ProductViewModel, LineItem> {
    private final LineItemProductVariantViewModelFactory productVariantViewModelFactory;
    private final ProductGalleryViewModelFactory productGalleryViewModelFactory;

    @Inject
    protected LineItemProductViewModelFactory(final LineItemProductVariantViewModelFactory productVariantViewModelFactory,
                                              final ProductGalleryViewModelFactory productGalleryViewModelFactory) {
        this.productVariantViewModelFactory = productVariantViewModelFactory;
        this.productGalleryViewModelFactory = productGalleryViewModelFactory;
    }

    @Override
    protected ProductViewModel newViewModelInstance(final LineItem lineItem) {
        return new ProductViewModel();
    }

    @Override
    public final ProductViewModel create(final LineItem lineItem) {
        return super.create(lineItem);
    }

    @Override
    protected final void initialize(final ProductViewModel viewModel, final LineItem lineItem) {
        fillProductId(viewModel, lineItem);
        fillVariantId(viewModel, lineItem);
        fillVariant(viewModel, lineItem);

        fillGallery(viewModel, lineItem);

        fillLineItem(viewModel, lineItem);
    }

    protected void fillLineItem(final ProductViewModel viewModel, final LineItem lineItem) {
        viewModel.put("lineItemId", lineItem.getId());
    }

    protected void fillGallery(final ProductViewModel viewModel, final LineItem lineItem) {
        viewModel.setGallery(productGalleryViewModelFactory.create(lineItem.getVariant()));
    }

    protected void fillVariant(final ProductViewModel viewModel, final LineItem lineItem) {
        viewModel.setVariant(productVariantViewModelFactory.create(lineItem));
    }

    protected void fillVariantId(final ProductViewModel viewModel, final LineItem lineItem) {
        viewModel.setVariantId(lineItem.getVariantId());
    }

    protected void fillProductId(final ProductViewModel viewModel, final LineItem lineItem) {
        viewModel.setProductId(lineItem.getProductId());
    }
}
