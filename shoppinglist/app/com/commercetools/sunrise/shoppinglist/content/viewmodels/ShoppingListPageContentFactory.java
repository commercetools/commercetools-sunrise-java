package com.commercetools.sunrise.shoppinglist.content.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.GenericListViewModel;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.framework.viewmodels.content.PageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductThumbnailViewModel;
import io.sphere.sdk.shoppinglists.LineItem;
import io.sphere.sdk.shoppinglists.ShoppingList;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The factory class for creating {@link ShoppingListPageContent}.
 */
public class ShoppingListPageContentFactory extends PageContentFactory<ShoppingListPageContent, ShoppingList> {
    private final PageTitleResolver pageTitleResolver;
    private final LineItemThumbnailViewModelFactory thumbnailViewModelFactory;

    @Inject
    protected ShoppingListPageContentFactory(final PageTitleResolver pageTitleResolver, final LineItemThumbnailViewModelFactory thumbnailViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.thumbnailViewModelFactory = thumbnailViewModelFactory;
    }

    @Override
    protected ShoppingListPageContent newViewModelInstance(final ShoppingList input) {
        return new ShoppingListPageContent();
    }

    @Override
    public final ShoppingListPageContent create(final ShoppingList wishlist) {
        return super.create(wishlist);
    }

    @Override
    protected final void initialize(final ShoppingListPageContent viewModel, final ShoppingList wishlist) {
        super.initialize(viewModel, wishlist);

        fillProducts(viewModel, wishlist);
        fillItemsInTotal(viewModel, wishlist);
    }

    @Override
    protected void fillTitle(final ShoppingListPageContent viewModel, final ShoppingList wishlist) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("my-account:myWishlist.title"));
    }

    protected void fillItemsInTotal(final ShoppingListPageContent viewModel, final ShoppingList wishlist) {
        final List<LineItem> lineItems = wishlist != null ? wishlist.getLineItems() : null;
        viewModel.setItemsInTotal(lineItems == null ? 0 : lineItems.size());
    }

    protected void fillProducts(final ShoppingListPageContent viewModel, final ShoppingList wishlist) {
        if (wishlist != null) {
            final GenericListViewModel<ProductThumbnailViewModel> productList = new GenericListViewModel<>();
            final List<LineItem> lineItems = wishlist.getLineItems();
            final List<ProductThumbnailViewModel> productThumbNails = lineItems == null ?
                    Collections.emptyList() :
                    lineItems.stream()
                            .map(thumbnailViewModelFactory::create)
                            .collect(Collectors.toList());
            productList.setList(productThumbNails);
            viewModel.setProducts(productList);
        }
    }
}
