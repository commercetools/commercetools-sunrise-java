package com.commercetools.sunrise.wishlist.remove;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentForm2Flow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveFromWishlistController extends SunriseContentFormController implements WithContentForm2Flow<Void, ShoppingList, RemoveFromWishlistFormData> {

    private final RemoveFromWishlistFormData formData;
    private final RemoveFromWishlistFormAction controllerAction;

    @Inject
    protected SunriseRemoveFromWishlistController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                  final RemoveFromWishlistFormData formData,
                                                  final RemoveFromWishlistFormAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends RemoveFromWishlistFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.REMOVE_FROM_WISHLIST_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<ShoppingList> executeAction(final Void input, final RemoveFromWishlistFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final RemoveFromWishlistFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends RemoveFromWishlistFormData> formData) {
        return new BlankPageContent();
    }

    @Override
    public void preFillFormData(final Void input, final RemoveFromWishlistFormData formData) {
        // Do not pre-fill anything
    }
}
