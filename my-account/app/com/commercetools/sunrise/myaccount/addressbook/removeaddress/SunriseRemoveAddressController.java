package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.AddressWithCustomer;
import com.commercetools.sunrise.myaccount.addressbook.SunriseAddressBookManagementController;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContent;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public abstract class SunriseRemoveAddressController<F extends RemoveAddressFormData> extends SunriseAddressBookManagementController implements WithTemplateName, WithFormFlow<F, AddressWithCustomer, Customer> {

    private final RemoveAddressExecutor removeAddressExecutor;
    private final AddressBookPageContentFactory addressBookPageContentFactory;

    protected SunriseRemoveAddressController(final CustomerFinder customerFinder, final RemoveAddressExecutor removeAddressExecutor,
                                             final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(customerFinder);
        this.removeAddressExecutor = removeAddressExecutor;
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
        return doRequest(() -> requireAddress(addressId, this::processForm));
    }

    @Override
    public CompletionStage<Customer> doAction(final F formData, final AddressWithCustomer addressWithCustomer) {
        return removeAddressExecutor.apply(addressWithCustomer, formData);
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
        // Do not pre-fill anything
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final AddressWithCustomer addressWithCustomer, @Nullable final Customer updatedCustomer) {
        final AddressBookPageContent pageContent = addressBookPageContentFactory.create(firstNonNull(updatedCustomer, addressWithCustomer.getCustomer()));
        return renderPageWithTemplate(pageContent, getTemplateName());
    }
}
