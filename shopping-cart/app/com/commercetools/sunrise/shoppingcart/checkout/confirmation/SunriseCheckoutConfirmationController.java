package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.viewmodels.CheckoutConfirmationPageContentFactory;
import io.sphere.sdk.orders.Order;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutConfirmationController extends SunriseContentFormController implements WithContentFormFlow<Void, Order, CheckoutConfirmationFormData> {

    private final CheckoutConfirmationFormData formData;
    private final CheckoutConfirmationControllerAction controllerAction;
    private final CheckoutConfirmationPageContentFactory pageContentFactory;

    protected SunriseCheckoutConfirmationController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                    final CheckoutConfirmationFormData formData,
                                                    final CheckoutConfirmationControllerAction controllerAction,
                                                    final CheckoutConfirmationPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends CheckoutConfirmationFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_CONFIRMATION_PAGE)
    public CompletionStage<Result> show() {
        return showFormPage(null, formData); // TODO it required non-empty cart
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_CONFIRMATION_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null); // TODO it required non-empty cart
    }

    @Override
    public CompletionStage<Order> executeAction(final Void input, final CheckoutConfirmationFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Order order, final CheckoutConfirmationFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends CheckoutConfirmationFormData> form) {
        return pageContentFactory.create(null, form);
    }

    @Override
    public void preFillFormData(final Void input, final CheckoutConfirmationFormData formData) {
        // Do not pre-fill anything
    }
}
