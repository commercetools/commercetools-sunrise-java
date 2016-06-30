package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SimpleFormBindingControllerTrait;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.myaccount.CustomerFinderBySession;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookManagementController;
import com.commercetools.sunrise.myaccount.addressbook.DefaultAddressBookAddressFormData;
import com.google.inject.Injector;
import io.sphere.sdk.client.BadRequestException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.AddAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultBillingAddress;
import io.sphere.sdk.customers.commands.updateactions.SetDefaultShippingAddress;
import io.sphere.sdk.models.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public abstract class SunriseAddAddressController extends AddressBookManagementController implements WithOverwriteableTemplateName, SimpleFormBindingControllerTrait<AddressBookAddressFormData, Customer, Customer> {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseAddAddressController.class);

    @Inject
    private Injector injector;

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

    @Override
    public Class<? extends AddressBookAddressFormData> getFormDataClass() {
        return DefaultAddressBookAddressFormData.class;
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            logger.debug("show new address form for address in locale={}", languageTag);
            return injector.getInstance(CustomerFinderBySession.class).findCustomer(session())
                    .thenComposeAsync(customerOpt -> {
                        Customer nullableCustomer = customerOpt.orElse(null);
                        return validateInput(nullableCustomer, this::showForm);
                    }, HttpExecution.defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            logger.debug("try to add address with in locale={}", languageTag);
            return injector.getInstance(CustomerFinderBySession.class).findCustomer(session())
                    .thenComposeAsync(customerOpt -> {
                        Customer nullableCustomer = customerOpt.orElse(null);
                        return validateInput(nullableCustomer, this::validateForm);
                    }, HttpExecution.defaultContext());
        });
    }

    @Override
    public CompletionStage<Result> showForm(final Customer customer) {
        final Form<? extends AddressBookAddressFormData> filledForm = createFilledForm(customer, null);
        return asyncOk(renderPage(filledForm, customer));
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Form<? extends AddressBookAddressFormData> form, final Customer customer) {
        saveFormErrors(form);
        return asyncBadRequest(renderPage(form, customer));
    }

    @Override
    public CompletionStage<? extends Customer> doAction(final AddressBookAddressFormData formData, final Customer customer) {
        final Address address = formData.toAddress();
        return addAddress(customer, address)
                .thenComposeAsync(updatedCustomer -> findAddressId(updatedCustomer, address)
                        .map(addressId -> setAddressAsDefault(updatedCustomer, addressId, formData))
                        .orElseGet(() -> completedFuture(updatedCustomer)));
    }

    @Override
    public CompletionStage<Result> handleFailedAction(final AddressBookAddressFormData formData, final Customer customer, final Throwable throwable) {
        if (throwable.getCause() instanceof BadRequestException) {
            saveUnexpectedError(throwable.getCause());
            final Form<? extends AddressBookAddressFormData> filledForm = createFilledForm(customer, formData.toAddress());
            return asyncBadRequest(renderPage(filledForm, customer));
        }
        return exceptionallyCompletedFuture(throwable);
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final AddressBookAddressFormData formData, final Customer oldCustomer, final Customer updatedCustomer) {
        return redirectToAddressBook();
    }

    protected CompletionStage<Html> renderPage(final Form<? extends AddressBookAddressFormData> form, final Customer customer) {
        final AddAddressPageContent pageContent = injector.getInstance(AddAddressPageContentFactory.class).create(customer, form);
        return renderPage(pageContent, getTemplateName());
    }

    protected Form<? extends AddressBookAddressFormData> createFilledForm(final Customer customer, @Nullable final Address address) {
        final DefaultAddressBookAddressFormData formData = new DefaultAddressBookAddressFormData();
        formData.apply(customer, address);
        return formFactory().form(DefaultAddressBookAddressFormData.class).fill(formData);
    }

    protected final CompletionStage<Result> validateInput(@Nullable final Customer nullableCustomer,
                                                          final Function<Customer, CompletionStage<Result>> onValidInput) {
        return ifValidCustomer(nullableCustomer, onValidInput::apply);
    }

    private CompletionStage<Customer> addAddress(final Customer customer, final Address address) {
        final AddAddress addAddressAction = AddAddress.of(address);
        return sphere().execute(CustomerUpdateCommand.of(customer, addAddressAction));
    }

    private <T extends AddressBookAddressFormData> CompletionStage<Customer> setAddressAsDefault(final Customer customer, final String addressId, final T formData) {
        final List<UpdateAction<Customer>> updateActions = new ArrayList<>();
        if (formData.isDefaultShippingAddress()) {
            updateActions.add(SetDefaultShippingAddress.of(addressId));
        }
        if (formData.isDefaultBillingAddress()) {
            updateActions.add(SetDefaultBillingAddress.of(addressId));
        }
        if (!updateActions.isEmpty()) {
            return sphere().execute(CustomerUpdateCommand.of(customer, updateActions));
        } else {
            return completedFuture(customer);
        }
    }

    private Optional<String> findAddressId(final Customer customer, final Address address) {
        return customer.getAddresses().stream()
                .filter(a -> a.equalsIgnoreId(address))
                .findFirst()
                .map(Address::getId);
    }
}
