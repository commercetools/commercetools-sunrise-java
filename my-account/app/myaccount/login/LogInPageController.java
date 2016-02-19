package myaccount.login;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;
import io.sphere.sdk.customers.errors.CustomerInvalidCredentials;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.F;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.CartSessionUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Allows to log in as a customer.
 */
@Singleton
public final class LogInPageController extends SunriseController {

    @Inject
    public LogInPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final SunrisePageData pageData = pageData(userContext, new LogInPageContent(), ctx());
        return F.Promise.pure(ok(renderLogInPage(userContext, pageData)));
    }

    @AddCSRFToken
    @RequireCSRFCheck
    public F.Promise<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<LogInFormData> filledForm = obtainFilledForm(LogInFormData.class);
        final LogInFormData formData = extractFormData(LogInFormData.class);
        final LogInPageContent pageContent = new LogInPageContent(formData);
        if (filledForm.hasErrors()) {
            final ErrorsBean errors = new ErrorsBean(filledForm);
            return F.Promise.pure(badRequest(errors, pageContent, userContext));
        } else {
            final CustomerSignInCommand signInCommand = CustomerSignInCommand.of(formData.getUsername(), formData.getPassword());
            return sphere().execute(signInCommand).map(result -> {
                CartSessionUtils.overwriteCartSessionData(result.getCart(), session(), userContext, reverseRouter());
                return redirect(reverseRouter().showCart(languageTag));
            }).recover(throwable -> handleInvalidCredentials(throwable, pageContent, userContext));
        }
    }

    private Result handleInvalidCredentials(final Throwable throwable, final LogInPageContent pageContent,
                                            final UserContext userContext) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            final ErrorsBean errors;
            if (errorResponseException.hasErrorCode(CustomerInvalidCredentials.CODE)) {
                Logger.debug("Invalid credentials");
                errors = new ErrorsBean("Invalid credentials"); // TODO get from i18n
            } else {
                Logger.error("Unknown error", errorResponseException);
                errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            }
            return badRequest(errors, pageContent, userContext);
        }
        throw new RuntimeException(throwable);
    }

    private Result badRequest(final ErrorsBean errors, final LogInPageContent pageContent, final UserContext userContext) {
        pageContent.getLogInForm().setErrors(errors);
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx());
        return badRequest(renderLogInPage(userContext, pageData));
    }

    private Html renderLogInPage(final UserContext userContext, final SunrisePageData pageData) {
        return templateService().renderToHtml("my-account-login", pageData, userContext.locales());
    }

    private <T> T extractFormData(final Class<T> clazz) {
        return DynamicForm.form(clazz, null).bindFromRequest(request()).get();
    }

    private <T> Form<T> obtainFilledForm(final Class<T> clazz) {
        return Form.form(clazz).bindFromRequest(request());
    }

}
