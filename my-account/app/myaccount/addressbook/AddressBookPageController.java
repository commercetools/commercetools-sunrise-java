package myaccount.addressbook;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.SphereException;
import myaccount.common.MyAccountController;
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
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static myaccount.CustomerSessionUtils.overwriteCustomerSessionData;

@Singleton
public class AddressBookPageController extends MyAccountController {

    protected final ProductDataConfig productDataConfig;
    protected final Form<AddAddressFormData> addAddressUnboundForm;
    protected final Form<ChangeAddressFormData> changeAddressUnboundForm;
    protected final Form<RemoveAddressFormData> removeAddressUnboundForm;

    @Inject
    public AddressBookPageController(final ControllerDependency controllerDependency,
                                     final ProductDataConfig productDataConfig, final FormFactory formFactory) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
        this.addAddressUnboundForm = formFactory.form(AddAddressFormData.class);
        this.changeAddressUnboundForm = formFactory.form(ChangeAddressFormData.class);
        this.removeAddressUnboundForm = formFactory.form(RemoveAddressFormData.class);
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getCustomer(session())
                .thenComposeAsync(customerOpt -> customerOpt
                        .map(customer -> handleFoundCustomer(customer, userContext))
                        .orElseGet(() -> handleNotFoundCustomer(userContext)),
                        HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> processAddAddress(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<AddAddressFormData> addAddressForm = addAddressUnboundForm.bindFromRequest();
        return getCustomer(session())
                .thenComposeAsync(customerOpt -> customerOpt
                        .map(customer -> {
                            if (addAddressForm.hasErrors()) {
                                return handleAddAddressFormErrors(addAddressForm, customer, userContext);
                            } else {
                                final Address address = addAddressForm.get().toAddress();
                                final CompletionStage<Result> resultStage = addAddress(customer, address)
                                        .thenComposeAsync(updatedCustomer -> handleSuccessfulCustomerUpdate(updatedCustomer, userContext), HttpExecution.defaultContext());
                                return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                        handleAddAddressError(throwable, addAddressForm, customer, userContext));
                            }
                        })
                        .orElseGet(() -> handleNotFoundCustomer(userContext)),
                        HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> processChangeAddress(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<ChangeAddressFormData> changeAddressForm = changeAddressUnboundForm.bindFromRequest();
        return getCustomer(session())
                .thenComposeAsync(customerOpt -> customerOpt
                                .map(customer -> {
                                    if (changeAddressForm.hasErrors()) {
                                        return handleChangeAddressFormErrors(changeAddressForm, customer, userContext);
                                    } else {
                                        final String addressId = changeAddressForm.get().getAddressId();
                                        final Address address = changeAddressForm.get().toAddress();
                                        final CompletionStage<Result> resultStage = changeAddress(customer, addressId, address)
                                                .thenComposeAsync(updatedCustomer -> handleSuccessfulCustomerUpdate(updatedCustomer, userContext), HttpExecution.defaultContext());
                                        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                                handleChangeAddressError(throwable, changeAddressForm, customer, userContext));
                                    }
                                })
                                .orElseGet(() -> handleNotFoundCustomer(userContext)),
                        HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> processRemoveAddress(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<RemoveAddressFormData> removeAddressForm = removeAddressUnboundForm.bindFromRequest();
        return getCustomer(session())
                .thenComposeAsync(customerOpt -> customerOpt
                                .map(customer -> {
                                    if (removeAddressForm.hasErrors()) {
                                        return handleRemoveAddressFormErrors(removeAddressForm, customer, userContext);
                                    } else {
                                        final String addressId = removeAddressForm.get().getAddressId();
                                        final CompletionStage<Result> resultStage = removeAddress(customer, addressId)
                                                .thenComposeAsync(updatedCustomer -> handleSuccessfulCustomerUpdate(updatedCustomer, userContext), HttpExecution.defaultContext());
                                        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                                handleRemoveAddressError(throwable, removeAddressForm, customer, userContext));
                                    }
                                })
                                .orElseGet(() -> handleNotFoundCustomer(userContext)),
                        HttpExecution.defaultContext());
    }

    protected CompletionStage<Customer> addAddress(final Customer customer, final Address address) {
        final AddAddress updateAction = AddAddress.of(address);
        return sphere().execute(CustomerUpdateCommand.of(customer, updateAction));
    }

    protected CompletionStage<Customer> changeAddress(final Customer customer, final String addressId, final Address address) {
        final ChangeAddress updateAction = ChangeAddress.of(addressId, address);
        return sphere().execute(CustomerUpdateCommand.of(customer, updateAction));
    }

    protected CompletionStage<Customer> removeAddress(final Customer customer, final String addressId) {
        final RemoveAddress updateAction = RemoveAddress.of(addressId);
        return sphere().execute(CustomerUpdateCommand.of(customer, updateAction));
    }

    protected CompletionStage<Result> handleFoundCustomer(final Customer customer, final UserContext userContext) {
        final AddressBookPageContent pageContent = createAddressBookPage(customer, userContext);
        return completedFuture(ok(renderAddressBookPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleSuccessfulCustomerUpdate(final Customer customer, final UserContext userContext) {
        overwriteCustomerSessionData(customer, session());
        final AddressBookPageContent pageContent = createAddressBookPage(customer, userContext);
        return completedFuture(ok(renderAddressBookPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleAddAddressFormErrors(final Form<AddAddressFormData> addAddressForm,
                                                                 final Customer customer, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(addAddressForm);
        final AddressBookPageContent pageContent = createAddressBookPageWithAddAddressError(addAddressForm, errors, customer, userContext);
        return completedFuture(badRequest(renderAddressBookPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleChangeAddressFormErrors(final Form<ChangeAddressFormData> changeAddressForm,
                                                                    final Customer customer, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(changeAddressForm);
        final AddressBookPageContent pageContent = createAddressBookPageWithChangeAddressError(changeAddressForm, errors, customer, userContext);
        return completedFuture(badRequest(renderAddressBookPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleRemoveAddressFormErrors(final Form<RemoveAddressFormData> removeAddressForm,
                                                                    final Customer customer, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(removeAddressForm);
        final AddressBookPageContent pageContent = createAddressBookPageWithRemoveAddressError(removeAddressForm, errors, customer, userContext);
        return completedFuture(badRequest(renderAddressBookPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleAddAddressError(final Throwable throwable,
                                                            final Form<AddAddressFormData> addAddressForm,
                                                            final Customer customer, final UserContext userContext) {
        if (throwable.getCause() instanceof SphereException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to add address to a customer raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final AddressBookPageContent pageContent = createAddressBookPageWithAddAddressError(addAddressForm, errors, customer, userContext);
            return completedFuture(badRequest(renderAddressBookPage(pageContent, userContext)));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Result> handleChangeAddressError(final Throwable throwable,
                                                               final Form<ChangeAddressFormData> changeAddressForm,
                                                               final Customer customer, final UserContext userContext) {
        if (throwable.getCause() instanceof SphereException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to change address from a customer raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final AddressBookPageContent pageContent = createAddressBookPageWithChangeAddressError(changeAddressForm, errors, customer, userContext);
            return completedFuture(badRequest(renderAddressBookPage(pageContent, userContext)));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Result> handleRemoveAddressError(final Throwable throwable,
                                                               final Form<RemoveAddressFormData> removeAddressForm,
                                                               final Customer customer, final UserContext userContext) {
        if (throwable.getCause() instanceof SphereException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to remove address from a customer raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final AddressBookPageContent pageContent = createAddressBookPageWithRemoveAddressError(removeAddressForm, errors, customer, userContext);
            return completedFuture(badRequest(renderAddressBookPage(pageContent, userContext)));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Result> handleNotFoundCustomer(final UserContext userContext) {
        final Call call = reverseRouter().showLogInForm(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected AddressBookPageContent createAddressBookPage(final Customer customer, final UserContext userContext) {
        final AddressBookPageContent pageContent = new AddressBookPageContent();
        //TODO
        return pageContent;
    }

    protected AddressBookPageContent createAddressBookPageWithAddAddressError(final Form<AddAddressFormData> form, final ErrorsBean errors,
                                                                              final Customer customer, final UserContext userContext) {
        final AddressBookPageContent pageContent = new AddressBookPageContent();
        //TODO
        return pageContent;
    }

    protected AddressBookPageContent createAddressBookPageWithChangeAddressError(final Form<ChangeAddressFormData> form, final ErrorsBean errors,
                                                                                 final Customer customer, final UserContext userContext) {
        final AddressBookPageContent pageContent = new AddressBookPageContent();
        //TODO
        return pageContent;
    }

    protected AddressBookPageContent createAddressBookPageWithRemoveAddressError(final Form<RemoveAddressFormData> form, final ErrorsBean errors,
                                                                                 final Customer customer, final UserContext userContext) {
        final AddressBookPageContent pageContent = new AddressBookPageContent();
        //TODO
        return pageContent;
    }

    protected Html renderAddressBookPage(final AddressBookPageContent pageContent, final UserContext userContext) {
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateEngine().renderToHtml("my-account-address-book", pageData, userContext.locales());
    }
}
