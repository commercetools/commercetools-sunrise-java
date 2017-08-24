package com.commercetools.sunrise.framework.viewmodels.content.wishlist;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.shoppinglists.LineItem;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.Configuration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * This view model factory creates {@link WishlistViewModel} instances for wishlists {@link ShoppingList}.
 */
public class WishlistViewModelFactory extends SimpleViewModelFactory<WishlistViewModel, ShoppingList> {
    private WishlistItemViewModelFactory wishlistItemViewModelFactory;

    private final int recentlyAddedItems;
    private final Locale locale;

    @Inject
    protected WishlistViewModelFactory(final WishlistItemViewModelFactory wishlistItemViewModelFactory,
                                       final Configuration configuration,
                                       final Locale locale) {
        this.wishlistItemViewModelFactory = wishlistItemViewModelFactory;
        this.recentlyAddedItems = configuration.getInt("wishlist.recentlyAddedItems");
        this.locale = locale;
    }

    @Override
    public final WishlistViewModel create(final ShoppingList wishlist) {
        return super.create(wishlist);
    }

    @Override
    protected WishlistViewModel newViewModelInstance(final ShoppingList wishlist) {
        return new WishlistViewModel();
    }

    @Override
    protected final void initialize(final WishlistViewModel viewModel, final ShoppingList wishlist) {
        fillTitle(viewModel, wishlist);


        fillQuantity(viewModel, wishlist);
        fillLineItems(viewModel, wishlist);
    }

    protected void fillLineItems(final WishlistViewModel viewModel, final ShoppingList wishlist) {
        if (wishlist != null) {
            final List<LineItem> lineItems = new ArrayList<>(wishlist.getLineItems());
            Collections.reverse(lineItems);

            final int lastIndex = lineItems.size() > recentlyAddedItems ? recentlyAddedItems : lineItems.size();

            final List<LineItem> recentlyAdded = lineItems.subList(0, lastIndex);
            final List<WishlistItemViewModel> wishlistItemViewModels = recentlyAdded.stream()
                    .map(wishlistItemViewModelFactory::create)
                    .collect(Collectors.toList());

            viewModel.setList(wishlistItemViewModels);
        }
    }

    protected void fillQuantity(final WishlistViewModel viewModel, final ShoppingList wishlist) {
        if (wishlist != null) {
            final List<LineItem> lineItems = wishlist.getLineItems();

            viewModel.setQuantity(lineItems.size());
        }
    }

    protected void fillTitle(final WishlistViewModel viewModel, final ShoppingList wishlist) {
        if (wishlist != null) {
            final String title = wishlist.getName().get(locale);
            viewModel.setTitle(title);
        }
    }
}
