package com.commercetools.sunrise.sessions.wishlist;

import com.commercetools.sunrise.framework.viewmodels.content.wishlist.ShoppinglistViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.wishlist.ShoppinglistViewModelFactory;
import com.commercetools.sunrise.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.sessions.ObjectStoringSessionStrategy;
import io.sphere.sdk.shoppinglists.ShoppingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * Default implementation of {@link ShoppingListsInSession}.
 */
public class DefaultShoppingListInSession extends DataFromResourceStoringOperations<ShoppingList> implements ShoppingListsInSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListsInSession.class);
    private static final String DEFAULT_SHOPPINGLIST_CONTAINER_SESSION_KEY = "sunrise-shoppinglist";

    private final String shoppingListsSessionKey;
    private final ObjectStoringSessionStrategy session;
    private final ShoppinglistViewModelFactory shoppinglistViewModelFactory;

    @Inject
    protected DefaultShoppingListInSession(final Configuration configuration, final ObjectStoringSessionStrategy session,
                                           final ShoppinglistViewModelFactory shoppinglistViewModelFactory) {
        this.shoppingListsSessionKey = configuration.getString("session.shoppinglist.container", DEFAULT_SHOPPINGLIST_CONTAINER_SESSION_KEY);
        this.session = session;
        this.shoppinglistViewModelFactory = shoppinglistViewModelFactory;
    }

    @Override
    public Optional<String> findShoppingListId(String shoppingListType){
        return findShoppingList(shoppingListType).map(ShoppinglistViewModel::getId);
    }

    @Override
    public Optional<ShoppinglistViewModel> findShoppingList(String shoppingListType) {
        return getShoppinglistsModelContainerInSession()
                .map(shoppingListsModelContainerContainer -> shoppingListsModelContainerContainer.getShoppingList(shoppingListType))
                .flatMap(Function.identity());
    }

    private Optional<ShoppingListContainer> getShoppinglistsModelContainerInSession(){
        return session.findObjectByKey(shoppingListsSessionKey, ShoppingListContainer.class);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void storeAssociatedData(@NotNull final ShoppingList shoppingList) {

        ShoppinglistViewModel shoppinglistViewModel = shoppinglistViewModelFactory.create(shoppingList);
        ShoppingListContainer shoppingListContainer = getShoppinglistsModelContainerInSession().orElseGet(ShoppingListContainer::new);
        shoppingListContainer.addShoppingList(shoppingList.getName().get(Locale.ENGLISH).toLowerCase(),shoppinglistViewModel);
        session.overwriteObjectByKey(shoppingListsSessionKey, shoppingListContainer);
    }

    @Override
    protected void removeAssociatedData() {
        session.removeObjectByKey(shoppingListsSessionKey);
    }
}
