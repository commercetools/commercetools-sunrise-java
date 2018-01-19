package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.addresses.MyAddressFetcher;
import com.commercetools.sunrise.myaccount.addressbook.AddAddressFormAction;
import com.commercetools.sunrise.myaccount.addressbook.ChangeAddressFormAction;
import com.commercetools.sunrise.myaccount.addressbook.RemoveAddressFormAction;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class AddressBookController extends SunriseController {

    private static final String ADDRESS_BOOK_TEMPLATE = "my-account-address-book";
    private static final String NEW_ADDRESS_TEMPLATE = "my-account-new-address";
    private static final String EDIT_ADDRESS_TEMPLATE = "my-account-edit-address";

    private final TemplateEngine templateEngine;
    private final MyAddressFetcher myAddressFetcher;
    private final AddAddressFormAction addAddressFormAction;
    private final ChangeAddressFormAction changeAddressFormAction;
    private final RemoveAddressFormAction removeAddressFormAction;

    @Inject
    AddressBookController(final TemplateEngine templateEngine,
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
        return templateEngine.render(ADDRESS_BOOK_TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> showAddAddressForm() {
        return templateEngine.render(NEW_ADDRESS_TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> addAddress() {
        return addAddressFormAction.apply(
                () -> routes.AddressBookController.show(),
                form -> templateEngine.render(NEW_ADDRESS_TEMPLATE, PageData.of().put("addAddressForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> showChangeAddressForm(final String addressIdentifier) {
        return myAddressFetcher.require(addressIdentifier)
                .thenApply(address -> PageData.of().put("address", address))
                .thenComposeAsync(pageData -> templateEngine.render(EDIT_ADDRESS_TEMPLATE, pageData)
                        .thenApply(Results::ok), HttpExecution.defaultContext());
    }

    @EnableHooks
    public CompletionStage<Result> changeAddress() {
        return changeAddressFormAction.apply(
                () -> routes.AddressBookController.show(),
                form -> templateEngine.render(EDIT_ADDRESS_TEMPLATE, PageData.of().put("changeAddressForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> removeAddress() {
        return removeAddressFormAction.apply(
                () -> routes.AddressBookController.show(),
                form -> templateEngine.render(ADDRESS_BOOK_TEMPLATE, PageData.of().put("removeAddressForm", form)));
    }
}
