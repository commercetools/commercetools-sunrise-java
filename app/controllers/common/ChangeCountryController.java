package controllers.common;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.common.localization.changecountry.CountryFormData;
import com.commercetools.sunrise.common.localization.changecountry.SunriseChangeCountryController;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.HomeReverseRouter;
import com.commercetools.sunrise.sessions.country.CountryInSession;
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
    public ChangeCountryController(final FormFactory formFactory,
                                   final CountryInSession countryInSession,
                                   final HomeReverseRouter homeReverseRouter) {
        super(formFactory, countryInSession);
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
