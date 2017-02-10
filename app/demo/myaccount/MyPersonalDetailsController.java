package demo.myaccount;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;
import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsLocalizedReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsFormData;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsPageContentFactory;
import com.commercetools.sunrise.myaccount.mydetails.SunriseMyPersonalDetailsController;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@RequestScoped
public class MyPersonalDetailsController extends SunriseMyPersonalDetailsController {

    private final MyPersonalDetailsLocalizedReverseRouter myPersonalDetailsLocalizedReverseRouter;
    private final AuthenticationLocalizedReverseRouter authenticationLocalizedReverseRouter;

    @Inject
    public MyPersonalDetailsController(final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory,
                                       final CustomerFinder customerFinder, final MyPersonalDetailsLocalizedReverseRouter myPersonalDetailsLocalizedReverseRouter,
                                       final AuthenticationLocalizedReverseRouter authenticationLocalizedReverseRouter) {
        super(myPersonalDetailsPageContentFactory, customerFinder);
        this.myPersonalDetailsLocalizedReverseRouter = myPersonalDetailsLocalizedReverseRouter;
        this.authenticationLocalizedReverseRouter = authenticationLocalizedReverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final MyPersonalDetailsFormData formData, final Customer customer, final Customer updatedCustomer) {
        return redirectTo(myPersonalDetailsLocalizedReverseRouter.myPersonalDetailsPageCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationLocalizedReverseRouter.showLogInForm());
    }
}
