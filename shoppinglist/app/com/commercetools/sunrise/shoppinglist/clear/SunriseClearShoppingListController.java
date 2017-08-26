package com.commercetools.sunrise.shoppinglist.clear;

import com.commercetools.sunrise.framework.controllers.SunriseController;
import com.commercetools.sunrise.framework.controllers.WithExecutionFlow;
import com.commercetools.sunrise.shoppinglist.ShoppingListFinder;
import com.commercetools.sunrise.shoppinglist.ShoppingListTypeIdentifier;
import com.commercetools.sunrise.shoppinglist.WithRequiredShoppingList;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current wishlist.
 */
public abstract class SunriseClearShoppingListController extends SunriseController
        implements WithExecutionFlow<ShoppingList, ShoppingList>, WithRequiredShoppingList, ShoppingListTypeIdentifier {

    private final ShoppingListFinder shoppingListFinder;
    private final ClearShoppingListControllerAction controllerAction;

    @Inject
    protected SunriseClearShoppingListController(final ShoppingListFinder shoppingListFinder,
                                                 final ClearShoppingListControllerAction controllerAction) {
        this.shoppingListFinder = shoppingListFinder;
        this.controllerAction = controllerAction;
    }

    @Override
    public final ShoppingListFinder getShoppingListFinder() {
        return shoppingListFinder;
    }


    @Override
    public CompletionStage<ShoppingList> executeAction(final ShoppingList wishlist) {
        return controllerAction.apply(wishlist);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist);

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final ShoppingList wishlist, final ClientErrorException clientErrorException) {
        return handleGeneralFailedAction(clientErrorException);
    }
}
