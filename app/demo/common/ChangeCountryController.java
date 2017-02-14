package demo.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.localization.CountryFormData;
import com.commercetools.sunrise.common.localization.CountryInSession;
import com.commercetools.sunrise.common.localization.SunriseChangeCountryController;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.hooks.RequestHookContext;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class ChangeCountryController extends SunriseChangeCountryController<CountryFormData> {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public ChangeCountryController(final RequestHookContext hookContext,
                                   final FormFactory formFactory,
                                   final CountryInSession countryInSession,
                                   final HomeReverseRouter homeReverseRouter) {
        super(hookContext, formFactory, countryInSession);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public Class<CountryFormData> getFormDataClass() {
        return CountryFormData.class;
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Void input, final Form<CountryFormData> form) {
        return redirectTo(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<CountryFormData> form, final ClientErrorException clientErrorException) {
        return redirectTo(homeReverseRouter.homePageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output, final CountryFormData formData) {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
