package com.commercetools.sunrise.shoppinglist.content;

import com.commercetools.sunrise.framework.controllers.SunriseContentController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppinglist.ShoppingListFinder;
import com.commercetools.sunrise.shoppinglist.ShoppingListTypeIdentifier;
import com.commercetools.sunrise.shoppinglist.WithRequiredShoppingList;
import com.commercetools.sunrise.shoppinglist.content.viewmodels.ShoppingListPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current shoppinglist.
 */
public abstract class SunriseShoppingListContentController extends SunriseContentController implements WithQueryFlow<ShoppingList>, WithRequiredShoppingList,ShoppingListTypeIdentifier {

    private final ShoppingListFinder shoppingListFinder;
    private final ShoppingListPageContentFactory shoppingListPageContentFactory;

    @Inject
    protected SunriseShoppingListContentController(final ContentRenderer contentRenderer,
                                                   final ShoppingListPageContentFactory shoppingListPageContentFactory,
                                                   final ShoppingListFinder shoppingListFinder) {
        super(contentRenderer);
        this.shoppingListPageContentFactory = shoppingListPageContentFactory;
        this.shoppingListFinder = shoppingListFinder;
    }

    @Override
    public final ShoppingListFinder getShoppingListFinder() {
        return shoppingListFinder;
    }

    @Override
    public PageContent createPageContent(final ShoppingList wishlist) {
        return shoppingListPageContentFactory.create(wishlist);
    }

    @Override
    public CompletionStage<Result> handleNotFoundShoppingList() {
        return okResultWithPageContent(shoppingListPageContentFactory.create(null));
    }
}
