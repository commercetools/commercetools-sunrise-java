package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.sessions.AbstractResourceInSession;
import com.commercetools.sunrise.core.sessions.StoringStrategy;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.Configuration;

import javax.inject.Inject;

final class WishlistInSessionImpl extends AbstractResourceInSession<ShoppingList> implements WishlistInSession {

    @Inject
    WishlistInSessionImpl(final Configuration globalConfig, final StoringStrategy storingStrategy) {
        super(globalConfig.getConfig("sunrise.wishlists"), storingStrategy);
    }
}
