package com.commercetools.sunrise.wishlist.add;

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

public abstract class SunriseAddToWishlistController extends SunriseContentFormController implements WithContentForm2Flow<Void, ShoppingList, AddToWishlistFormData> {

    private final AddToWishlistFormData formData;
    private final AddToWishlistControllerAction controllerAction;

    @Inject
    protected SunriseAddToWishlistController(final ContentRenderer contentRenderer,
                                             final FormFactory formFactory,
                                             final AddToWishlistFormData formData,
                                             final AddToWishlistControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends AddToWishlistFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.ADD_TO_WISHLIST_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<ShoppingList> executeAction(final Void input, final AddToWishlistFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final AddToWishlistFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends AddToWishlistFormData> formData) {
        return new BlankPageContent();
    }

    @Override
    public void preFillFormData(final Void input, final AddToWishlistFormData formData) {
      // Do not pre-fill anything
    }
}
