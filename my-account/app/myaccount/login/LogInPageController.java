package myaccount.login;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;
import io.sphere.sdk.customers.errors.CustomerInvalidCredentials;
import myaccount.CustomerSessionUtils;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.CartSessionUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static myaccount.CustomerSessionUtils.overwriteCustomerSessionData;
import static shoppingcart.CartSessionUtils.overwriteCartSessionData;

/**
 * Allows to log in as a customer.
 */
@Singleton
public final class LogInPageController extends SunriseController {

    private final Form<LogInFormData> logInForm;
    private final Form<SignUpFormData> signUpForm;

    @Inject
    public LogInPageController(final ControllerDependency controllerDependency, final FormFactory formFactory) {
        super(controllerDependency);
        this.logInForm = formFactory.form(LogInFormData.class);
        this.signUpForm = formFactory.form(SignUpFormData.class);
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final LogInPageContent pageContent = new LogInPageContent();
        return completedFuture(ok(renderLogInPage(pageContent, userContext)));
    }

    @AddCSRFToken
    @RequireCSRFCheck
    public CompletionStage<Result> processLogIn(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<LogInFormData> form = logInForm.bindFromRequest();
        final LogInPageContent pageContent = new LogInPageContent(form);
        if (form.hasErrors()) {
            return completedFuture(handleLogInFormErrors(form, pageContent, userContext));
        } else {
            return logIn(form.get())
                    .thenApplyAsync(signInResult -> handleSuccessfulSignIn(signInResult, userContext), HttpExecution.defaultContext())
                    .exceptionally(throwable -> handleInvalidCredentialsError(throwable, pageContent, userContext));
        }
    }

    @AddCSRFToken
    @RequireCSRFCheck
    public CompletionStage<Result> processSignUp(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<SignUpFormData> form = signUpForm.bindFromRequest();
        final LogInPageContent pageContent = new LogInPageContent(form, userContext, i18nResolver(), configuration());
        if (form.hasErrors()) {
            return completedFuture(handleSignUpFormErrors(form, pageContent, userContext));
        } else {
            return signUp(form.get())
                    .thenApplyAsync(signInResult -> handleSuccessfulSignIn(signInResult, userContext), HttpExecution.defaultContext())
                    .exceptionally(throwable -> handleExistingCustomerError(throwable, pageContent, userContext));
        }
    }

    public Result processLogOut(final String languageTag) {
        CustomerSessionUtils.removeCustomer(session());
        CartSessionUtils.removeCart(session());
        return redirect(reverseRouter().showHome(languageTag));
    }

    private CompletionStage<CustomerSignInResult> logIn(final LogInFormData formData) {
        final String anonymousCartId = CartSessionUtils.getCartId(session()).orElse(null);
        final CustomerSignInCommand signInCommand = CustomerSignInCommand.of(formData.getUsername(), formData.getPassword(), anonymousCartId);
        return sphere().execute(signInCommand);
    }

    private CompletionStage<CustomerSignInResult> signUp(final SignUpFormData formData) {
        final String anonymousCartId = CartSessionUtils.getCartId(session()).orElse(null);
        final CustomerDraft customerDraft = CustomerDraftBuilder.of(formData.getEmail(), formData.getPassword())
                .title(formData.getTitle())
                .firstName(formData.getFirstName())
                .lastName(formData.getLastName())
                .anonymousCartId(anonymousCartId)
                .build();
        final CustomerCreateCommand customerCreateCommand = CustomerCreateCommand.of(customerDraft);
        return sphere().execute(customerCreateCommand);
    }

    private Result handleSuccessfulSignIn(final CustomerSignInResult result, final UserContext userContext) {
        overwriteCartSessionData(result.getCart(), session(), userContext, reverseRouter());
        overwriteCustomerSessionData(result.getCustomer(), session());
        return redirect(reverseRouter().showHome(userContext.locale().toLanguageTag()));
    }

    private Result handleLogInFormErrors(final Form<LogInFormData> form, final LogInPageContent pageContent,
                                         final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(form);
        pageContent.getLogInForm().setErrors(errors);
        return badRequest(renderLogInPage(pageContent, userContext));
    }

    private Result handleSignUpFormErrors(final Form<SignUpFormData> form, final LogInPageContent pageContent,
                                          final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(form);
        pageContent.getSignUpForm().setErrors(errors);
        return badRequest(renderLogInPage(pageContent, userContext));
    }

    private Result handleInvalidCredentialsError(final Throwable throwable, final LogInPageContent pageContent,
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
            pageContent.getLogInForm().setErrors(errors);
            return badRequest(renderLogInPage(pageContent, userContext));
        }
        throw new RuntimeException(throwable);
    }

    private Result handleExistingCustomerError(final Throwable throwable, final LogInPageContent pageContent,
                                               final UserContext userContext) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("Unknown error, probably customer already exists", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("A user with this email already exists"); // TODO get from i18n
            pageContent.getSignUpForm().setErrors(errors);
            return badRequest(renderLogInPage(pageContent, userContext));
        }
        throw new RuntimeException(throwable);
    }

    private Html renderLogInPage(final LogInPageContent pageContent, final UserContext userContext) {
        if (pageContent.getSignUpForm() == null) {
            final SignUpFormBean signUpFormBean = new SignUpFormBean(null, userContext, i18nResolver(), configuration());
            pageContent.setSignUpForm(signUpFormBean);
        }
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx());
        return templateService().renderToHtml("my-account-login", pageData, userContext.locales());
    }
}
