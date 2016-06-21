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
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
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
public abstract class SunriseChangeAddressController extends MyAccountController implements WithOverwriteableTemplateName {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseChangeAddressController.class);

    @Inject
    private FormFactory formFactory;
    @Inject
    private AddressBookReverseRouter addressBookReverseRouter;

    @Nullable
    private Form<ChangeAddressFormData> form;

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("address-book", "change-address", "address"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-edit-address";
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag, final String addressId) {
        return doRequest(() -> {
            form = formFactory.form(ChangeAddressFormData.class);
            return getCustomerAndExecute(this::showChangeAddressForm);
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return doRequest(() -> {
            form = formFactory.form(ChangeAddressFormData.class).bindFromRequest();
            return getCustomerAndExecute(this::processChangeAddressForm);
        });
    }

    protected CompletionStage<Result> showChangeAddressForm(final Customer customer) {
        final PageContent pageContent = createPageContent(customer, new Feedback());
        return asyncOk(renderPage(pageContent, getTemplateName()));
    }

    protected CompletionStage<Result> processChangeAddressForm(final Customer customer) {
        return formHasNoErrors() ? handleValidForm(customer) : handleInvalidForm(customer);
    }

    protected boolean formHasNoErrors() {
        return form != null && !form.hasErrors();
    }

    protected CompletionStage<Result> handleInvalidForm(final Customer customer) {
        final Feedback feedback = new Feedback();
        feedback.setErrors(extractErrors(form));
        final PageContent pageContent = createPageContent(customer, feedback);
        return asyncOk(renderPage(pageContent, getTemplateName()));
    }

    protected CompletionStage<Result> handleValidForm(final Customer customer) {
        final String addressId = form.get().getAddressId();
        final Address address = form.get().toAddress();
        final CompletionStage<Result> resultStage = changeAddressFromCustomer(customer, addressId, address)
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
            logger.error("The request to change address from a customer raised an exception", errorResponseException);
            final Feedback feedback = new Feedback(); // TODO See how to deal with feedback in general
            feedback.setErrors(singletonList("Something went wrong, please try again")); // TODO get from i18n
            final PageContent pageContent = createPageContent(customer, feedback);
            return asyncBadRequest(renderPage(pageContent, getTemplateName()));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CompletionStage<Customer> changeAddressFromCustomer(final Customer customer, final String addressId, final Address newAddress) {
        final ChangeAddress updateAction = ChangeAddress.of(addressId, newAddress);
        return sphere().execute(CustomerUpdateCommand.of(customer, updateAction));
    }

    protected PageContent createPageContent(final Customer customer, final Feedback feedback) {
        throw new NotImplementedException("Change address controller page");
    }

    protected final Optional<Form<ChangeAddressFormData>> getForm() {
        return Optional.ofNullable(form);
    }

    private CompletableFuture<Result> redirectToAddressBook() {
        final Call call = addressBookReverseRouter.showMyAddressBook(userContext().languageTag());
        return completedFuture(redirect(call));
    }
}