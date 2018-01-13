package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.shoppingcart.discountcodes.AddDiscountCodeFormAction;
import com.commercetools.sunrise.shoppingcart.discountcodes.RemoveDiscountCodeFormAction;
import com.commercetools.sunrise.shoppingcart.discountcodes.SunriseDiscountCodesController;
import com.google.inject.Inject;
import play.mvc.Result;

@LogMetrics
@NoCache
public final class DiscountCodesController extends SunriseDiscountCodesController {

    @Inject
    DiscountCodesController(final TemplateEngine templateEngine,
                            final AddDiscountCodeFormAction addDiscountCodeFormAction,
                            final RemoveDiscountCodeFormAction removeDiscountCodeFormAction) {
        super(templateEngine, addDiscountCodeFormAction, removeDiscountCodeFormAction);
    }

    @Override
    protected Result onAdded() {
        return redirect(routes.CartController.show());
    }

    @Override
    protected Result onRemoved() {
        return redirect(routes.CartController.show());
    }
}
