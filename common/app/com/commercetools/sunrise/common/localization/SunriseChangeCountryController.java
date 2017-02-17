package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.controllers.SunriseFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.sessions.country.CountryInSession;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@IntroducingMultiControllerComponents(LocalizationThemeLinksControllerComponent.class)
public abstract class SunriseChangeCountryController<F extends CountryFormData> extends SunriseFormController implements WithFormFlow<F, Void, Void> {

    private final CountryInSession countryInSession;

    protected SunriseChangeCountryController(final ComponentRegistry componentRegistry, final FormFactory formFactory,
                                             final CountryInSession countryInSession) {
        super(componentRegistry, formFactory);
        this.countryInSession = countryInSession;
    }

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
