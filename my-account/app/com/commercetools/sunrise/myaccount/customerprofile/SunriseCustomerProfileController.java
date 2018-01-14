package com.commercetools.sunrise.myaccount.customerprofile;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCustomerProfileController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final CustomerProfileFormAction formAction;

    protected SunriseCustomerProfileController(final TemplateEngine templateEngine,
                                               final CustomerProfileFormAction formAction) {
        this.templateEngine = templateEngine;
        this.formAction = formAction;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render("my-account-personal-details")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> edit() {
        return formAction.apply(this::onEdited,
                form -> {
                    final PageData pageData = PageData.of().put("editProfileForm", form);
                    return templateEngine.render("my-account-personal-details", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    protected abstract Result onEdited();

//    // TODO prefill in templates
//    @Override
//    public void preFillFormData(final Void input, final CustomerProfileFormData formData) {
//        formData.applyCustomerName(customer.getName());
//        formData.applyEmail(customer.getEmail());
//    }
}
