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
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;
import io.sphere.sdk.models.SphereException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
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
public abstract class SunriseRemoveAddressController extends MyAccountController implements WithOverwriteableTemplateName {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseRemoveAddressController.class);

    @Inject
    private FormFactory formFactory;
    @Inject
    private AddressBookReverseRouter addressBookReverseRouter;
    @Inject
    private AddressBookPageContentFactory addressBookPageContentFactory;

    @Nullable
    private Form<RemoveAddressFormData> form;

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("address-book", "remove-address", "address"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-address-book";
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            form = formFactory.form(RemoveAddressFormData.class).bindFromRequest();
            return getCustomerAndExecute(this::processRemoveAddressForm);
        });
    }

    protected CompletionStage<Result> processRemoveAddressForm(final Customer customer) {
        return formHasNoErrors() ? handleValidForm(customer) : handleInvalidForm(customer);
    }

    protected boolean formHasNoErrors() {
        return form != null && !form.hasErrors();
    }

    protected CompletionStage<Result> handleInvalidForm(final Customer customer) {
        final Feedback feedback = new Feedback();
        feedback.setErrors(extractErrors(form));
        final PageContent pageContent = createPageContent(customer, feedback);
        return completedFuture(ok(renderPage(pageContent, getTemplateName())));
    }

    protected CompletionStage<Result> handleValidForm(final Customer customer) {
        final String addressId = form.get().getAddressId(); // TODO do something when either form is null or is not initialized
        final CompletionStage<Result> resultStage = removeAddressFromCustomer(customer, addressId)
                .thenComposeAsync(this::handleSuccessfulCustomerUpdate, HttpExecution.defaultContext());
        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                handleFailedCustomerUpdate(customer, throwable));
    }

    protected CompletionStage<Result> handleSuccessfulCustomerUpdate(final Customer customer) {
        return redirectToAddressBook();
    }

    protected CompletionStage<Result> handleFailedCustomerUpdate(final Customer customer, final Throwable throwable) {
        if (throwable.getCause() instanceof SphereException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            logger.error("The request to remove address from a customer raised an exception", errorResponseException);
            final Feedback feedback = new Feedback(); // TODO See how to deal with feedback in general
            feedback.setErrors(singletonList("Something went wrong, please try again")); // TODO get from i18n
            final PageContent pageContent = createPageContent(customer, feedback);
            return completedFuture(badRequest(renderPage(pageContent, getTemplateName())));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Customer> removeAddressFromCustomer(final Customer customer, final String addressId) {
        final RemoveAddress updateAction = RemoveAddress.of(addressId);
        return sphere().execute(CustomerUpdateCommand.of(customer, updateAction));
    }

    protected PageContent createPageContent(final Customer customer, final Feedback feedback) {
        return addressBookPageContentFactory.create(customer);
    }

    protected final Optional<Form<RemoveAddressFormData>> getForm() {
        return Optional.ofNullable(form);
    }

    private CompletableFuture<Result> redirectToAddressBook() {
        final Call call = addressBookReverseRouter.showMyAddressBook(userContext().languageTag());
        return completedFuture(redirect(call));
    }
}
