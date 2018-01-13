package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.sessions.AbstractResourceInSession;
import com.commercetools.sunrise.core.sessions.StoringStrategy;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.Configuration;

import javax.inject.Inject;

final class MyWishlistInSessionImpl extends AbstractResourceInSession<ShoppingList> implements MyWishlistInSession {

    @Inject
    MyWishlistInSessionImpl(final Configuration globalConfig, final StoringStrategy storingStrategy) {
        super(globalConfig.getConfig("sunrise.wishlists"), storingStrategy);
    }
}
