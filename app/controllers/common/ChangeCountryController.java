package controllers.common;

import com.commercetools.sunrise.common.localization.changecountry.ChangeCountryFormAction;
import com.commercetools.sunrise.common.localization.changecountry.ChangeCountryFormData;
import com.commercetools.sunrise.common.localization.changecountry.SunriseChangeCountryController;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import controllers.productcatalog.routes;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class ChangeCountryController extends SunriseChangeCountryController {

    @Inject
    public ChangeCountryController(final FormFactory formFactory,
                                   final ChangeCountryFormData formData,
                                   final ChangeCountryFormAction controllerAction) {
        super(formFactory, formData, controllerAction);
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Void input, final Form<? extends ChangeCountryFormData> form) {
        return redirectAsync(routes.HomeController.show());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<? extends ChangeCountryFormData> form, final ClientErrorException clientErrorException) {
        return redirectAsync(routes.HomeController.show());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output, final ChangeCountryFormData formData) {
        return redirectAsync(routes.HomeController.show());
    }
}
