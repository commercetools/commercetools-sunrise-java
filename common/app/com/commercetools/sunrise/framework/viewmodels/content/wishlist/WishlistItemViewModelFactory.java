package com.commercetools.sunrise.framework.viewmodels.content.wishlist;

import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.shoppinglists.LineItem;
import play.mvc.Call;

import javax.inject.Inject;

/**
 * This view model factory creates {@link WishlistItemViewModel} instances for the product variants of
 * the wishlists line items {@link LineItem#getVariant()}.
 */
public class WishlistItemViewModelFactory extends SimpleViewModelFactory<WishlistItemViewModel, LineItem> {


    @Inject
    private ProductReverseRouter productReverseRouter;


    @Override
    public final WishlistItemViewModel create(final LineItem lineItem) {
        return super.create(lineItem);
    }

    @Override
    protected WishlistItemViewModel newViewModelInstance(final LineItem lineItem) {
        return new WishlistItemViewModel();
    }

    @Override
    protected final void initialize(final WishlistItemViewModel viewModel, final LineItem lineItem) {
        fillImageUrl(viewModel, lineItem);
        fillProductSlug(viewModel, lineItem);
    }

    protected void fillImageUrl(final WishlistItemViewModel viewModel, final LineItem lineItem) {
        if (lineItem != null && lineItem.getVariant() != null) {
            final String imageUrl = lineItem.getVariant().getImages().stream()
                    .findFirst()
                    .map(image -> image.getUrl()).orElse(null);

            viewModel.setImageUrl(imageUrl);
        }
    }

    protected void fillProductSlug(final WishlistItemViewModel viewModel, final LineItem lineItem) {
        if (lineItem != null) {
            final String productSlug = productReverseRouter
                    .productDetailPageCall(lineItem)
                    .map(Call::url)
                    .orElse("");
            viewModel.setUrl(productSlug);
        }
    }


}
