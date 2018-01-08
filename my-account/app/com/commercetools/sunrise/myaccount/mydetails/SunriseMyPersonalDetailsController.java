package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.myaccount.mydetails.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.myaccount.MyAccountController;
import com.commercetools.sunrise.myaccount.mydetails.viewmodels.MyPersonalDetailsPageContentFactory;
import io.sphere.sdk.customers.Customer;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseMyPersonalDetailsController extends SunriseContentFormController
        implements MyAccountController, WithContentFormFlow<Void, Customer, MyPersonalDetailsFormData> {

    private final MyPersonalDetailsFormData formData;
    private final MyPersonalDetailsControllerAction controllerAction;
    private final MyPersonalDetailsPageContentFactory pageContentFactory;

    protected SunriseMyPersonalDetailsController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                 final MyPersonalDetailsFormData formData,
                                                 final MyPersonalDetailsControllerAction controllerAction,
                                                 final MyPersonalDetailsPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends MyPersonalDetailsFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(MyPersonalDetailsReverseRouter.MY_PERSONAL_DETAILS_PAGE)
    public CompletionStage<Result> show() {
        return showFormPage(null, formData);
    }

    @EnableHooks
    @SunriseRoute(MyPersonalDetailsReverseRouter.MY_PERSONAL_DETAILS_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Customer> executeAction(final Void input, final MyPersonalDetailsFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final MyPersonalDetailsFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends MyPersonalDetailsFormData> form) {
        return pageContentFactory.create(null, form);
    }

    // TODO prefill in templates
    @Override
    public void preFillFormData(final Void input, final MyPersonalDetailsFormData formData) {
//        formData.applyCustomerName(customer.getName());
//        formData.applyEmail(customer.getEmail());
    }
}
