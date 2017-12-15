package com.commercetools.sunrise.common.localization.changecountry;

import com.commercetools.sunrise.core.controllers.SunriseFormController;
import com.commercetools.sunrise.core.controllers.WithFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.common.localization.LocalizationReverseRouter;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseChangeCountryController extends SunriseFormController
        implements WithFormFlow<Void, Void, ChangeCountryFormData> {

    private final ChangeCountryFormData formData;
    private final ChangeCountryControllerAction controllerAction;

    protected SunriseChangeCountryController(final FormFactory formFactory, final ChangeCountryFormData formData,
                                             final ChangeCountryControllerAction controllerAction) {
        super(formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends ChangeCountryFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(LocalizationReverseRouter.CHANGE_COUNTRY_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final ChangeCountryFormData formData) {
        controllerAction.accept(formData);
        return completedFuture(null);
    }
}
