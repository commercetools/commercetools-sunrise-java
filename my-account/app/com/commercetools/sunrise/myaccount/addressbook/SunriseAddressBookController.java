package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.addresses.MyAddressFetcher;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddressBookController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final MyAddressFetcher myAddressFetcher;
    private final AddAddressFormAction addAddressFormAction;
    private final ChangeAddressFormAction changeAddressFormAction;
    private final RemoveAddressFormAction removeAddressFormAction;

    protected SunriseAddressBookController(final TemplateEngine templateEngine,
                                           final MyAddressFetcher myAddressFetcher,
                                           final AddAddressFormAction addAddressFormAction,
                                           final ChangeAddressFormAction changeAddressFormAction,
                                           final RemoveAddressFormAction removeAddressFormAction) {
        this.templateEngine = templateEngine;
        this.myAddressFetcher = myAddressFetcher;
        this.addAddressFormAction = addAddressFormAction;
        this.changeAddressFormAction = changeAddressFormAction;
        this.removeAddressFormAction = removeAddressFormAction;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render("my-account-address-book")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> showAddAddressForm() {
        return templateEngine.render("my-account-new-address")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> showChangeAddressForm(final String addressIdentifier) {
        return myAddressFetcher.require(addressIdentifier)
                .thenApply(address -> PageData.of().put("address", address))
                .thenComposeAsync(pageData -> templateEngine.render("my-account-edit-address", pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }

    @EnableHooks
    public CompletionStage<Result> addAddress() {
        return addAddressFormAction.apply(this::onAddressAdded,
                form -> {
                    final PageData pageData = PageData.of().put("addAddressForm", form);
                    return templateEngine.render("my-account-new-address", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> changeAddress() {
        return changeAddressFormAction.apply(this::onAddressChanged,
                form -> {
                    final PageData pageData = PageData.of().put("changeAddressForm", form);
                    return templateEngine.render("my-account-edit-address", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> removeAddress() {
        return removeAddressFormAction.apply(this::onAddressRemoved,
                form -> {
                    final PageData pageData = PageData.of().put("removeAddressForm", form);
                    return templateEngine.render("my-account-address-book", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    protected abstract Result onAddressAdded();

    protected abstract Result onAddressChanged();

    protected abstract Result onAddressRemoved();


    // TODO move this to templates
//    public void preFillFormData(final AddAddressFormData formData) {
//        final Address address = addressWithCustomer.getAddress();
//        final Customer customer = addressWithCustomer.getCustomer();
//        formData.applyAddress(address);
//        formData.applyDefaultShippingAddress(isDefaultAddress(address, customer.getDefaultShippingAddressId()));
//        formData.applyDefaultBillingAddress(isDefaultAddress(address, customer.getDefaultBillingAddressId()));
//    }
//
//    private boolean isDefaultAddress(final Address address, @Nullable final String defaultAddressId) {
//        return Objects.equals(defaultAddressId, address.getId());
//    }
}