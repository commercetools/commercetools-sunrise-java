package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.Feedback;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.myaccount.common.MyAccountController;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.SphereException;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.FormUtils.extractErrors;
import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public abstract class SunriseAddAddressController extends MyAccountController implements WithOverwriteableTemplateName {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseAddAddressController.class);

    @Inject
    private FormFactory formFactory;
    @Inject
    private AddressBookReverseRouter addressBookReverseRouter;

    @Nullable
    private Form<AddAddressFormData> form;

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("address-book", "add-address", "address"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-new-address";
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            form = formFactory.form(AddAddressFormData.class);
            return getCustomerAndExecute(this::showAddAddressForm);
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            form = formFactory.form(AddAddressFormData.class).bindFromRequest();
            return getCustomerAndExecute(this::processAddAddressForm);
        });
    }

    protected CompletionStage<Result> showAddAddressForm(final Customer customer) {
        final PageContent pageContent = createPageContent(customer, new Feedback());
        return asyncOk(renderPage(pageContent, getTemplateName()));
    }

    protected CompletionStage<Result> processAddAddressForm(final Customer customer) {
        return formHasNoErrors() ? handleValidForm(customer) : handleInvalidForm(customer);
    }

    protected boolean formHasNoErrors() {
        return form != null && !form.hasErrors();
    }

    protected CompletionStage<Result> handleInvalidForm(final Customer customer) {
        final Feedback feedback = new Feedback();
        feedback.setErrors(extractErrors(form));
        final PageContent pageContent = createPageContent(customer, feedback);
        return renderPage(pageContent, getTemplateName())
                .thenApplyAsync(html -> ok(html), HttpExecution.defaultContext());
    }

    protected CompletionStage<Result> handleValidForm(final Customer customer) {
        final Address address = form.get().toAddress();
        final CompletionStage<Result> resultStage = addAddressToCustomer(customer, address)
                .thenComposeAsync(this::handleSuccessfulCustomerUpdate, HttpExecution.defaultContext());
        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                handleFailedCustomerUpdate(customer, throwable));
    }

    protected CompletionStage<Result> handleSuccessfulCustomerUpdate(final Customer updatedCustomer) {
        return redirectToAddressBook();
    }

    protected CompletionStage<Result> handleFailedCustomerUpdate(final Customer customer, final Throwable throwable) {
        if (throwable.getCause() instanceof SphereException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            logger.error("The request to add address to a customer raised an exception", errorResponseException);
            final Feedback feedback = new Feedback(); // TODO See how to deal with feedback in general
            feedback.setErrors(singletonList("Something went wrong, please try again")); // TODO get from i18n
            final PageContent pageContent = createPageContent(customer, feedback);
            return asyncBadRequest(renderPage(pageContent, getTemplateName()));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }


    protected CompletionStage<Customer> addAddressToCustomer(final Customer customer, final Address address) {
        final AddAddress updateAction = AddAddress.of(address);
        return sphere().execute(CustomerUpdateCommand.of(customer, updateAction));
    }

    protected PageContent createPageContent(final Customer customer, final Feedback feedback) {
        throw new NotImplementedException("Add address controller page");
    }

    protected final Optional<Form<AddAddressFormData>> getForm() {
        return Optional.ofNullable(form);
    }

    private CompletableFuture<Result> redirectToAddressBook() {
        final Call call = addressBookReverseRouter.showMyAddressBook(userContext().languageTag());
        return completedFuture(redirect(call));
    }
}
