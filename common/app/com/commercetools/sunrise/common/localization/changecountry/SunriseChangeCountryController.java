package com.commercetools.sunrise.common.localization.changecountry;

import com.commercetools.sunrise.controllers.SunriseFormController;
import com.commercetools.sunrise.controllers.WithFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.common.LocalizationReverseRouter;
import com.commercetools.sunrise.sessions.country.CountryInSession;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseChangeCountryController<F extends CountryFormData> extends SunriseFormController implements WithFormFlow<F, Void, Void> {

    private final CountryInSession countryInSession;

    protected SunriseChangeCountryController(final FormFactory formFactory, final CountryInSession countryInSession) {
        super(formFactory);
        this.countryInSession = countryInSession;
    }

    @RunRequestStartedHook
    @SunriseRoute(LocalizationReverseRouter.CHANGE_COUNTRY_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final F formData) {
        countryInSession.store(formData.toCountryCode());
        return completedFuture(null);
    }
}
