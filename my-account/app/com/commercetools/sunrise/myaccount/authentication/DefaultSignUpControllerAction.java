package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.core.controllers.AbstractControllerAction;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.models.customers.CustomerCreator;
import com.commercetools.sunrise.models.customers.SignUpFormData;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sdk.errors.ErrorResponseExceptionUtils.isDuplicatedEmailFieldError;

public class DefaultSignUpControllerAction extends AbstractControllerAction<SignUpFormData> implements SignUpControllerAction {

    private final SignUpFormData formData;
    private final CustomerCreator customerCreator;

    @Inject
    protected DefaultSignUpControllerAction(final FormFactory formFactory, final TemplateEngine templateEngine,
                                            final SignUpFormData formData, final CustomerCreator customerCreator) {
        super(formFactory, templateEngine);
        this.formData = formData;
        this.customerCreator = customerCreator;
    }

    @Override
    protected Class<? extends SignUpFormData> formClass() {
        return formData.getClass();
    }

    @Override
    protected String formName() {
        return "signUpForm";
    }

    @Override
    protected CompletionStage<?> executeRequest(final SignUpFormData formData) {
        return customerCreator.get(formData.customerDraft());
    }

    @Override
    protected CompletionStage<Result> handleFailedRequest(final Form<? extends SignUpFormData> form, final Throwable throwable) {
        if (isDuplicatedEmailFieldError(throwable.getCause())) {
            form.reject("errors.emailAlreadyExists");
        }
        return super.handleFailedRequest(form, throwable);
    }
}
