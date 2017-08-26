package controllers.wishlist;

import com.commercetools.sunrise.shoppinglist.ShoppingListTypeIdentifier;

public interface WishlistTypeIdentifier extends ShoppingListTypeIdentifier {

    @Override
    default String getShoppingListType() {
        return "wishlist";
    }

}
