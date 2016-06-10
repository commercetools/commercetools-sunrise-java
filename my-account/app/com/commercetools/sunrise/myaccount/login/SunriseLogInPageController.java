package com.commercetools.sunrise.myaccount.login;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.SunrisePageData;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.CustomerDraft;
import io.sphere.sdk.customers.CustomerDraftBuilder;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.customers.commands.CustomerSignInCommand;
import io.sphere.sdk.customers.errors.CustomerInvalidCredentials;
import org.apache.commons.lang3.RandomStringUtils;
import play.Configuration;
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
import wedecidelatercommon.HomeReverseRouter;
import wedecidelatercommon.ProductReverseRouter;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.FormUtils.extractBooleanFormField;
import static com.commercetools.sunrise.common.utils.FormUtils.extractFormField;
import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static com.commercetools.sunrise.myaccount.CustomerSessionUtils.overwriteCustomerSessionData;
import static com.commercetools.sunrise.myaccount.CustomerSessionUtils.removeCustomerSessionData;
import static shoppingcart.CartSessionUtils.overwriteCartSessionData;
import static shoppingcart.CartSessionUtils.removeCartSessionData;

public abstract class SunriseLogInPageController extends SunriseFrameworkController {

    @Inject
    private Configuration configuration;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private FormFactory formFactory;
    @Inject
    private HomeReverseRouter homeReverseRouter;
    @Inject
    private ProductReverseRouter productReverseRouter;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final LogInPageContent pageContent = createPageContent(userContext());
        return completedFuture(ok(renderLogInPage(pageContent, userContext())));
    }

    @RequireCSRFCheck
    public CompletionStage<Result> processLogIn(final String languageTag) {
        final Form<LogInFormData> logInForm = formFactory.form(LogInFormData.class).bindFromRequest();
        if (logInForm.hasErrors()) {
            return handleLogInFormErrors(logInForm, userContext());
        } else {
            final CompletionStage<Result> resultStage = logIn(logInForm.get())
                    .thenComposeAsync(signInResult -> handleSuccessfulSignIn(signInResult, userContext()), HttpExecution.defaultContext());
            return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                    handleInvalidCredentialsError(throwable, logInForm, userContext()));
        }
    }

    @RequireCSRFCheck
    public CompletionStage<Result> processSignUp(final String languageTag) {
        final Form<SignUpFormData> signUpForm = formFactory.form(SignUpFormData.class).bindFromRequest();
        if (signUpForm.hasErrors()) {
            return handleSignUpFormErrors(signUpForm, userContext());
        } else {
            final CompletionStage<Result> resultStage = signUp(signUpForm.get())
                    .thenComposeAsync(signInResult -> handleSuccessfulSignIn(signInResult, userContext()), HttpExecution.defaultContext());
            return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                    handleExistingCustomerError(throwable, signUpForm, userContext()));
        }
    }

    public CompletionStage<Result> processLogOut(final String languageTag) {
        removeCustomerSessionData(session());
        removeCartSessionData(session());
        //TODO shouldn't we delete all session data? if not, a comment is necessary
        final Call call = homeReverseRouter.showHome(languageTag);
        return completedFuture(redirect(call));
    }

    protected CompletionStage<CustomerSignInResult> logIn(final LogInFormData formData) {
        final String anonymousCartId = CartSessionUtils.getCartId(session()).orElse(null);
        final CustomerSignInCommand signInCommand = CustomerSignInCommand.of(formData.getUsername(), formData.getPassword(), anonymousCartId);
        return sphere().execute(signInCommand);
    }

    protected CompletionStage<CustomerSignInResult> signUp(final SignUpFormData formData) {
        final String anonymousCartId = CartSessionUtils.getCartId(session()).orElse(null);
        final String customerNumber = generateCustomerNumber();
        final CustomerDraft customerDraft = CustomerDraftBuilder.of(formData.getEmail(), formData.getPassword())
                .customerNumber(customerNumber)
                .title(formData.getTitle())
                .firstName(formData.getFirstName())
                .lastName(formData.getLastName())
                .anonymousCartId(anonymousCartId)
                .build();
        final CustomerCreateCommand customerCreateCommand = CustomerCreateCommand.of(customerDraft);
        return sphere().execute(customerCreateCommand);
    }

    protected String generateCustomerNumber() {
        return RandomStringUtils.randomNumeric(6);
    }

    protected CompletionStage<Result> handleSuccessfulSignIn(final CustomerSignInResult result, final UserContext userContext) {
        overwriteCartSessionData(result.getCart(), session(), userContext, productReverseRouter);
        overwriteCustomerSessionData(result.getCustomer(), session());
        final Call call = homeReverseRouter.showHome(userContext.locale().toLanguageTag());
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
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
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
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected LogInPageContent createPageContent(final UserContext userContext) {
        final LogInPageContent pageContent = new LogInPageContent();
        pageContent.setLogInForm(new LogInFormBean(null));
        pageContent.setSignUpForm(new SignUpFormBean(userContext, i18nResolver, configuration));
        return pageContent;
    }

    protected LogInPageContent createPageContentWithLogInError(final Form<LogInFormData> logInForm,
                                                               final ErrorsBean errors, final UserContext userContext) {
        final LogInPageContent pageContent = new LogInPageContent();
        final String username = extractFormField(logInForm, "username");
        final LogInFormBean logInFormBean = new LogInFormBean(username);
        logInFormBean.setErrors(errors);
        pageContent.setLogInForm(logInFormBean);
        pageContent.setSignUpForm(new SignUpFormBean(userContext, i18nResolver, configuration));
        return pageContent;
    }

    protected LogInPageContent createPageContentWithSignUpError(final Form<SignUpFormData> signUpForm,
                                                                final ErrorsBean errors, final UserContext userContext) {
        final LogInPageContent pageContent = new LogInPageContent();
        final String title = extractFormField(signUpForm, "title");
        final String firstName = extractFormField(signUpForm, "firstName");
        final String lastName = extractFormField(signUpForm, "lastName");
        final String email = extractFormField(signUpForm, "email");
        final boolean agreeToTerms = extractBooleanFormField(signUpForm, "agreeToTerms");
        final SignUpFormBean signUpFormBean = new SignUpFormBean(title, firstName, lastName, email, agreeToTerms, userContext, i18nResolver, configuration);
        signUpFormBean.setErrors(errors);
        pageContent.setSignUpForm(signUpFormBean);
        pageContent.setLogInForm(new LogInFormBean());
        return pageContent;
    }

    protected Html renderLogInPage(final LogInPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(pageContent);
        return templateEngine().renderToHtml("my-account-login", pageData, userContext.locales());
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("login", "user"));
    }
}
