package com.commercetools.sunrise.myaccount.addressbook.changeaddress;


import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.controllers.WithForm;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.addresses.MyAddressFetcher;
import com.commercetools.sunrise.myaccount.addressbook.addaddress.AddAddressFormData;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

public abstract class SunriseChangeAddressController extends SunriseContentController implements WithContent, WithForm {

    private final ChangeAddressControllerAction controllerAction;
    private final MyAddressFetcher myAddressFetcher;

    protected SunriseChangeAddressController(final ContentRenderer contentRenderer,
                                             final ChangeAddressControllerAction controllerAction,
                                             final MyAddressFetcher myAddressFetcher) {
        super(contentRenderer);
        this.controllerAction = controllerAction;
        this.myAddressFetcher = myAddressFetcher;
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.CHANGE_ADDRESS_PAGE)
    public CompletionStage<Result> show(final String addressIdentifier) {
        return myAddressFetcher.require(addressIdentifier)
                .thenComposeAsync(address -> okResult(PageData.of().putField("address", address)), HttpExecution.defaultContext());
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.CHANGE_ADDRESS_PROCESS)
    public CompletionStage<Result> process() {
        final Form<? extends ChangeAddressFormData> form = controllerAction.bindForm();
        if (form.hasErrors()) {
            return badRequestResult(PageData.of().putField("changeAddressForm", form));
        } else {
            return controllerAction.apply(form.get())
                    .thenApplyAsync(x -> handleSuccessfulAction(), HttpExecution.defaultContext());
        }
    }

      // TODO move this to templates
    public void preFillFormData(final AddAddressFormData formData) {
//        final Address address = addressWithCustomer.getAddress();
//        final Customer customer = addressWithCustomer.getCustomer();
//        formData.applyAddress(address);
//        formData.applyDefaultShippingAddress(isDefaultAddress(address, customer.getDefaultShippingAddressId()));
//        formData.applyDefaultBillingAddress(isDefaultAddress(address, customer.getDefaultBillingAddressId()));
    }


    private boolean isDefaultAddress(final Address address, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, address.getId());
    }
}