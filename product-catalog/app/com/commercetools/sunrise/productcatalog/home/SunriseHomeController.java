package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

/**
 * Controller for the home page.
 */
public abstract class SunriseHomeController extends SunriseController {

    private final TemplateEngine templateEngine;

    protected SunriseHomeController(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render("home")
                .thenApply(Results::ok);
    }
}
