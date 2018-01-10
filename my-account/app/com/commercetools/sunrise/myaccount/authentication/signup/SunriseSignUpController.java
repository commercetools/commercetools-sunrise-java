package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import com.commercetools.sunrise.models.customers.SignUpFormData;
import com.commercetools.sunrise.myaccount.MyAccountController;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static com.commercetools.sdk.errors.ErrorResponseExceptionUtils.isDuplicatedEmailFieldError;

public abstract class SunriseSignUpController extends SunriseContentFormController implements MyAccountController, WithContentFormFlow<Void, CustomerSignInResult, SignUpFormData> {

    private final SignUpFormData formData;
    private final SignUpControllerAction controllerAction;

    protected SunriseSignUpController(final ContentRenderer contentRenderer,
                                      final FormFactory formFactory,
                                      final SignUpFormData formData,
                                      final SignUpControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends SignUpFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(AuthenticationReverseRouter.SIGN_UP_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<CustomerSignInResult> executeAction(final Void input, final SignUpFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<? extends SignUpFormData> form, final ClientErrorException clientErrorException) {
        if (isDuplicatedEmailFieldError(clientErrorException)) {
            saveFormError(form, "A user with this email already exists"); // TODO i18n
            return showFormPageWithErrors(input, form);
        } else {
            return WithContentFormFlow.super.handleClientErrorFailedAction(input, form, clientErrorException);
        }
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final CustomerSignInResult result, final SignUpFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends SignUpFormData> form) {
        return new BlankPageContent();
    }

    @Override
    public void preFillFormData(final Void input, final SignUpFormData formData) {
        // Do not pre-fill anything
    }
}
