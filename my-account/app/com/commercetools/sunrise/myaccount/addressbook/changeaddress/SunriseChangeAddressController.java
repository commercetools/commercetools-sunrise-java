package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SimpleFormBindingControllerTrait;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookManagementController;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressBookAddressFormData;
import com.google.inject.Injector;
import io.sphere.sdk.client.BadRequestException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.ChangeAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.models.Address;
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
public class SunriseChangeAddressController extends AddressBookManagementController implements WithOverwriteableTemplateName, SimpleFormBindingControllerTrait<AddressBookAddressFormData> {

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
            return injector.getInstance(ChangeAddressActionDataDefaultProvider.class).getActionData(session(), addressId)
                    .thenComposeAsync(this::showChangeAddress, HttpExecution.defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return doRequest(() -> {
            logger.debug("try to change address with id={} in locale={}", addressId, languageTag);
            Form<DefaultAddressBookAddressFormData> form = formFactory.form(DefaultAddressBookAddressFormData.class).bindFromRequest();
            return injector.getInstance(ChangeAddressActionDataDefaultProvider.class).getActionData(session(), addressId)
                    .thenComposeAsync(actionData -> processChangeAddress(actionData, form), HttpExecution.defaultContext());
        });
    }

    protected CompletionStage<Result> showChangeAddress(final ChangeAddressActionData data) {
        return ifNotNullCustomer(data.customer().orElse(null), notNullCustomer -> data.oldAddress()
                .map(oldAddress -> showFormWithOriginalAddress(notNullCustomer, oldAddress))
                .orElseGet(() -> handleNotFoundOriginalAddress(notNullCustomer)));
    }

    protected CompletionStage<Result> processChangeAddress(final ChangeAddressActionData data, final Form<? extends AddressBookAddressFormData> form) {
        return ifNotNullCustomer(data.customer().orElse(null), customer -> data.oldAddress()
                .map(oldAddress -> {
                    if (!form.hasErrors()) {
                        return handleValidForm(customer, oldAddress, form.get());
                    } else {
                        return handleInvalidForm(customer, oldAddress, form);
                    }
                }).orElseGet(() -> handleNotFoundOriginalAddress(customer)));
    }

    @Override
    protected CompletionStage<Result> handleValidForm(final Customer customer, final Address oldAddress, final AddressBookAddressFormData formData) {
        final CompletionStage<Result> resultStage = changeAddressFromCustomer(customer, oldAddress, formData)
                .thenComposeAsync(updatedCustomer -> displaySuccessfulCustomerUpdate(updatedCustomer, oldAddress, formData), HttpExecution.defaultContext());
        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                handleFailedCustomerUpdate(customer, oldAddress, formData, throwable));
    }

    @Override
    protected CompletionStage<Result> handleInvalidForm(final Customer customer, final Address oldAddress, final Form<? extends AddressBookAddressFormData> form) {
        saveFormErrors(form);
        return asyncBadRequest(renderPage(customer, form));
    }

    protected CompletionStage<Result> showFormWithOriginalAddress(final Customer customer, final Address oldAddress) {
        final Form<?> form = obtainFilledForm(customer, oldAddress);
        return asyncOk(renderPage(customer, form));
    }

    protected CompletionStage<Result> handleNotFoundOriginalAddress(final Customer customer) {  // TODO move up to common controller
        return redirectToAddressBook();
    }

    protected CompletionStage<Result> displaySuccessfulCustomerUpdate(final Customer customer, final Address oldAddress, final AddressBookAddressFormData formData) {
        return redirectToAddressBook();
    }

    protected CompletionStage<Result> handleFailedCustomerUpdate(final Customer customer, final Address oldAddress,
                                                                 final AddressBookAddressFormData formData, final Throwable throwable) {
        if (throwable.getCause() instanceof BadRequestException) {
            saveUnexpectedError(throwable.getCause());
            final Form<? extends AddressBookAddressFormData> form = obtainFilledForm(customer, formData.toAddress());
            return asyncBadRequest(renderPage(customer, form));
        }
        return exceptionallyCompletedFuture(throwable);
    }

    protected CompletionStage<Customer> changeAddressFromCustomer(final Customer customer, final Address oldAddress, final AddressBookAddressFormData formData) {
        return changeAddress(customer, oldAddress, formData);
    }

    private CompletionStage<Customer> changeAddress(final Customer customer, final Address oldAddress, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        updateActions.add(ChangeAddress.ofOldAddressToNewAddress(oldAddress, formData.toAddress()));
        updateActions.addAll(setDefaultAddressActions(customer, oldAddress.getId(), formData));
        return sphere().execute(CustomerUpdateCommand.of(customer, updateActions));
    }

    protected CompletionStage<Html> renderPage(final Customer customer, final Form<?> form) {
        final ChangeAddressPageContent pageContent = injector.getInstance(ChangeAddressPageContentFactory.class).create(customer, form);
        return renderPage(pageContent, getTemplateName());
    }

    protected Form<? extends AddressBookAddressFormData> obtainFilledForm(final Customer customer, @Nullable final Address address) {
        final DefaultAddressBookAddressFormData formData = new DefaultAddressBookAddressFormData();
        formData.apply(customer, address);
        return formFactory.form(DefaultAddressBookAddressFormData.class).fill(formData);
    }

    @Override
    public Class<? extends AddressBookAddressFormData> getFormDataClass() {
        return DefaultAddressBookAddressFormData.class;
    }

    private List<UpdateAction<Customer>> setDefaultAddressActions(final Customer customer, final String addressId, final AddressBookAddressFormData formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        setDefaultAddressAction(addressId, formData.isDefaultShippingAddress(), customer.getDefaultShippingAddressId(), SetDefaultShippingAddress::of)
                .ifPresent(updateActions::add);
        setDefaultAddressAction(addressId, formData.isDefaultBillingAddress(), customer.getDefaultBillingAddressId(), SetDefaultBillingAddress::of)
                .ifPresent(updateActions::add);
        return updateActions;
    }

    private Optional<UpdateAction<Customer>> setDefaultAddressAction(final String addressId, final boolean isNewDefaultAddress,
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