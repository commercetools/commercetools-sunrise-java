package demo.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.localization.LanguageFormData;
import com.commercetools.sunrise.common.localization.SunriseChangeLanguageController;
import com.commercetools.sunrise.common.reverserouter.productcatalog.HomeReverseRouter;
import com.commercetools.sunrise.common.sessions.language.LanguageInSession;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class ChangeLanguageController extends SunriseChangeLanguageController<LanguageFormData> {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public ChangeLanguageController(final FormFactory formFactory,
                                    final LanguageInSession languageInSession,
                                    final HomeReverseRouter homeReverseRouter) {
        super(formFactory, languageInSession);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public Class<LanguageFormData> getFormDataClass() {
        return LanguageFormData.class;
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Void input, final Form<LanguageFormData> form) {
        return redirectTo(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<LanguageFormData> form, final ClientErrorException clientErrorException) {
        return redirectTo(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output, final LanguageFormData formData) {
        return redirectTo(homeReverseRouter.homePageCall(formData.getLanguage()));
    }
}
