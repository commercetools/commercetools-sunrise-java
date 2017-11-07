package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContent;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.ctp.CtpExceptionUtils.isDiscountCodeNonApplicableError;

public abstract class SunriseAddDiscountCodeController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Cart, AddDiscountCodeFormData>, WithRequiredCart {

    private final AddDiscountCodeFormData formData;
    private final CartFinder cartFinder;
    private final CartPageContentFactory pageContentFactory;
    private final AddDiscountCodeControllerAction controllerAction;

    protected SunriseAddDiscountCodeController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                               final AddDiscountCodeFormData formData, final CartFinder cartFinder,
                                               final CartPageContentFactory pageContentFactory,
                                               final AddDiscountCodeControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.pageContentFactory = pageContentFactory;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends AddDiscountCodeFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.ADD_DISCOUNT_CODE_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final AddDiscountCodeFormData formData) {
        return controllerAction.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final AddDiscountCodeFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends AddDiscountCodeFormData> form) {
        final CartPageContent cartPageContent = pageContentFactory.create(cart, form);
        return cartPageContent;
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Cart cart,
                                                                 final Form<? extends AddDiscountCodeFormData> form,
                                                                 final ClientErrorException clientErrorException) {
        if (isDiscountCodeNonApplicableError(clientErrorException)) {
            return handleDiscountCodeNonApplicable(cart, form, clientErrorException);
        } else {
            return WithContentFormFlow.super.handleClientErrorFailedAction(cart, form, clientErrorException);
        }
    }

    protected abstract CompletionStage<Result> handleDiscountCodeNonApplicable(final Cart cart,
                                                                    final Form<? extends AddDiscountCodeFormData> form,
                                                                    final ClientErrorException clientErrorException);

    @Override
    public void preFillFormData(final Cart cart, final AddDiscountCodeFormData formData) {
        // Do not pre-fill anything
    }
}
