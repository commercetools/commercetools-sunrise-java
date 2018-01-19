package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.myaccount.customerprofile.CustomerProfileFormAction;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class CustomerProfileController extends SunriseController {

    private static final String TEMPLATE = "my-account-personal-details";

    private final TemplateEngine templateEngine;
    private final CustomerProfileFormAction formAction;

    @Inject
    CustomerProfileController(final TemplateEngine templateEngine,
                              final CustomerProfileFormAction formAction) {
        this.templateEngine = templateEngine;
        this.formAction = formAction;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render(TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> edit() {
        return formAction.apply(
                () -> routes.CustomerProfileController.show(),
                form -> templateEngine.render(TEMPLATE, PageData.of().put("editProfileForm", form)));
    }

    //    // TODO prefill in templates
//    @Override
//    public void preFillFormData(final Void input, final CustomerProfileFormData formData) {
//        formData.applyCustomerName(customer.getName());
//        formData.applyEmail(customer.getEmail());
//    }
}
