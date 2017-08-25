package controllers.wishlist;

import com.commercetools.sunrise.wishlist.ShoppingListTypeIdentifier;

public interface WishlistTypeIdentifier extends ShoppingListTypeIdentifier {

    @Override
    default String getShoppingListType() {
        return "wishlist";
    }

}
