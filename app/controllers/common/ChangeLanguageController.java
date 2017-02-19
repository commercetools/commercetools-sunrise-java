package controllers.common;

import com.commercetools.sunrise.common.localization.changelanguage.ChangeLanguageControllerAction;
import com.commercetools.sunrise.common.localization.changelanguage.DefaultChangeLanguageFormData;
import com.commercetools.sunrise.common.localization.changelanguage.SunriseChangeLanguageController;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.HomeReverseRouter;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class ChangeLanguageController extends SunriseChangeLanguageController<DefaultChangeLanguageFormData> {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public ChangeLanguageController(final FormFactory formFactory,
                                    final ChangeLanguageControllerAction changeLanguageControllerAction,
                                    final HomeReverseRouter homeReverseRouter) {
        super(formFactory, changeLanguageControllerAction);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public Class<DefaultChangeLanguageFormData> getFormDataClass() {
        return DefaultChangeLanguageFormData.class;
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Void input, final Form<DefaultChangeLanguageFormData> form) {
        return redirectTo(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<DefaultChangeLanguageFormData> form, final ClientErrorException clientErrorException) {
        return redirectTo(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output, final DefaultChangeLanguageFormData formData) {
        return redirectTo(homeReverseRouter.homePageCall(formData.getLanguage()));
    }
}
