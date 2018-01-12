package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentForm2Flow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sdk.errors.ErrorResponseExceptionUtils.isDiscountCodeNonApplicableError;

public abstract class SunriseAddDiscountCodeController extends SunriseContentFormController implements WithContentForm2Flow<Void, Cart, AddDiscountCodeFormData> {

    private final AddDiscountCodeFormData formData;
    private final AddDiscountCodeControllerAction controllerAction;

    protected SunriseAddDiscountCodeController(final ContentRenderer contentRenderer,
                                               final FormFactory formFactory,
                                               final AddDiscountCodeFormData formData,
                                               final AddDiscountCodeControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends AddDiscountCodeFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.ADD_DISCOUNT_CODE_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null); // TODO it was requiring non-empty cart
    }

    @Override
    public CompletionStage<Cart> executeAction(final Void input, final AddDiscountCodeFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final AddDiscountCodeFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends AddDiscountCodeFormData> form) {
        return new BlankPageContent();
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input,
                                                                 final Form<? extends AddDiscountCodeFormData> form,
                                                                 final ClientErrorException clientErrorException) {
        if (isDiscountCodeNonApplicableError(clientErrorException)) {
            return handleDiscountCodeNonApplicable(input, form, clientErrorException);
        } else {
            return WithContentForm2Flow.super.handleClientErrorFailedAction(input, form, clientErrorException);
        }
    }

    protected abstract CompletionStage<Result> handleDiscountCodeNonApplicable(final Void input,
                                                                    final Form<? extends AddDiscountCodeFormData> form,
                                                                    final ClientErrorException clientErrorException);

    @Override
    public void preFillFormData(final Void input, final AddDiscountCodeFormData formData) {
        // Do not pre-fill anything
    }
}
