package controllers.common;

import com.commercetools.sunrise.common.localization.changelanguage.DefaultLanguageFormData;
import com.commercetools.sunrise.common.localization.changelanguage.SunriseChangeLanguageController;
import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.HomeReverseRouter;
import com.commercetools.sunrise.sessions.language.LanguageInSession;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class ChangeLanguageController extends SunriseChangeLanguageController<DefaultLanguageFormData> {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public ChangeLanguageController(final FormFactory formFactory,
                                    final LanguageInSession languageInSession,
                                    final HomeReverseRouter homeReverseRouter) {
        super(formFactory, languageInSession);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public Class<DefaultLanguageFormData> getFormDataClass() {
        return DefaultLanguageFormData.class;
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Void input, final Form<DefaultLanguageFormData> form) {
        return redirectTo(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<DefaultLanguageFormData> form, final ClientErrorException clientErrorException) {
        return redirectTo(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output, final DefaultLanguageFormData formData) {
        return redirectTo(homeReverseRouter.homePageCall(formData.getLanguage()));
    }
}
