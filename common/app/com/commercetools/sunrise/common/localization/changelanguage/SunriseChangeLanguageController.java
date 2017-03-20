package com.commercetools.sunrise.common.localization.changelanguage;

import com.commercetools.sunrise.framework.controllers.SunriseFormController;
import com.commercetools.sunrise.framework.controllers.WithFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.common.localization.LocalizationReverseRouter;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseChangeLanguageController extends SunriseFormController
        implements WithFormFlow<Void, Void, ChangeLanguageFormData> {

    private final ChangeLanguageFormData formData;
    private final ChangeLanguageControllerAction controllerAction;

    protected SunriseChangeLanguageController(final FormFactory formFactory, final ChangeLanguageFormData formData,
                                              final ChangeLanguageControllerAction controllerAction) {
        super(formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends ChangeLanguageFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(LocalizationReverseRouter.CHANGE_LANGUAGE_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final ChangeLanguageFormData formData) {
        controllerAction.accept(formData);
        return completedFuture(null);
    }
}
