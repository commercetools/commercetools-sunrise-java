package controllers.common;

import com.commercetools.sunrise.common.localization.changelanguage.ChangeLanguageControllerAction;
import com.commercetools.sunrise.common.localization.changelanguage.ChangeLanguageFormData;
import com.commercetools.sunrise.common.localization.changelanguage.SunriseChangeLanguageController;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
public final class ChangeLanguageController extends SunriseChangeLanguageController {

    @Inject
    public ChangeLanguageController(final FormFactory formFactory,
                                    final ChangeLanguageFormData formData,
                                    final ChangeLanguageControllerAction controllerAction) {
        super(formFactory, formData, controllerAction);
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Void input, final Form<? extends ChangeLanguageFormData> form) {
        return completedFuture(redirect(request().getHeader(Http.HeaderNames.REFERER)));
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final Form<? extends ChangeLanguageFormData> form, final ClientErrorException clientErrorException) {
        return completedFuture(redirect(request().getHeader(Http.HeaderNames.REFERER)));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Void output, final ChangeLanguageFormData formData) {
        return completedFuture(redirect(request().getHeader(Http.HeaderNames.REFERER)));
    }
}
