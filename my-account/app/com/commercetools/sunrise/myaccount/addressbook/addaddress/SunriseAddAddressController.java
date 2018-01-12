package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.controllers.WithForm;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseAddAddressController extends SunriseContentController implements WithContent, WithForm {

    private final AddAddressControllerAction controllerAction;

    protected SunriseAddAddressController(final ContentRenderer contentRenderer,
                                          final AddAddressControllerAction controllerAction) {
        super(contentRenderer);
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADD_ADDRESS_PAGE)
    public CompletionStage<Result> show() {
        return okResult(PageData.of());
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.ADD_ADDRESS_PROCESS)
    public CompletionStage<Result> process() {
        final Form<? extends AddAddressFormData> form = controllerAction.bindForm();
        if (form.hasErrors()) {
            return badRequestResult(PageData.of().putField("addAddressForm", form));
        } else {
            return controllerAction.apply(form.get())
                    .thenApplyAsync(x -> handleSuccessfulAction(), HttpExecution.defaultContext());
        }
    }

    // TODO move this to template
    public void preFillFormData(final AddAddressFormData formData) {
//        final Address address = Address.of(country)
//                .withTitle(customer.getTitle())
//                .withFirstName(customer.getFirstName())
//                .withLastName(customer.getLastName())
//                .withEmail(customer.getEmail());
//        formData.applyAddress(address);
    }
}
