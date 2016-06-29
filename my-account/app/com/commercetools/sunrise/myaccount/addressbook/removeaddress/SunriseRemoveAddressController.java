package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookManagementController;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookPageContent;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookPageContentFactory;
import com.google.inject.Injector;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.SphereException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;

@RequestScoped
public abstract class SunriseRemoveAddressController extends AddressBookManagementController implements WithOverwriteableTemplateName {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseRemoveAddressController.class);

    @Inject
    private Injector injector;
    @Inject
    private FormFactory formFactory;
    @Inject
    private AddressBookPageContentFactory addressBookPageContentFactory;

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
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return doRequest(() -> {
            logger.debug("try to remove address with id={} in locale={}", addressId, languageTag);
            final Form<RemoveAddressFormData> form = formFactory.form(RemoveAddressFormData.class).bindFromRequest();
            return injector.getInstance(RemoveAddressActionDataDefaultProvider.class).getActionData(session(), addressId, form)
                    .thenComposeAsync(this::processRemoveAddressForm);
        });
    }

    protected <T> CompletionStage<Result> processRemoveAddressForm(final RemoveAddressActionData<T> data) {
        return ifNotNullCustomer(data.customer().orElse(null), customer -> data.address()
                .map(address -> data.form()
                        .map(form -> {
                            if (!form.hasErrors()) {
                                return applySubmittedAddress(customer, address, form.get());
                            } else {
                                return handleInvalidSubmittedAddress(customer, address, form);
                            }
                        }).orElseGet(() -> showForm(customer, address)))
                .orElseGet(() -> handleNotFoundAddress(customer)));
    }

    protected CompletionStage<Result> showForm(final Customer customer, final Address address) {
        return redirectToAddressBook();
    }

    protected CompletionStage<Result> handleNotFoundAddress(final Customer customer) {
        return redirectToAddressBook();
    }

    protected  <T> CompletionStage<Result> applySubmittedAddress(final Customer customer, final Address address, final T formData) {
        final CompletionStage<Result> resultStage = removeAddressFromCustomer(customer, address, formData)
                .thenComposeAsync(updatedCustomer -> displaySuccessfulCustomerUpdate(updatedCustomer, address, formData), HttpExecution.defaultContext());
        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                handleFailedCustomerUpdate(customer, address, formData, throwable));
    }

    protected <T> CompletionStage<Result> displaySuccessfulCustomerUpdate(final Customer customer, final Address address, final T formData) {
        return redirectToAddressBook();
    }

    protected <T> CompletionStage<Result> handleFailedCustomerUpdate(final Customer customer, final Address address,
                                                                     final T formData, final Throwable throwable) {
        if (throwable.getCause() instanceof SphereException) {
            saveUnexpectedError(throwable.getCause());
            final Form<?> form = obtainFilledForm();
            return asyncBadRequest(renderPage(customer, form));
        }
        return exceptionallyCompletedFuture(throwable);
    }

    protected <T> CompletionStage<Result> handleInvalidSubmittedAddress(final Customer customer, final Address address, final Form<T> form) {
        saveFormErrors(form);
        return asyncBadRequest(renderPage(customer, form));
    }

    protected <T> CompletionStage<Customer> removeAddressFromCustomer(final Customer customer, final Address address, final T formData) {
        final RemoveAddress updateAction = RemoveAddress.of(address);
        return sphere().execute(CustomerUpdateCommand.of(customer, updateAction));
    }

    protected CompletionStage<Html> renderPage(final Customer customer, final Form<?> form) {
        final AddressBookPageContent pageContent = addressBookPageContentFactory.create(customer);
        return renderPage(pageContent, getTemplateName());
    }

    protected Form<?> obtainFilledForm() {
        final RemoveAddressFormData formData = new RemoveAddressFormData();
        return formFactory.form(RemoveAddressFormData.class).fill(formData);
    }
}
