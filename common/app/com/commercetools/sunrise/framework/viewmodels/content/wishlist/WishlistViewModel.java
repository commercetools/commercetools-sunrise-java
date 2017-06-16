package com.commercetools.sunrise.framework.viewmodels.content.wishlist;

import com.commercetools.sunrise.framework.viewmodels.GenericListViewModel;

/**
 * This view model is used to render the mini wishlist view.
 */
public class WishlistViewModel extends GenericListViewModel<WishlistItemViewModel> {
    private String title;

    private Integer quantity;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }
}
