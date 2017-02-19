package com.commercetools.sunrise.common.localization.changecountry;

import com.commercetools.sunrise.framework.controllers.SunriseFormController;
import com.commercetools.sunrise.framework.controllers.WithFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.common.LocalizationReverseRouter;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseChangeCountryController<F extends ChangeCountryFormData> extends SunriseFormController implements WithFormFlow<F, Void, Void> {

    private final ChangeCountryControllerAction changeCountryControllerAction;

    protected SunriseChangeCountryController(final FormFactory formFactory,
                                             final ChangeCountryControllerAction changeCountryControllerAction) {
        super(formFactory);
        this.changeCountryControllerAction = changeCountryControllerAction;
    }

    @RunRequestStartedHook
    @SunriseRoute(LocalizationReverseRouter.CHANGE_COUNTRY_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return processForm(null);
    }

    @Override
    public CompletionStage<Void> executeAction(final Void input, final F formData) {
        changeCountryControllerAction.accept(formData);
        return completedFuture(null);
    }
}
