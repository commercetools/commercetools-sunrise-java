package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentForm2Flow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import com.commercetools.sunrise.myaccount.MyAccountController;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseRemoveAddressController extends SunriseContentFormController
        implements MyAccountController, WithContentForm2Flow<Void, Customer, RemoveAddressFormData> {

    private final RemoveAddressFormData formData;
    private final RemoveAddressControllerAction controllerAction;

    protected SunriseRemoveAddressController(final ContentRenderer contentRenderer,
                                             final FormFactory formFactory,
                                             final RemoveAddressFormData formData,
                                             final RemoveAddressControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends RemoveAddressFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(AddressBookReverseRouter.REMOVE_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String addressIdentifier) {
        return processForm(null);
    }

    @Override
    public CompletionStage<Customer> executeAction(final Void input, final RemoveAddressFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final RemoveAddressFormData formData);

    @Override
    public void preFillFormData(final Void input, final RemoveAddressFormData formData) {
        // Do not pre-fill anything
    }

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends RemoveAddressFormData> form) {
        return new BlankPageContent();
    }
}
