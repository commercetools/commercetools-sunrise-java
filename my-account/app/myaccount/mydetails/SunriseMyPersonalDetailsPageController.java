package myaccount.mydetails;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import common.models.TitleFormFieldBean;
import common.template.i18n.I18nResolver;
import common.utils.FormUtils;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.ChangeEmail;
import io.sphere.sdk.customers.commands.updateactions.SetFirstName;
import io.sphere.sdk.customers.commands.updateactions.SetLastName;
import io.sphere.sdk.customers.commands.updateactions.SetTitle;
import io.sphere.sdk.models.SphereException;
import myaccount.common.MyAccountController;
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

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static myaccount.CustomerSessionUtils.overwriteCustomerSessionData;

public abstract class SunriseMyPersonalDetailsPageController extends MyAccountController {

    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    protected FormFactory formFactory;
    @Inject
    protected I18nResolver i18nResolver;
    @Inject
    protected Configuration configuration;
    @Inject
    protected ReverseRouter reverseRouter;//TODO framework use smaller router

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return getCustomer(session())
                .thenComposeAsync(customerOpt -> customerOpt
                        .map(customer -> handleFoundCustomer(customer, userContext()))
                        .orElseGet(() -> handleNotFoundCustomer(userContext())),
                        HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        final Form<MyPersonalDetailsFormData> myPersonalDetailsForm = formFactory.form(MyPersonalDetailsFormData.class).bindFromRequest();
        return getCustomer(session())
                .thenComposeAsync(customerOpt -> customerOpt
                        .map(customer -> {
                            if (myPersonalDetailsForm.hasErrors()) {
                                return handleFormErrors(myPersonalDetailsForm, customer, userContext());
                            } else {
                                final CompletionStage<Result> resultStage = updateCustomer(customer, myPersonalDetailsForm.get())
                                        .thenComposeAsync(updatedCustomer -> handleSuccessfulCustomerUpdate(updatedCustomer, userContext()), HttpExecution.defaultContext());
                                return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                        handleSetShippingToCartError(throwable, myPersonalDetailsForm, customer, userContext()));
                            }
                        })
                        .orElseGet(() -> handleNotFoundCustomer(userContext())),
                        HttpExecution.defaultContext());
    }

    protected CompletionStage<Customer> updateCustomer(final Customer customer, final MyPersonalDetailsFormData newCustomerData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        if (!Objects.equals(customer.getTitle(), newCustomerData.getTitle())) {
            updateActions.add(SetTitle.of(newCustomerData.getTitle()));
        }
        if (!Objects.equals(customer.getFirstName(), newCustomerData.getFirstName())) {
            updateActions.add(SetFirstName.of(newCustomerData.getFirstName()));
        }
        if (!Objects.equals(customer.getLastName(), newCustomerData.getLastName())) {
            updateActions.add(SetLastName.of(newCustomerData.getLastName()));
        }
        if (!Objects.equals(customer.getEmail(), newCustomerData.getEmail())) {
            updateActions.add(ChangeEmail.of(newCustomerData.getEmail()));
        }
        if (!updateActions.isEmpty()) {
            return sphere().execute(CustomerUpdateCommand.of(customer, updateActions));
        } else {
            return completedFuture(customer);
        }
    }

    protected CompletionStage<Result> handleFoundCustomer(final Customer customer, final UserContext userContext) {
        final MyPersonalDetailsPageContent pageContent = createMyPersonalDetailsPage(customer, userContext);
        return completedFuture(ok(renderMyPersonalDetailsPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleSuccessfulCustomerUpdate(final Customer customer, final UserContext userContext) {
        overwriteCustomerSessionData(customer, session());
        final MyPersonalDetailsPageContent pageContent = createMyPersonalDetailsPage(customer, userContext);
        return completedFuture(ok(renderMyPersonalDetailsPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<MyPersonalDetailsFormData> myPersonalDetailsForm,
                                                       final Customer customer, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(myPersonalDetailsForm);
        final MyPersonalDetailsPageContent pageContent = createMyPersonalDetailsPageWithError(myPersonalDetailsForm, errors, customer, userContext);
        return completedFuture(badRequest(renderMyPersonalDetailsPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleSetShippingToCartError(final Throwable throwable,
                                                                   final Form<MyPersonalDetailsFormData> myPersonalDetailsForm,
                                                                   final Customer customer, final UserContext userContext) {
        if (throwable.getCause() instanceof SphereException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to update customer details raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final MyPersonalDetailsPageContent pageContent = createMyPersonalDetailsPageWithError(myPersonalDetailsForm, errors, customer, userContext);
            return completedFuture(badRequest(renderMyPersonalDetailsPage(pageContent, userContext)));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Result> handleNotFoundCustomer(final UserContext userContext) {
        final Call call = reverseRouter.showLogInForm(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected MyPersonalDetailsPageContent createMyPersonalDetailsPage(final Customer customer, final UserContext userContext) {
        final MyPersonalDetailsPageContent pageContent = new MyPersonalDetailsPageContent();
        pageContent.setCustomer(new CustomerBean(customer));
        pageContent.setPersonalDetailsForm(new MyPersonalDetailsFormBean(customer, userContext, i18nResolver, configuration));
        return pageContent;
    }

    protected MyPersonalDetailsPageContent createMyPersonalDetailsPageWithError(final Form<MyPersonalDetailsFormData> myPersonalDetailsForm,
                                                                                final ErrorsBean errors, final Customer customer,
                                                                                final UserContext userContext) {
        final MyPersonalDetailsPageContent pageContent = new MyPersonalDetailsPageContent();
        pageContent.setCustomer(new CustomerBean(customer));
        final MyPersonalDetailsFormBean formBean = new MyPersonalDetailsFormBean();
        final Function<String, String> formFieldExtractor = FormUtils.formFieldExtractor(myPersonalDetailsForm);
        formBean.setSalutations(new TitleFormFieldBean(formFieldExtractor.apply("title"), userContext, i18nResolver, configuration));
        formBean.setFirstName(formFieldExtractor.apply("firstName"));
        formBean.setLastName(formFieldExtractor.apply("lastName"));
        formBean.setEmail(formFieldExtractor.apply("email"));
        formBean.setErrors(errors);
        pageContent.setPersonalDetailsForm(formBean);
        return pageContent;
    }

    protected Html renderMyPersonalDetailsPage(final MyPersonalDetailsPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(pageContent);
        return templateEngine().renderToHtml("my-account-personal-details", pageData, userContext.locales());
    }
}
