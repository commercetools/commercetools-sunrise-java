package controllers.common;

import com.commercetools.sunrise.common.localization.changecountry.ChangeCountryControllerAction;
import com.commercetools.sunrise.common.localization.changecountry.ChangeCountryFormData;
import com.commercetools.sunrise.common.localization.changecountry.SunriseChangeCountryController;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.home.HomeReverseRouter;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class ChangeCountryController extends SunriseChangeCountryController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public ChangeCountryController(final FormFactory formFactory,
                                   final ChangeCountryFormData formData,
                                   final ChangeCountryControllerAction controllerAction,
                                   final HomeReverseRouter homeReverseRouter) {
        super(formFactory, formData, controllerAction);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Void input, final Form<? extends ChangeCountryFormData> form) {
        return redirectToCall(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<? extends ChangeCountryFormData> form, final ClientErrorException clientErrorException) {
        return redirectToCall(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output, final ChangeCountryFormData formData) {
        return redirectToCall(homeReverseRouter.homePageCall());
    }
}
