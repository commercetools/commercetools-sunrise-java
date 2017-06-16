package com.commercetools.sunrise.framework.viewmodels.content.wishlist;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

/**
 * This view model is used to render the line items of the mini wishlist view.
 */
public class WishlistItemViewModel extends ViewModel {
    private String imageUrl;

    private String name;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
