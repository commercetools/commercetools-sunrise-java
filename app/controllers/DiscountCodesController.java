package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.shoppingcart.discountcodes.AddDiscountCodeFormAction;
import com.commercetools.sunrise.shoppingcart.discountcodes.RemoveDiscountCodeFormAction;
import com.google.inject.Inject;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class DiscountCodesController extends SunriseController {

    private static final String TEMPLATE = "cart";

    private final TemplateEngine templateEngine;
    private final AddDiscountCodeFormAction addDiscountCodeFormAction;
    private final RemoveDiscountCodeFormAction removeDiscountCodeFormAction;

    @Inject
    DiscountCodesController(final TemplateEngine templateEngine,
                            final AddDiscountCodeFormAction addDiscountCodeFormAction,
                            final RemoveDiscountCodeFormAction removeDiscountCodeFormAction) {
        this.templateEngine = templateEngine;
        this.addDiscountCodeFormAction = addDiscountCodeFormAction;
        this.removeDiscountCodeFormAction = removeDiscountCodeFormAction;
    }

    @EnableHooks
    public CompletionStage<Result> add() {
        return addDiscountCodeFormAction.apply(
                () -> routes.CartController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("addDiscountCodeForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> remove() {
        return removeDiscountCodeFormAction.apply(
                () -> routes.CartController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("removeDiscountCodeForm", form)));
    }
}
