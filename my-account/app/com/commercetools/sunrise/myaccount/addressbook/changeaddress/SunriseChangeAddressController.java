package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookManagementController;
import com.commercetools.sunrise.myaccount.addressbook.AddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressFormData;
import com.google.inject.Injector;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
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
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;

@RequestScoped
public abstract class SunriseChangeAddressController extends AddressBookManagementController implements WithOverwriteableTemplateName {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseChangeAddressController.class);

    @Inject
    private Injector injector;
    @Inject
    private FormFactory formFactory;

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
            logger.debug("show edit form for address with id={} in locale={}", addressId, languageTag);
            return injector.getInstance(ChangeAddressActionDataDefaultProvider.class).getActionData(session(), addressId, null)
                    .thenComposeAsync(this::showChangeAddress, HttpExecution.defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return doRequest(() -> {
            logger.debug("try to change address with id={} in locale={}", addressId, languageTag);
            Form<DefaultAddressFormData> form = formFactory.form(DefaultAddressFormData.class).bindFromRequest();
            return injector.getInstance(ChangeAddressActionDataDefaultProvider.class).getActionData(session(), addressId, form)
                    .thenComposeAsync(this::processChangeAddress, HttpExecution.defaultContext());
        });
    }

    protected <T extends AddressFormData> CompletionStage<Result> showChangeAddress(final ChangeAddressActionData<T> data) {
        return ifNotNullCustomer(data.customer().orElse(null), notNullCustomer -> data.oldAddress()
                .map(oldAddress -> showFormWithOriginalAddress(notNullCustomer, oldAddress))
                .orElseGet(() -> handleNotFoundOriginalAddress(notNullCustomer)));
    }

    protected <T extends AddressFormData> CompletionStage<Result> processChangeAddress(final ChangeAddressActionData<T> data) {
        return ifNotNullCustomer(data.customer().orElse(null), customer -> data.oldAddress()
                .map(oldAddress -> data.form()
                        .map(form -> {
                            if (!form.hasErrors()) {
                                return applySubmittedAddress(customer, oldAddress, form.get());
                            } else {
                                return handleInvalidSubmittedAddress(customer, oldAddress, form);
                            }
                        }).orElseGet(() -> showFormWithOriginalAddress(customer, oldAddress)))
                .orElseGet(() -> handleNotFoundOriginalAddress(customer)));
    }

    protected CompletionStage<Result> showFormWithOriginalAddress(final Customer customer, final Address oldAddress) {
        final Form<?> form = obtainFilledForm(oldAddress);
        return asyncOk(renderPage(customer, form));
    }

    protected CompletionStage<Result> handleNotFoundOriginalAddress(final Customer customer) {  // TODO move up to common controller
        return redirectToAddressBook();
    }

    protected <T extends AddressFormData> CompletionStage<Result> handleInvalidSubmittedAddress(final Customer customer, final Address oldAddress, final Form<T> form) {
        saveFormErrors(form);
        return asyncBadRequest(renderPage(customer, form));
    }

    protected <T extends AddressFormData> CompletionStage<Result> applySubmittedAddress(final Customer customer, final Address oldAddress, final T formData) {
        final CompletionStage<Result> resultStage = changeAddressFromCustomer(customer, oldAddress, formData)
                .thenComposeAsync(updatedCustomer -> displaySuccessfulCustomerUpdate(updatedCustomer, oldAddress, formData), HttpExecution.defaultContext());
        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                handleFailedCustomerUpdate(customer, oldAddress, formData, throwable));
    }

    protected <T extends AddressFormData> CompletionStage<Result> displaySuccessfulCustomerUpdate(final Customer customer, final Address oldAddress, final T formData) {
        return redirectToAddressBook();
    }

    protected <T extends AddressFormData> CompletionStage<Result> handleFailedCustomerUpdate(final Customer customer, final Address oldAddress,
                                                                                             final T formData, final Throwable throwable) {
        if (throwable.getCause() instanceof SphereException) {
            saveUnexpectedError((SphereException) throwable.getCause());
            final Form<?> form = obtainFilledForm(formData.toAddress());
            return asyncBadRequest(renderPage(customer, form));
        }
        return exceptionallyCompletedFuture(throwable);
    }

    protected <T extends AddressFormData> CompletionStage<Customer> changeAddressFromCustomer(final Customer customer, final Address oldAddress, final T formData) {
        return changeAddress(customer, oldAddress, formData);
    }

    private <T extends AddressFormData> CompletionStage<Customer> changeAddress(final Customer customer, final Address oldAddress, final T formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(ChangeAddress.ofOldAddressToNewAddress(oldAddress, formData.toAddress()));
        updateActions.addAll(setDefaultAddressActions(customer, oldAddress.getId(), formData));
        return sphere().execute(CustomerUpdateCommand.of(customer, updateActions));
    }

    protected CompletionStage<Html> renderPage(final Customer customer, final Form<?> form) {
        throw new NotImplementedException("Change address controller page");
        //return renderPage(pageContent, getTemplateName();
    }

    protected Form<?> obtainFilledForm(@Nullable final Address address) {
        final DefaultAddressFormData formData = new DefaultAddressFormData();
        formData.apply(address);
        return formFactory.form(DefaultAddressFormData.class).fill(formData);
    }

    private <T extends AddressFormData> List<UpdateAction<Customer>> setDefaultAddressActions(final Customer customer, final String addressId, final T formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        defaultAddressAction(addressId, formData.isDefaultShippingAddress(), customer.getDefaultShippingAddressId(), SetDefaultShippingAddress::of)
                .ifPresent(updateActions::add);
        defaultAddressAction(addressId, formData.isDefaultBillingAddress(), customer.getDefaultBillingAddressId(), SetDefaultBillingAddress::of)
                .ifPresent(updateActions::add);
        return updateActions;
    }

    private Optional<UpdateAction<Customer>> defaultAddressAction(final String addressId, final boolean isNewDefaultAddress,
                                                                  @Nullable final String defaultAddressId,
                                                                  final Function<String, UpdateAction<Customer>> actionCreator) {
        final boolean defaultNeedsChange = isDefaultAddressDifferent(addressId, isNewDefaultAddress, defaultAddressId);
        if (defaultNeedsChange) {
            final String addressIdToSetAsDefault = isNewDefaultAddress ? addressId : null;
            return Optional.of(actionCreator.apply(addressIdToSetAsDefault));
        }
        return Optional.empty();
    }

    private boolean isDefaultAddressDifferent(final String addressId, final boolean isNewDefaultAddress, @Nullable final String defaultAddressId) {
        final boolean isDefaultAddress = Objects.equals(defaultAddressId, addressId);
        return isNewDefaultAddress ^ isDefaultAddress;
    }
}