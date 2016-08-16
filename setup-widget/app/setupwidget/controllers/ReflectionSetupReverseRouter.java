package setupwidget.controllers;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import com.commercetools.sunrise.common.reverserouter.ReflectionReverseRouterBase;
import com.commercetools.sunrise.common.reverserouter.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionSetupReverseRouter extends ReflectionReverseRouterBase implements SetupReverseRouter {

    private ReverseCaller processSetupFormCaller;

    @Inject
    private void setRoutes(final ParsedRoutes parsedRoutes) {
        processSetupFormCaller = getCallerForRoute(parsedRoutes, "processSetupFormCall");
    }


    @Override
    public Call processSetupFormCall() {
        return processSetupFormCaller.call();
    }
}
