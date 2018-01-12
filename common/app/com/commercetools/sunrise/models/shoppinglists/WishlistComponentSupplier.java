package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.components.Component;
import com.commercetools.sunrise.core.components.ComponentSupplier;

import java.util.List;

import static java.util.Arrays.asList;

public final class WishlistComponentSupplier implements ComponentSupplier {

    public static List<Class<? extends Component>> get() {
        return asList(
                WishlistStoringComponent.class,
                WishlistDisplayingComponent.class,
                ShoppingListLineItemExpansionComponent.class);
    }

}
