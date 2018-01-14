package com.commercetools.sunrise.shoppingcart.discountcodes;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseDiscountCodesController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final AddDiscountCodeFormAction addDiscountCodeFormAction;
    private final RemoveDiscountCodeFormAction removeDiscountCodeFormAction;

    protected SunriseDiscountCodesController(final TemplateEngine templateEngine,
                                             final AddDiscountCodeFormAction addDiscountCodeFormAction,
                                             final RemoveDiscountCodeFormAction removeDiscountCodeFormAction) {
        this.templateEngine = templateEngine;
        this.addDiscountCodeFormAction = addDiscountCodeFormAction;
        this.removeDiscountCodeFormAction = removeDiscountCodeFormAction;
    }

    @EnableHooks
    public CompletionStage<Result> add() {
        return addDiscountCodeFormAction.apply(this::onAdded,
                form -> {
                    final PageData pageData = PageData.of().put("addDiscountCodeForm", form);
                    return templateEngine.render("cart", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> remove() {
        return removeDiscountCodeFormAction.apply(this::onRemoved,
                form -> {
                    final PageData pageData = PageData.of().put("removeDiscountCodeForm", form);
                    return templateEngine.render("cart", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    protected abstract Result onAdded();

    protected abstract Result onRemoved();
}
