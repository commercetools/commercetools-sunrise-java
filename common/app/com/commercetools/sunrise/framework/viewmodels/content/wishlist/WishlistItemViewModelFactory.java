package com.commercetools.sunrise.framework.viewmodels.content.wishlist;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.shoppinglists.LineItem;

/**
 * This view model factory creates {@link WishlistItemViewModel} instances for the product variants of
 * the wishlists line items {@link LineItem#getVariant()}.
 */
public class WishlistItemViewModelFactory extends SimpleViewModelFactory<WishlistItemViewModel, ProductVariant> {

    @Override
    public final WishlistItemViewModel create(final ProductVariant productVariant) {
        return super.create(productVariant);
    }

    @Override
    protected WishlistItemViewModel newViewModelInstance(final ProductVariant productVariant) {
        return new WishlistItemViewModel();
    }

    @Override
    protected final void initialize(final WishlistItemViewModel viewModel, final ProductVariant productVariant) {
        fillImageUrl(viewModel, productVariant);
    }

    protected void fillImageUrl(final WishlistItemViewModel viewModel, final ProductVariant productVariant) {
        if (productVariant != null) {
            final String imageUrl = productVariant.getImages().stream()
                    .findFirst()
                    .map(image -> image.getUrl()).orElse(null);

            viewModel.setImageUrl(imageUrl);
        }
    }
}
