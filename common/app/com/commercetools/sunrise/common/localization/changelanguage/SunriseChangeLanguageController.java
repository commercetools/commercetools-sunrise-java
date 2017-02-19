package com.commercetools.sunrise.common.localization.changelanguage;

import com.commercetools.sunrise.framework.controllers.SunriseFormController;
import com.commercetools.sunrise.framework.controllers.WithFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.common.LocalizationReverseRouter;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseChangeLanguageController<F extends ChangeLanguageFormData> extends SunriseFormController implements WithFormFlow<F, Void, Void> {

    private final ChangeLanguageControllerAction changeLanguageControllerAction;

    protected SunriseChangeLanguageController(final FormFactory formFactory,
                                              final ChangeLanguageControllerAction changeLanguageControllerAction) {
        super(formFactory);
        this.changeLanguageControllerAction = changeLanguageControllerAction;
    }

    @RunRequestStartedHook
    @SunriseRoute(LocalizationReverseRouter.CHANGE_LANGUAGE_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final F formData) {
        changeLanguageControllerAction.accept(formData);
        return completedFuture(null);
    }
}
