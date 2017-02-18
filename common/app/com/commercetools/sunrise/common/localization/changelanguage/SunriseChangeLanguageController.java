package com.commercetools.sunrise.common.localization.changelanguage;

import com.commercetools.sunrise.controllers.SunriseFormController;
import com.commercetools.sunrise.controllers.WithFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.common.LocalizationReverseRouter;
import com.commercetools.sunrise.sessions.language.LanguageInSession;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseChangeLanguageController<F extends LanguageFormData> extends SunriseFormController implements WithFormFlow<F, Void, Void> {

    private final LanguageInSession languageInSession;

    protected SunriseChangeLanguageController(final FormFactory formFactory, final LanguageInSession languageInSession) {
        super(formFactory);
        this.languageInSession = languageInSession;
    }

    @RunRequestStartedHook
    @SunriseRoute(LocalizationReverseRouter.CHANGE_LANGUAGE_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final F formData) {
        languageInSession.store(formData.toLocale());
        return completedFuture(null);
    }
}
