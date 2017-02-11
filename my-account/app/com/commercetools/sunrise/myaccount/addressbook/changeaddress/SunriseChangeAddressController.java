package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookActionData;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import com.commercetools.sunrise.myaccount.addressbook.SunriseAddressBookManagementController;
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

public abstract class SunriseChangeAddressController<F extends AddressBookAddressFormData> extends SunriseAddressBookManagementController implements WithTemplateName, WithFormFlow<F, AddressBookActionData, Customer> {

    private final ChangeAddressExecutor changeAddressExecutor;
    private final ChangeAddressPageContentFactory changeAddressPageContentFactory;

    protected SunriseChangeAddressController(final CustomerFinder customerFinder, final ChangeAddressExecutor changeAddressExecutor,
                                             final ChangeAddressPageContentFactory changeAddressPageContentFactory) {
        super(customerFinder);
        this.changeAddressExecutor = changeAddressExecutor;
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
    public CompletionStage<? extends Customer> doAction(final F formData, final AddressBookActionData context) {
        return changeAddressExecutor.changeAddress(context.getCustomer(), context.getAddress(), formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final AddressBookActionData context, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, context, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final AddressBookActionData context, final Customer updatedCustomer);

    @Override
    public void preFillFormData(final F formData, final AddressBookActionData context) {
        formData.applyAddress(context.getAddress());
        formData.setDefaultShippingAddress(isDefaultAddress(context.getAddress(), context.getCustomer().getDefaultShippingAddressId()));
        formData.setDefaultBillingAddress(isDefaultAddress(context.getAddress(), context.getCustomer().getDefaultBillingAddressId()));
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final AddressBookActionData context, @Nullable final Customer updatedCustomer) {
        final ChangeAddressControllerData addAddressControllerData = new ChangeAddressControllerData(form, context.getCustomer(), updatedCustomer);
        final ChangeAddressPageContent pageContent = changeAddressPageContentFactory.create(addAddressControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    private boolean isDefaultAddress(final Address address, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, address.getId());
    }
}