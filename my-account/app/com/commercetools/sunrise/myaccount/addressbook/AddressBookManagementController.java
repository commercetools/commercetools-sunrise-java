package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.errors.UserFeedback;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.myaccount.common.MyAccountController;
import com.google.inject.Injector;
import io.sphere.sdk.models.SphereException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

import static com.commercetools.sunrise.common.utils.FormUtils.extractUserFeedback;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AddressBookManagementController extends MyAccountController {

    protected static final Logger logger = LoggerFactory.getLogger(AddressBookManagementController.class);

    @Inject
    private Injector injector;

    protected CompletableFuture<Result> redirectToAddressBook() {
        final Call call = injector.getInstance(AddressBookReverseRouter.class).showMyAddressBook(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    protected final void saveFormErrors(final Form<?> form) {
        final Http.Context context = injector.getInstance(Http.Context.class);
        context.flash().putAll(extractUserFeedback(form));
    }

    protected final void saveUnexpectedError(final SphereException sphereException) {
        logger.error("The CTP request raised an unexpected exception", sphereException);
        final Http.Context context = injector.getInstance(Http.Context.class);
        context.flash().put(UserFeedback.ERROR, "Something went wrong, please try again"); // TODO get from i18n
    }
}
