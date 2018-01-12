package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsFormAction;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsFormData;
import com.commercetools.sunrise.myaccount.mydetails.SunriseMyPersonalDetailsController;
import io.sphere.sdk.customers.Customer;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class MyPersonalDetailsController extends SunriseMyPersonalDetailsController {

    @Inject
    public MyPersonalDetailsController(final ContentRenderer contentRenderer,
                                       final FormFactory formFactory,
                                       final MyPersonalDetailsFormData formData,
                                       final MyPersonalDetailsFormAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "my-account-personal-details";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Customer updatedCustomer, final MyPersonalDetailsFormData formData) {
        return redirectAsync(routes.MyPersonalDetailsController.show());
    }
}
