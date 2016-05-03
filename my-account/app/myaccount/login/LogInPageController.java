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
import play.mvc.Call;
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
public class LogInPageController extends SunriseController {

    protected final Form<LogInFormData> logInUnboundForm;
    protected final Form<SignUpFormData> signUpUnboundForm;

    @Inject
    public LogInPageController(final ControllerDependency controllerDependency, final FormFactory formFactory) {
        super(controllerDependency);
        this.logInUnboundForm = formFactory.form(LogInFormData.class);
        this.signUpUnboundForm = formFactory.form(SignUpFormData.class);
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final LogInPageContent pageContent = createPageContent(userContext);
        return completedFuture(ok(renderLogInPage(pageContent, userContext)));
    }

    @RequireCSRFCheck
    public CompletionStage<Result> processLogIn(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<LogInFormData> logInForm = logInUnboundForm.bindFromRequest();
        if (logInForm.hasErrors()) {
            return handleLogInFormErrors(logInForm, userContext);
        } else {
            return logIn(logInForm.get())
                    .thenComposeAsync(signInResult -> handleSuccessfulSignIn(signInResult, userContext), HttpExecution.defaultContext())
                    .exceptionally(throwable -> handleInvalidCredentialsError(throwable, logInForm, userContext).toCompletableFuture().join()); // TODO move to async
        }
    }

    @RequireCSRFCheck
    public CompletionStage<Result> processSignUp(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<SignUpFormData> signUpForm = signUpUnboundForm.bindFromRequest();
        if (signUpForm.hasErrors()) {
            return handleSignUpFormErrors(signUpForm, userContext);
        } else {
            return signUp(signUpForm.get())
                    .thenComposeAsync(signInResult -> handleSuccessfulSignIn(signInResult, userContext), HttpExecution.defaultContext())
                    .exceptionally(throwable -> handleExistingCustomerError(throwable, signUpForm, userContext).toCompletableFuture().join());  // TODO move to async
        }
    }

    public CompletionStage<Result> processLogOut(final String languageTag) {
        CustomerSessionUtils.removeCustomer(session());
        CartSessionUtils.removeCart(session());
        final Call call = reverseRouter().showHome(languageTag);
        return completedFuture(redirect(call));
    }

    protected CompletionStage<CustomerSignInResult> logIn(final LogInFormData formData) {
        final String anonymousCartId = CartSessionUtils.getCartId(session()).orElse(null);
        final CustomerSignInCommand signInCommand = CustomerSignInCommand.of(formData.getUsername(), formData.getPassword(), anonymousCartId);
        return sphere().execute(signInCommand);
    }

    protected CompletionStage<CustomerSignInResult> signUp(final SignUpFormData formData) {
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

    protected CompletionStage<Result> handleSuccessfulSignIn(final CustomerSignInResult result, final UserContext userContext) {
        overwriteCartSessionData(result.getCart(), session(), userContext, reverseRouter());
        overwriteCustomerSessionData(result.getCustomer(), session());
        final Call call = reverseRouter().showHome(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleLogInFormErrors(final Form<LogInFormData> logInForm, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(logInForm);
        final LogInPageContent pageContent = createPageContentWithLogInError(logInForm, errors, userContext);
        return completedFuture(badRequest(renderLogInPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleSignUpFormErrors(final Form<SignUpFormData> signUpForm, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(signUpForm);
        final LogInPageContent pageContent = createPageContentWithSignUpError(signUpForm, errors, userContext);
        return completedFuture(badRequest(renderLogInPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleInvalidCredentialsError(final Throwable throwable, final Form<LogInFormData> logInForm,
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
            final LogInPageContent pageContent = createPageContentWithLogInError(logInForm, errors, userContext);
            return completedFuture(badRequest(renderLogInPage(pageContent, userContext)));
        }
        throw new RuntimeException(throwable);
    }

    protected CompletionStage<Result> handleExistingCustomerError(final Throwable throwable, final Form<SignUpFormData> signUpForm,
                                                 final UserContext userContext) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("Unknown error, probably customer already exists", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("A user with this email already exists"); // TODO get from i18n
            final LogInPageContent pageContent = createPageContentWithSignUpError(signUpForm, errors, userContext);
            return completedFuture(badRequest(renderLogInPage(pageContent, userContext)));
        }
        throw new RuntimeException(throwable);
    }

    protected LogInPageContent createPageContent(final UserContext userContext) {
        final LogInPageContent pageContent = new LogInPageContent();
        pageContent.setLogInForm(new LogInFormBean(null));
        pageContent.setSignUpForm(new SignUpFormBean(userContext, i18nResolver(), configuration()));
        return pageContent;
    }

    protected LogInPageContent createPageContentWithLogInError(final Form<LogInFormData> logInForm,
                                                               final ErrorsBean errors, final UserContext userContext) {
        final LogInPageContent pageContent = new LogInPageContent();
        final String username = logInForm.field("username").value();
        final LogInFormBean logInFormBean = new LogInFormBean(username);
        logInFormBean.setErrors(errors);
        pageContent.setLogInForm(logInFormBean);
        pageContent.setSignUpForm(new SignUpFormBean(userContext, i18nResolver(), configuration()));
        return pageContent;
    }

    protected LogInPageContent createPageContentWithSignUpError(final Form<SignUpFormData> signUpForm,
                                                                final ErrorsBean errors, final UserContext userContext) {
        final LogInPageContent pageContent = new LogInPageContent();
        final String title = signUpForm.field("title").value();
        final String firstName = signUpForm.field("firstName").value();
        final String lastName = signUpForm.field("lastName").value();
        final String email = signUpForm.field("email").value();
        final boolean agreeToTerms = Boolean.valueOf(signUpForm.field("agreeToTerms").value());
        final SignUpFormBean signUpFormBean = new SignUpFormBean(title, firstName, lastName, email, agreeToTerms, userContext, i18nResolver(), configuration());
        signUpFormBean.setErrors(errors);
        pageContent.setSignUpForm(signUpFormBean);
        pageContent.setLogInForm(new LogInFormBean());
        return pageContent;
    }

    protected Html renderLogInPage(final LogInPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("my-account-login", pageData, userContext.locales());
    }
}
