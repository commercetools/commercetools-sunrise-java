package controllers.hololist;

import com.commercetools.sunrise.shoppinglist.ShoppingListTypeIdentifier;

public interface HololistTypeIdentifier extends ShoppingListTypeIdentifier {

    String SHOPPINGLIST_IDENTIFIER = "hololist";

    @Override
    default String getShoppingListType() {
        return SHOPPINGLIST_IDENTIFIER;
    }

}
