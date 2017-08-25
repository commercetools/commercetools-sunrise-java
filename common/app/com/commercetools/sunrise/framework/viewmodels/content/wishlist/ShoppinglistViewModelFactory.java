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
 * This view model factory creates {@link ShoppinglistViewModel} instances for wishlists {@link ShoppingList}.
 */
public class ShoppinglistViewModelFactory extends SimpleViewModelFactory<ShoppinglistViewModel, ShoppingList> {
    private ShoppinglistItemViewModelFactory shoppinglistItemViewModelFactory;

    private final int recentlyAddedItems;
    private final Locale locale;

    @Inject
    protected ShoppinglistViewModelFactory(final ShoppinglistItemViewModelFactory shoppinglistItemViewModelFactory,
                                           final Configuration configuration,
                                           final Locale locale) {
        this.shoppinglistItemViewModelFactory = shoppinglistItemViewModelFactory;
        this.recentlyAddedItems = configuration.getInt("wishlist.recentlyAddedItems");
        this.locale = locale;
    }

    @Override
    public final ShoppinglistViewModel create(final ShoppingList wishlist) {
        return super.create(wishlist);
    }

    @Override
    protected ShoppinglistViewModel newViewModelInstance(final ShoppingList wishlist) {
        return new ShoppinglistViewModel();
    }

    @Override
    protected final void initialize(final ShoppinglistViewModel viewModel, final ShoppingList wishlist) {
        fillTitle(viewModel, wishlist);
        fillId(viewModel, wishlist);
        fillQuantity(viewModel, wishlist);
        fillLineItems(viewModel, wishlist);
    }

    protected void fillLineItems(final ShoppinglistViewModel viewModel, final ShoppingList wishlist) {
        if (wishlist != null) {
            final List<LineItem> lineItems = new ArrayList<>(wishlist.getLineItems());
            Collections.reverse(lineItems);

            final int lastIndex = lineItems.size() > recentlyAddedItems ? recentlyAddedItems : lineItems.size();

            final List<LineItem> recentlyAdded = lineItems.subList(0, lastIndex);
            final List<ShoppinglistItemViewModel> shoppinglistItemViewModels = recentlyAdded.stream()
                    .map(shoppinglistItemViewModelFactory::create)
                    .collect(Collectors.toList());

            viewModel.setList(shoppinglistItemViewModels);
        }
    }

    protected void fillQuantity(final ShoppinglistViewModel viewModel, final ShoppingList wishlist) {
        if (wishlist != null) {
            final List<LineItem> lineItems = wishlist.getLineItems();

            viewModel.setQuantity(lineItems.size());
        }
    }

    protected void fillTitle(final ShoppinglistViewModel viewModel, final ShoppingList wishlist) {
        if (wishlist != null) {
            final String title = wishlist.getName().get(locale);
            viewModel.setTitle(title);
        }
    }

    protected void fillId(final ShoppinglistViewModel viewModel, final ShoppingList wishlist) {
        if (wishlist != null) {
            final String id = wishlist.getId();
            viewModel.setId(id);
        }
    }
}
