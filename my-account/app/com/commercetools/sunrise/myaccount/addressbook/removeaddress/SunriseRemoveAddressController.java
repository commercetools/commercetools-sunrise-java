package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookActionData;
import com.commercetools.sunrise.myaccount.addressbook.SunriseAddressBookManagementController;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.AddressBookControllerData;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.AddressBookPageContent;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.AddressBookPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

public abstract class SunriseRemoveAddressController<F extends RemoveAddressFormData> extends SunriseAddressBookManagementController implements WithTemplateName, WithFormFlow<F, AddressBookActionData, Customer> {

    private final RemoveAddressFunction removeAddressFunction;
    private final AddressBookPageContentFactory addressBookPageContentFactory;

    protected SunriseRemoveAddressController(final CustomerFinder customerFinder, final RemoveAddressFunction removeAddressFunction,
                                             final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(customerFinder);
        this.removeAddressFunction = removeAddressFunction;
        this.addressBookPageContentFactory = addressBookPageContentFactory;
    }

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

    @SunriseRoute("removeAddressFromAddressBookProcessFormCall")
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return doRequest(() -> requireAddress(addressId, this::validateForm));
    }

    @Override
    public CompletionStage<Customer> doAction(final F formData, final AddressBookActionData context) {
        return removeAddressFunction.apply(formData, context);
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
        // Do not pre-fill anything
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final AddressBookActionData context, @Nullable final Customer updatedCustomer) {
        final AddressBookControllerData addressBookControllerData = new AddressBookControllerData(context.getCustomer(), updatedCustomer);
        final AddressBookPageContent pageContent = addressBookPageContentFactory.create(addressBookControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }
}
