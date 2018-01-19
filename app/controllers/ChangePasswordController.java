package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.myaccount.changepassword.ChangePasswordFormAction;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class ChangePasswordController extends SunriseController {

    private static final String TEMPLATE = "my-account-change-password";

    private final TemplateEngine templateEngine;
    private final ChangePasswordFormAction formAction;

    @Inject
    ChangePasswordController(final TemplateEngine templateEngine,
                             final ChangePasswordFormAction formAction) {
        this.templateEngine = templateEngine;
        this.formAction = formAction;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render(TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> change() {
        return formAction.apply(
                () -> routes.ChangePasswordController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("changePasswordForm", form)));
    }
}
