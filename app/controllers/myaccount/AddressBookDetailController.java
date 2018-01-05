package controllers.myaccount;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.myaccount.authentication.AuthenticationReverseRouter;
import com.commercetools.sunrise.models.customers.CustomerFetcher;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.SunriseAddressBookDetailController;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels.AddressBookPageContentFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class AddressBookDetailController extends SunriseAddressBookDetailController {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public AddressBookDetailController(final ContentRenderer contentRenderer,
                                       final CustomerFetcher customerFinder,
                                       final AddressBookPageContentFactory pageContentFactory,
                                       final AuthenticationReverseRouter authenticationReverseRouter) {
        super(contentRenderer, customerFinder, pageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-address-book";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectToCall(authenticationReverseRouter.logInPageCall());
    }
}
