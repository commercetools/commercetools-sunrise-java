package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import com.commercetools.sunrise.myaccount.addressbook.SunriseAddressBookManagementController;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.view.ChangeAddressPageContent;
import com.commercetools.sunrise.myaccount.addressbook.changeaddress.view.ChangeAddressPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public abstract class SunriseChangeAddressController<F extends AddressBookAddressFormData> extends SunriseAddressBookManagementController implements WithTemplateName, WithFormFlow<F, AddressWithCustomer, Customer> {

    private final ChangeAddressFunction changeAddressFunction;
    private final ChangeAddressPageContentFactory changeAddressPageContentFactory;

    protected SunriseChangeAddressController(final CustomerFinder customerFinder, final ChangeAddressFunction changeAddressFunction,
                                             final ChangeAddressPageContentFactory changeAddressPageContentFactory) {
        super(customerFinder);
        this.changeAddressFunction = changeAddressFunction;
        this.changeAddressPageContentFactory = changeAddressPageContentFactory;
    }

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

    @SunriseRoute("changeAddressInAddressBookCall")
    public CompletionStage<Result> show(final String languageTag, final String addressId) {
        return doRequest(() -> requireAddress(addressId, this::showForm));
    }

    @SunriseRoute("changeAddressInAddressBookProcessFormCall")
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return doRequest(() -> requireAddress(addressId, this::validateForm));
    }

    @Override
    public CompletionStage<Customer> doAction(final F formData, final AddressWithCustomer addressWithCustomer) {
        return changeAddressFunction.apply(addressWithCustomer, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final AddressWithCustomer addressWithCustomer, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, addressWithCustomer, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final AddressWithCustomer addressWithCustomer, final Customer updatedCustomer);

    @Override
    public void preFillFormData(final F formData, final AddressWithCustomer input) {
        formData.applyAddress(input.getAddress());
        formData.setDefaultShippingAddress(isDefaultAddress(input.getAddress(), input.getCustomer().getDefaultShippingAddressId()));
        formData.setDefaultBillingAddress(isDefaultAddress(input.getAddress(), input.getCustomer().getDefaultBillingAddressId()));
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final AddressWithCustomer addressWithCustomer, @Nullable final Customer updatedCustomer) {
        final ChangeAddressPageContent pageContent = changeAddressPageContentFactory.create(firstNonNull(updatedCustomer, addressWithCustomer.getCustomer()), form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    private boolean isDefaultAddress(final Address address, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, address.getId());
    }
}