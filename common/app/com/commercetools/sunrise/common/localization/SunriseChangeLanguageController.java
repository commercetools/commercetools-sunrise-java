package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.controllers.SunriseFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
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
public abstract class SunriseChangeLanguageController<F extends LanguageFormData> extends SunriseFormController implements WithFormFlow<F, Void, Void> {

    private final LanguageInSession languageInSession;

    protected SunriseChangeLanguageController(final RequestHookContext hookContext, final FormFactory formFactory,
                                              final LanguageInSession languageInSession) {
        super(hookContext, formFactory);
        this.languageInSession = languageInSession;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("localization-controller", "language"));
    }

    @SunriseRoute("processChangeLanguageForm")
    public CompletionStage<Result> process() {
        return doRequest(() -> processForm(null));
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final F formData) {
        languageInSession.store(formData.toLocale());
        return completedFuture(null);
    }
}
