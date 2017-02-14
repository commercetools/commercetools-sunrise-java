package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.controllers.SunriseTemplatelessFormController;
import com.commercetools.sunrise.common.controllers.WithTemplatelessFormFlow;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@IntroducingMultiControllerComponents(LocalizationThemeLinksControllerComponent.class)
public abstract class SunriseChangeCountryController<F extends CountryFormData> extends SunriseTemplatelessFormController implements WithTemplatelessFormFlow<Void, Void, F> {

    private final CountryInSession countryInSession;

    protected SunriseChangeCountryController(final RequestHookContext hookContext, final FormFactory formFactory,
                                             final CountryInSession countryInSession) {
        super(hookContext, formFactory);
        this.countryInSession = countryInSession;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("localization-controller", "country"));
    }

    @SunriseRoute("processChangeCountryForm")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> processForm(null));
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final F formData) {
        countryInSession.store(formData.toCountryCode());
        return completedFuture(null);
    }
}
