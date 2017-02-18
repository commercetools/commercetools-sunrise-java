package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.controllers.SunriseFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.reverserouter.common.LocalizationLinksControllerComponent;
import com.commercetools.sunrise.common.sessions.country.CountryInSession;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RegisteredComponents;
import com.commercetools.sunrise.hooks.RunRequestStartedHook;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RegisteredComponents(LocalizationLinksControllerComponent.class)
public abstract class SunriseChangeCountryController<F extends CountryFormData> extends SunriseFormController implements WithFormFlow<F, Void, Void> {

    private final CountryInSession countryInSession;

    protected SunriseChangeCountryController(final FormFactory formFactory, final CountryInSession countryInSession) {
        super(formFactory);
        this.countryInSession = countryInSession;
    }

    @RunRequestStartedHook
    @SunriseRoute("processChangeCountryForm")
    public CompletionStage<Result> process(final String languageTag) {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final F formData) {
        countryInSession.store(formData.toCountryCode());
        return completedFuture(null);
    }
}
