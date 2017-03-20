package controllers.common;

import com.commercetools.sunrise.common.localization.changelanguage.ChangeLanguageControllerAction;
import com.commercetools.sunrise.common.localization.changelanguage.ChangeLanguageFormData;
import com.commercetools.sunrise.common.localization.changelanguage.SunriseChangeLanguageController;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.home.HomeReverseRouter;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class ChangeLanguageController extends SunriseChangeLanguageController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public ChangeLanguageController(final FormFactory formFactory,
                                    final ChangeLanguageFormData formData,
                                    final ChangeLanguageControllerAction controllerAction,
                                    final HomeReverseRouter homeReverseRouter) {
        super(formFactory, formData, controllerAction);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Void input, final Form<? extends ChangeLanguageFormData> form) {
        return redirectToCall(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<? extends ChangeLanguageFormData> form, final ClientErrorException clientErrorException) {
        return redirectToCall(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output, final ChangeLanguageFormData formData) {
        return redirectToCall(homeReverseRouter.homePageCall(formData.locale().toLanguageTag()));
    }
}
