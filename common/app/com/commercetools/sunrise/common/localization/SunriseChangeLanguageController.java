package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.controllers.SunriseFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.reverserouter.common.LocalizationLinksControllerComponent;
import com.commercetools.sunrise.common.sessions.language.LanguageInSession;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RegisteredComponents;
import com.commercetools.sunrise.hooks.RunRequestStartedHook;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RegisteredComponents(LocalizationLinksControllerComponent.class)
public abstract class SunriseChangeLanguageController<F extends LanguageFormData> extends SunriseFormController implements WithFormFlow<F, Void, Void> {

    private final LanguageInSession languageInSession;

    protected SunriseChangeLanguageController(final FormFactory formFactory, final LanguageInSession languageInSession) {
        super(formFactory);
        this.languageInSession = languageInSession;
    }

    @RunRequestStartedHook
    @SunriseRoute("processChangeLanguageForm")
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final F formData) {
        languageInSession.store(formData.toLocale());
        return completedFuture(null);
    }
}
